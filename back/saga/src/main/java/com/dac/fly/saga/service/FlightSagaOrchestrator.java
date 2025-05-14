package com.dac.fly.saga.service;

import java.util.EnumSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.dac.fly.shared.dto.response.CompletedFlightResponseDto;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.dac.fly.saga.enums.FlightCancelSagaStep;
import com.dac.fly.saga.feign.FlightClient;
import com.dac.fly.shared.config.RabbitConstants;
import com.dac.fly.shared.dto.command.CompensateCancelFlightCommand;
import com.dac.fly.shared.dto.command.CompensateCancelReservationCommand;
import com.dac.fly.shared.dto.command.UpdateMilesCommand;
import com.dac.fly.shared.dto.events.CancelledFlightEventDto;
import com.dac.fly.shared.dto.events.CompletedFlightEventDto;
import com.dac.fly.shared.dto.events.FlightReservationsCancelledEventDto;
import com.dac.fly.shared.dto.events.FlightReservationsCompletedEventDto;
import com.dac.fly.shared.dto.events.MilesUpdatedEvent;
import com.dac.fly.shared.dto.request.CancelFlightDto;
import com.dac.fly.shared.dto.response.CancelledFlightResponseDto;
import com.dac.fly.saga.enums.CompleteFlightSagaStep;
@Service
public class FlightSagaOrchestrator {

    private static final long TIMEOUT_SECONDS = 10;

    private final RabbitTemplate rabbit;
    private final ConcurrentHashMap<String, CompletableFuture<CancelledFlightEventDto>> flightCancelResponses;
    private final ConcurrentHashMap<String, CompletableFuture<FlightReservationsCancelledEventDto>> reservationsCancelResponses;
    private final ConcurrentHashMap<String, CompletableFuture<MilesUpdatedEvent>> milesResponses;
    private final FlightClient flightClient;

    public FlightSagaOrchestrator(
            RabbitTemplate rabbit,
            ConcurrentHashMap<String, CompletableFuture<CancelledFlightEventDto>> flightCancelResponses,
            ConcurrentHashMap<String, CompletableFuture<FlightReservationsCancelledEventDto>> reservationsCancelResponses,
            ConcurrentHashMap<String, CompletableFuture<MilesUpdatedEvent>> milesResponses, FlightClient flightClient) {
        this.rabbit = rabbit;
        this.flightCancelResponses = flightCancelResponses;
        this.reservationsCancelResponses = reservationsCancelResponses;
        this.milesResponses = milesResponses;
        this.flightClient = flightClient;
    }

    public CancelledFlightResponseDto cancelFlightSaga(String flightCode) {
        if (!flightClient.existsByCode(flightCode)) {
            throw new IllegalArgumentException("Voo não encontrado: " + flightCode);
        }

        EnumSet<FlightCancelSagaStep> completed = EnumSet.noneOf(FlightCancelSagaStep.class);
        CancelledFlightEventDto flightEvt = null;
        FlightReservationsCancelledEventDto resEvt = null;

        try {
            var flightFuture = new CompletableFuture<CancelledFlightEventDto>();
            flightCancelResponses.put(flightCode, flightFuture);
            rabbit.convertAndSend(
                    RabbitConstants.EXCHANGE,
                    RabbitConstants.CANCEL_FLIGHT_CMD_QUEUE,
                    new CancelFlightDto(flightCode));
            flightEvt = getWithTimeout(flightCancelResponses, flightCode);
            completed.add(FlightCancelSagaStep.FLIGHT_CANCELLED);

            var resFuture = new CompletableFuture<FlightReservationsCancelledEventDto>();
            reservationsCancelResponses.put(flightCode, resFuture);
            rabbit.convertAndSend(
                    RabbitConstants.EXCHANGE,
                    RabbitConstants.CANCEL_RESERVATION_BY_FLIGHT_CMD_QUEUE,
                    new CancelFlightDto(flightCode));
            resEvt = getWithTimeout(reservationsCancelResponses, flightCode);
            completed.add(FlightCancelSagaStep.RESERVATIONS_CANCELLED);

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

            completed.add(FlightCancelSagaStep.MILES_REFUNDED);

            return new CancelledFlightResponseDto(
                    flightEvt.codigo(),
                    flightEvt.data(),
                    flightEvt.valor_passagem(),
                    flightEvt.quantidade_poltronas_total(),
                    flightEvt.quantidade_poltronas_ocupadas(),
                    flightEvt.estado(),
                    flightEvt.codigo_aeroporto_origem(),
                    flightEvt.codigo_aeroporto_destino());
        } catch (AmqpException ex) {
            compensateCancelFlightSaga(flightCode, flightEvt, resEvt, completed);
            throw new RuntimeException("Erro no cancelamento de voo — iniciando compensação", ex);
        }
    }

    private void compensateCancelFlightSaga(
            String flightCode,
            CancelledFlightEventDto flightEvt,
            FlightReservationsCancelledEventDto resEvt,
            EnumSet<FlightCancelSagaStep> completed) {

        if (completed.contains(FlightCancelSagaStep.MILES_REFUNDED)) {
            for (var cm : resEvt.refunds()) {
                String key = flightCode + "-" + cm.codigoCliente();
                rabbit.convertAndSend(
                        RabbitConstants.EXCHANGE,
                        RabbitConstants.ROLLBACK_MILES_CMD_QUEUE,
                        new UpdateMilesCommand(
                                key,
                                cm.codigoCliente(),
                                cm.milhasUtilizadas(),
                                false));
            }
        }

        if (completed.contains(FlightCancelSagaStep.RESERVATIONS_CANCELLED) && resEvt != null) {
            for (var cm : resEvt.refunds()) {
                rabbit.convertAndSend(
                        RabbitConstants.EXCHANGE,
                        RabbitConstants.COMPENSATE_CANCEL_RESERVATION_CMD_QUEUE,
                        new CompensateCancelReservationCommand(
                                cm.codigoReserva(),
                                cm.estadoAnterior()));
            }
        }

        if (completed.contains(FlightCancelSagaStep.FLIGHT_CANCELLED) && flightEvt != null) {
            rabbit.convertAndSend(
                    RabbitConstants.EXCHANGE,
                    RabbitConstants.COMPENSATE_CANCEL_FLIGHT_CMD_QUEUE,
                    new CompensateCancelFlightCommand(
                            flightEvt.codigo(),
                            flightEvt.estadoAnterior()));
        }
    }

    public CompletedFlightResponseDto completeFlight(String flightCode){
        if (!flightClient.existsByCode(flightCode)) {
            throw new IllegalArgumentException("Voo não encontrado: " + flightCode);
        }

        EnumSet<CompleteFlightSagaStep> completed = EnumSet.noneOf(CompleteFlightSagaStep.class);
        CompletedFlightEventDto flightEvt = null;
        FlightReservationsCompletedEventDto resEvt = null;

        try {
            var flightFuture = new CompletableFuture<CompletedFlightEventDto>();
            flightCompletedResponses.put(flightCode, flightFuture);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private <T> T getWithTimeout(
            ConcurrentHashMap<String, CompletableFuture<T>> map,
            String key) {
        try {
            return map.get(key).get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            throw new RuntimeException("Erro/timeout na chave " + key, ex);
        }
    }
}
