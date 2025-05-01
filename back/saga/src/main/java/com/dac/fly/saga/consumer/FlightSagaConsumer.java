package com.dac.fly.saga.consumer;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.dac.fly.shared.config.RabbitConstants;
import com.dac.fly.shared.dto.events.FlightReservationsCancelledEventDto;
import com.dac.fly.shared.dto.events.MilesUpdatedEvent;
import com.dac.fly.shared.dto.response.CancelledFlightResponseDto;

@Component
public class FlightSagaConsumer {

    private final ConcurrentHashMap<String, CompletableFuture<CancelledFlightResponseDto>> flightCancelResponses;
    private final ConcurrentHashMap<String, CompletableFuture<FlightReservationsCancelledEventDto>> reservationsCancelResponses;
    // private final ConcurrentHashMap<String, CompletableFuture<MilesUpdatedEvent>> milesResponses;

    public FlightSagaConsumer(
            ConcurrentHashMap<String, CompletableFuture<CancelledFlightResponseDto>> flightCancelResponses,
            ConcurrentHashMap<String, CompletableFuture<FlightReservationsCancelledEventDto>> reservationsCancelResponses,
            ConcurrentHashMap<String, CompletableFuture<MilesUpdatedEvent>> milesResponses) {
        this.flightCancelResponses = flightCancelResponses;
        this.reservationsCancelResponses = reservationsCancelResponses;
        // this.milesResponses = milesResponses;
    }

    @RabbitListener(queues = RabbitConstants.FLIGHT_CANCELLED_RESP_QUEUE)
    public void onFlightCancelled(CancelledFlightResponseDto evt) {
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
        }
    }
}
