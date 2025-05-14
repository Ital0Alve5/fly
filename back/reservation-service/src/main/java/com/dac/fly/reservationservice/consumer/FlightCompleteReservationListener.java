package com.dac.fly.reservationservice.consumer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.dac.fly.reservationservice.config.RabbitMQConfig;
import com.dac.fly.reservationservice.entity.command.Reserva;
import com.dac.fly.reservationservice.publisher.FlightReservationPublisher;
import com.dac.fly.reservationservice.repository.command.ReservaCommandRepository;
import com.dac.fly.reservationservice.service.command.ReservationCommandService;
import com.dac.fly.shared.config.RabbitConstants;
import com.dac.fly.shared.dto.command.CreateReservationCommand;
import com.dac.fly.shared.dto.events.FlightReservationsCompletedEventDto;
import com.dac.fly.shared.dto.command.CompleteFlightDto ;
import com.dac.fly.shared.dto.response.CreatedReservationResponseDto;

@Component
public class FlightCompleteReservationListener {

    private final RabbitTemplate rabbit;
    private final DirectExchange internalExchange;
    private final ReservationCommandService reservationService;
    private final ReservaCommandRepository commandRepo;
    private final FlightReservationPublisher publisher;

    public FlightCompleteReservationListener(
            RabbitTemplate rabbit,
            @Qualifier("internalExchange") DirectExchange internalExchange,
            ReservationCommandService reservationService,
            ReservaCommandRepository commandRepo,
            FlightReservationPublisher publisher) {
        this.rabbit = rabbit;
        this.internalExchange = internalExchange;
        this.reservationService = reservationService;
        this.commandRepo = commandRepo;
        this.publisher = publisher;
    }

    @RabbitListener(queues = RabbitConstants.COMPLETE_RESERVATION_BY_FLIGHT_CMD_QUEUE)
    public void onFlightCompleted(CompleteFlightDto cmd) {
        List<String> reservationIds = commandRepo
                .findByCodigoVoo(cmd.codigo())
                .stream()
                .map(Reserva::getCodigo)
                .toList();

        for (String resId : reservationIds) {
            reservationService.completeReservationByFlight(resId);
        }

        var evt = new FlightReservationsCompletedEventDto(cmd.codigo(), reservationIds);

        publisher.publishFlightReservationsCompletedForCqrs(evt);

        publisher.publishCompletedFlightReservations(evt);
    }
}
