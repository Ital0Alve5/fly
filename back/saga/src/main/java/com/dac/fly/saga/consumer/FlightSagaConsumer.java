package com.dac.fly.saga.consumer;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import com.dac.fly.shared.dto.events.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.dac.fly.shared.config.RabbitConstants;

@Component
public class FlightSagaConsumer {

    private final ConcurrentHashMap<String, CompletableFuture<CancelledFlightEventDto>> flightCancelResponses;
    private final ConcurrentHashMap<String, CompletableFuture<FlightReservationsCancelledEventDto>> reservationsCancelResponses;
    private final ConcurrentHashMap<String, CompletableFuture<CompletedFlightEventDto>> flightCompleteResponses;
    private final ConcurrentHashMap<String, CompletableFuture<FlightReservationsCompletedEventDto>> reservationsCompletedResponses;

    public FlightSagaConsumer(
            ConcurrentHashMap<String, CompletableFuture<CancelledFlightEventDto>> flightCancelResponses,
            ConcurrentHashMap<String, CompletableFuture<FlightReservationsCancelledEventDto>> reservationsCancelResponses,
            ConcurrentHashMap<String, CompletableFuture<MilesUpdatedEvent>> milesResponses,
            ConcurrentHashMap<String, CompletableFuture<CompletedFlightEventDto>> flightCompleteResponses,
            ConcurrentHashMap<String, CompletableFuture<FlightReservationsCompletedEventDto>> reservationsCompletedResponses
            ) {
        this.flightCancelResponses = flightCancelResponses;
        this.reservationsCancelResponses = reservationsCancelResponses;
        this.flightCompleteResponses = flightCompleteResponses;
        this.reservationsCompletedResponses = reservationsCompletedResponses;
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


    @RabbitListener(queues = RabbitConstants.FLIGHT_COMPLETED_RESP_QUEUE)
    public void onFlightCompleted(CompletedFlightEventDto evt) {
        var future = flightCompleteResponses.remove(evt.codigo());
        if (future != null) {
            future.complete(evt);
        }
    }

    @RabbitListener(queues = RabbitConstants.FLIGHT_RESERVATIONS_COMPLETED_QUEUE)
    public void onFlightReservationsCompleted(FlightReservationsCompletedEventDto evt) {
        var future = reservationsCompletedResponses.remove(evt.flightCode());

        if (future != null) {
            future.complete(evt);
        } else {
            System.err.println("FUTURE NULO: nada foi completado");
        }
    }
}
