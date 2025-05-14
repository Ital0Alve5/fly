package com.dac.fly.reservationservice.consumer;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.dac.fly.reservationservice.config.RabbitMQConfig;
import com.dac.fly.reservationservice.dto.events.ReservationStatusUpdatedEventDto;
import com.dac.fly.reservationservice.service.query.ReservationQueryService;
import com.dac.fly.shared.dto.events.CancelledReservationEventDto;
import com.dac.fly.shared.dto.events.FlightReservationsCancelledEventDto;
import com.dac.fly.shared.dto.response.CreatedReservationResponseDto;
import com.dac.fly.shared.dto.events.FlightReservationsCompletedEventDto;

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
    public void handleCanceledReservationEvent(CancelledReservationEventDto evt) {
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

    @RabbitListener(queues = RabbitMQConfig.INTERNAL_COMPENSATE_CREATE_KEY)
    public void handleReservationRemovedEvent(String reservationId) {
        boolean ok = service.deleteReservation(reservationId);
        if (!ok) {
            throw new AmqpRejectAndDontRequeueException(
                    "Erro ao projetar remoção de reserva " + reservationId);
        }
    }

    @RabbitListener(queues = RabbitMQConfig.INTERNAL_COMPLETE_FLIGHT_KEY)
    public void handleReservationRemovedEvent(FlightReservationsCompletedEventDto evt) {
        boolean ok = service.handleFlightReservationsCompleted(evt);
        if (!ok) {
            throw new AmqpRejectAndDontRequeueException(
                    "Erro ao projetar remoção de reserva " + evt.flightCode());
        }
    }

    @RabbitListener(queues = RabbitMQConfig.INTERNAL_UPDATED_KEY)
    public void handleUpdateReservationStatusEvent(ReservationStatusUpdatedEventDto evt) {
        boolean ok = service.handleUpdateReservationStatus(evt.reservationCode(), evt.newStatus());
        if (!ok) {
            throw new AmqpRejectAndDontRequeueException("Erro ao projetar atualização de status de reserva " + evt.reservationCode());
        }
    }
}
