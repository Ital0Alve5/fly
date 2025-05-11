package com.dac.fly.saga.consumer;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.dac.fly.shared.config.RabbitConstants;
import com.dac.fly.shared.dto.command.CreateReservationCommand;
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
    private final ConcurrentHashMap<String, CompletableFuture<CreateReservationCommand>> failedReservationResponses;

    public ReservationSagaConsumer(
            ConcurrentHashMap<String, CompletableFuture<MilesUpdatedEvent>> milesResponses,
            ConcurrentHashMap<String, CompletableFuture<SeatsUpdatedEvent>> seatsResponses,
            ConcurrentHashMap<String, CompletableFuture<CreatedReservationResponseDto>> reservationResponses,
            ConcurrentHashMap<String, CompletableFuture<CanceledReservationResponseDto>> reservationCancelResponses,
            ConcurrentHashMap<String, CompletableFuture<CreateReservationCommand>> failedReservationResponses) {
        this.milesResponses = milesResponses;
        this.seatsResponses = seatsResponses;
        this.reservationResponses = reservationResponses;
        this.reservationCancelResponses = reservationCancelResponses;
        this.failedReservationResponses = failedReservationResponses;
    }

    @RabbitListener(queues = RabbitConstants.UPDATE_MILES_RESP_QUEUE)
    public void onMilesDeducted(MilesUpdatedEvent evt) {
        var future = milesResponses.remove(evt.codigo());
        if (future != null) {
            future.complete(evt);
        }
    }

    @RabbitListener(queues = RabbitConstants.UPDATE_SEATS_RESP_QUEUE)
    public void onSeatsUpdated(SeatsUpdatedEvent evt) {
        var future = seatsResponses.remove(evt.codigo());
        if (future != null) {
            future.complete(evt);
        }
    }

    @RabbitListener(queues = RabbitConstants.CREATED_QUEUE)
    public void onReservationCreated(CreatedReservationResponseDto evt) {
        var future = reservationResponses.remove(evt.codigo());
        if (future != null) {
            future.complete(evt);
        }
    }

    @RabbitListener(queues = RabbitConstants.CANCELED_RESERVATION_RESP_QUEUE)
    public void onReservationCanceled(CanceledReservationResponseDto evt) {
        var future = reservationCancelResponses.remove(evt.codigo());
        if (future != null) {
            future.complete(evt);
        }
    }

    @RabbitListener(queues = RabbitConstants.FAILED_CREATE_RESERVATION_QUEUE)
    public void onCreateReservationFailed(CreateReservationCommand cmd) {
        var future = failedReservationResponses.remove(cmd.codigo());
        if (future != null) {
            future.complete(cmd);
        }
    }
}
