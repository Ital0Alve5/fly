package com.dac.fly.saga.consumer;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.dac.fly.shared.config.RabbitConstants;
import com.dac.fly.shared.dto.events.CancelledFlightEventDto;
import com.dac.fly.shared.dto.events.FlightReservationsCancelledEventDto;
import com.dac.fly.shared.dto.events.MilesUpdatedEvent;

@Component
public class FlightSagaConsumer {

    private final ConcurrentHashMap<String, CompletableFuture<CancelledFlightEventDto>> flightCancelResponses;
    private final ConcurrentHashMap<String, CompletableFuture<FlightReservationsCancelledEventDto>> reservationsCancelResponses;

    public FlightSagaConsumer(
            ConcurrentHashMap<String, CompletableFuture<CancelledFlightEventDto>> flightCancelResponses,
            ConcurrentHashMap<String, CompletableFuture<FlightReservationsCancelledEventDto>> reservationsCancelResponses,
            ConcurrentHashMap<String, CompletableFuture<MilesUpdatedEvent>> milesResponses) {
        this.flightCancelResponses = flightCancelResponses;
        this.reservationsCancelResponses = reservationsCancelResponses;
    }

    @RabbitListener(queues = RabbitConstants.FLIGHT_CANCELLED_RESP_QUEUE)
    public void onFlightCancelled(CancelledFlightEventDto evt) {
        var future = flightCancelResponses.remove(evt.codigo());
        if (future != null) {
            future.complete(evt);
        }
    }

    @RabbitListener(queues = RabbitConstants.FLIGHT_RESERVATIONS_CANCELLED_QUEUE)
    public void onFlightReservationsCancelled(FlightReservationsCancelledEventDto evt) {

        var future = reservationsCancelResponses.remove(evt.flightCode());

        if (future != null) {
            future.complete(evt);
        } else {
            System.err.println("FUTURE NULO: nada foi completado");
        }
    }
}
