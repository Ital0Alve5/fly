package com.dac.fly.saga.service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.dac.fly.shared.config.RabbitConstants;
import com.dac.fly.shared.dto.command.UpdateMilesCommand;
import com.dac.fly.shared.dto.events.FlightReservationsCancelledEventDto;
import com.dac.fly.shared.dto.events.MilesUpdatedEvent;
import com.dac.fly.shared.dto.request.CancelFlightDto;
import com.dac.fly.shared.dto.response.CancelledFlightResponseDto;

@Service
public class FlightSagaOrchestrator {

    private static final long TIMEOUT_SECONDS = 10;

    private final RabbitTemplate rabbit;
    private final ConcurrentHashMap<String, CompletableFuture<CancelledFlightResponseDto>> flightCancelResponses;
    private final ConcurrentHashMap<String, CompletableFuture<FlightReservationsCancelledEventDto>> reservationsCancelResponses;
    private final ConcurrentHashMap<String, CompletableFuture<MilesUpdatedEvent>> milesResponses;

    public FlightSagaOrchestrator(
            RabbitTemplate rabbit,
            ConcurrentHashMap<String, CompletableFuture<CancelledFlightResponseDto>> flightCancelResponses,
            ConcurrentHashMap<String, CompletableFuture<FlightReservationsCancelledEventDto>> reservationsCancelResponses,
            ConcurrentHashMap<String, CompletableFuture<MilesUpdatedEvent>> milesResponses) {
        this.rabbit = rabbit;
        this.flightCancelResponses = flightCancelResponses;
        this.reservationsCancelResponses = reservationsCancelResponses;
        this.milesResponses = milesResponses;
    }

    public CancelledFlightResponseDto cancelFlightSaga(String flightCode) {
        CompletableFuture<CancelledFlightResponseDto> flightFuture = new CompletableFuture<>();
        flightCancelResponses.put(flightCode, flightFuture);

        System.err.println("1");
        rabbit.convertAndSend(
                RabbitConstants.EXCHANGE,
                RabbitConstants.CANCEL_FLIGHT_CMD_QUEUE,
                new CancelFlightDto(flightCode));
        System.err.println("6");

        CancelledFlightResponseDto flightEvt = getWithTimeout(flightCancelResponses, flightCode);
        System.err.println("7");

        rabbit.convertAndSend(
                RabbitConstants.EXCHANGE,
                RabbitConstants.CANCEL_RESERVATION_BY_FLIGHT_CMD_QUEUE,
                new CancelFlightDto(flightCode));

        CompletableFuture<FlightReservationsCancelledEventDto> resFuture = new CompletableFuture<>();
        reservationsCancelResponses.put(flightCode, resFuture);

        FlightReservationsCancelledEventDto resEvt = getWithTimeout(reservationsCancelResponses, flightCode);

        for (var cm : resEvt.refunds()) {
            String key = flightCode + "-" + cm.codigoCliente();
            CompletableFuture<MilesUpdatedEvent> milesFuture = new CompletableFuture<>();
            milesResponses.put(key, milesFuture);

            rabbit.convertAndSend(
                    RabbitConstants.EXCHANGE,
                    RabbitConstants.UPDATE_MILES_CMD_QUEUE,
                    new UpdateMilesCommand(
                            key,
                            cm.codigoCliente(),
                            cm.milhasUtilizadas(),
                            true));

            getWithTimeout(milesResponses, key);
        }

        return flightEvt;
    }

    private <T> T getWithTimeout(
            ConcurrentHashMap<String, CompletableFuture<T>> map,
            String key) {
        CompletableFuture<T> future = map.get(key);
        try {
            return future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            throw new RuntimeException("Erro/timeout na chave " + key, ex);
        } finally {
            map.remove(key);
        }
    }
}
