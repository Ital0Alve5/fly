package com.dac.fly.reservationservice.consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.dac.fly.reservationservice.entity.command.Reserva;
import com.dac.fly.reservationservice.publisher.FlightReservationPublisher;
import com.dac.fly.reservationservice.repository.command.ReservaCommandRepository;
import com.dac.fly.reservationservice.service.command.ReservationCommandService;
import com.dac.fly.shared.config.RabbitConstants;
import com.dac.fly.shared.dto.events.ClientMilesDto;
import com.dac.fly.shared.dto.events.FlightReservationsCancelledEventDto;
import com.dac.fly.shared.dto.request.CancelFlightDto;
import com.dac.fly.shared.dto.response.CanceledReservationResponseDto;

@Service
public class FlightCancelReservationListener {

    private final ReservaCommandRepository commandRepo;
    private final ReservationCommandService reservationService;
    private final FlightReservationPublisher publisher;
    private final Set<String> processedFlights = ConcurrentHashMap.newKeySet();

    public FlightCancelReservationListener(
            ReservaCommandRepository commandRepo,
            ReservationCommandService reservationService,
            FlightReservationPublisher publisher) {
        this.commandRepo = commandRepo;
        this.reservationService = reservationService;
        this.publisher = publisher;
    }

    @RabbitListener(queues = RabbitConstants.CANCEL_RESERVATION_BY_FLIGHT_CMD_QUEUE)
    public void onFlightCancelled(CancelFlightDto cmd) {
        if (!processedFlights.add(cmd.codigo())) {
            return;
        }

        List<String> reservationIds = commandRepo
                .findByCodigoVoo(cmd.codigo())
                .stream()
                .map(Reserva::getCodigo)
                .toList();

        List<ClientMilesDto> refunds = new ArrayList<>(reservationIds.size());
        for (String resId : reservationIds) {
            CanceledReservationResponseDto cancelDto = reservationService.cancelReservationByCode(resId);

            refunds.add(new ClientMilesDto(
                    cancelDto.codigo_cliente(),
                    cancelDto.milhas_utilizadas()));
        }

        var evt = new FlightReservationsCancelledEventDto(cmd.codigo(), refunds);
        publisher.publishCancelledFlightReservations(evt);
    }
}
