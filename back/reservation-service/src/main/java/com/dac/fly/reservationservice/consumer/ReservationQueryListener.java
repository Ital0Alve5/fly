package com.dac.fly.reservationservice.consumer;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.dac.fly.reservationservice.config.RabbitMQConfig;
import com.dac.fly.reservationservice.service.query.ReservationQueryService;
import com.dac.fly.shared.dto.events.FlightReservationsCancelledEventDto;
import com.dac.fly.shared.dto.response.CanceledReservationResponseDto;
import com.dac.fly.shared.dto.response.CreatedReservationResponseDto;

@Component
public class ReservationQueryListener {

    private final ReservationQueryService service;

    public ReservationQueryListener(ReservationQueryService service) {
        this.service = service;
    }

    @RabbitListener(queues = RabbitMQConfig.INTERNAL_CREATED_KEY)
    public void handleCreatedReservationEvent(CreatedReservationResponseDto evt) {
        boolean ok = service.saveCreatedReservation(evt);
        if (!ok) {
            throw new AmqpRejectAndDontRequeueException("Erro ao projetar reserva " + evt.codigo());
        }
    }

    @RabbitListener(queues = RabbitMQConfig.INTERNAL_CANCELLED_KEY)
    public void handleCanceledReservationEvent(CanceledReservationResponseDto evt) {
        boolean ok = service.saveCanceledReservation(evt);
        if (!ok) {
            throw new AmqpRejectAndDontRequeueException("Erro ao projetar reserva cancelada " + evt.codigo());
        }
    }

    @RabbitListener(queues = RabbitMQConfig.INTERNAL_CANCEL_FLIGHT_KEY)
    public void onFlightReservationsCancelled(FlightReservationsCancelledEventDto evt) {
        boolean ok = service.handleFlightReservationsCancelled(evt);
        if (!ok) {
            throw new AmqpRejectAndDontRequeueException(
                    "Erro ao projetar cancelamento de reservas por voo: " + evt.flightCode());
        }
    }

}
