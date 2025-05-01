package com.dac.fly.reservationservice.consumer;

import java.util.ArrayList;
import java.util.List;

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
        List<String> reservationIds = commandRepo
                .findByCodigoVoo(cmd.codigo())
                .stream()
                .map(Reserva::getCodigo)
                .toList();

        List<ClientMilesDto> refunds = new ArrayList<>(reservationIds.size());

        for (String resId : reservationIds) {
            CanceledReservationResponseDto cancelDto = reservationService.cancelReservationByFlight(resId);

            refunds.add(new ClientMilesDto(
                    cancelDto.codigo(),
                    cancelDto.codigo_cliente(),
                    cancelDto.milhas_utilizadas()));
        }

        var evt = new FlightReservationsCancelledEventDto(cmd.codigo(), refunds);

        publisher.publishFlightReservationsCancelledForCqrs(evt);

        publisher.publishCancelledFlightReservations(evt);
    }
}
