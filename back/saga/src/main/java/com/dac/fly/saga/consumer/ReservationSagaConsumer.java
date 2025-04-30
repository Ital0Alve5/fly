package com.dac.fly.saga.consumer;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.dac.fly.shared.config.RabbitConstants;
import com.dac.fly.shared.dto.events.MilesUpdatedEvent;
import com.dac.fly.shared.dto.events.SeatsUpdatedEvent;
import com.dac.fly.shared.dto.response.CanceledReservationResponseDto;
import com.dac.fly.shared.dto.response.CreatedReservationResponseDto;

@Component
public class ReservationSagaConsumer {

    private final ConcurrentHashMap<String, CompletableFuture<MilesUpdatedEvent>> milesResponses;
    private final ConcurrentHashMap<String, CompletableFuture<SeatsUpdatedEvent>> seatsResponses;
    private final ConcurrentHashMap<String, CompletableFuture<CreatedReservationResponseDto>> reservationResponses;
    private final ConcurrentHashMap<String, CompletableFuture<CanceledReservationResponseDto>> reservationCancelResponses;

    public ReservationSagaConsumer(
            ConcurrentHashMap<String, CompletableFuture<MilesUpdatedEvent>> milesResponses,
            ConcurrentHashMap<String, CompletableFuture<SeatsUpdatedEvent>> seatsResponses,
            ConcurrentHashMap<String, CompletableFuture<CreatedReservationResponseDto>> reservationResponses,
            ConcurrentHashMap<String, CompletableFuture<CanceledReservationResponseDto>> reservationCancelResponses) {
        this.milesResponses = milesResponses;
        this.seatsResponses = seatsResponses;
        this.reservationResponses = reservationResponses;
        this.reservationCancelResponses = reservationCancelResponses;
    }

    @RabbitListener(queues = RabbitConstants.UPDATE_MILES_RESP_QUEUE)
    public void onMilesDeducted(MilesUpdatedEvent evt) {
        var future = milesResponses.get(evt.codigo());
        if (future != null) {
            future.complete(evt);
        }
    }

    @RabbitListener(queues = RabbitConstants.UPDATE_SEATS_RESP_QUEUE)
    public void onSeatsUpdated(SeatsUpdatedEvent evt) {
        var future = seatsResponses.get(evt.codigo());
        if (future != null) {
            future.complete(evt);
        }
    }

    @RabbitListener(queues = RabbitConstants.CREATED_QUEUE)
    public void onReservationCreated(CreatedReservationResponseDto evt) {
        var future = reservationResponses.get(evt.codigo());
        if (future != null) {
            future.complete(evt);
        }
    }

    @RabbitListener(queues = RabbitConstants.CANCELED_QUEUE)
    public void onReservationCanceled(CanceledReservationResponseDto evt) {
        var future = reservationCancelResponses.get(evt.codigo());
        if (future != null) {
            future.complete(evt);
        }
    }

}
