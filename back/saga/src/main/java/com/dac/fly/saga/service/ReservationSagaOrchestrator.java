package com.dac.fly.saga.service;

import java.util.EnumSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.dac.fly.saga.enums.CancelReservationSagaStep;
import com.dac.fly.saga.enums.CreateReservationSagaStep;
import com.dac.fly.shared.config.RabbitConstants;
import com.dac.fly.shared.dto.command.CancelReservationCommand;
import com.dac.fly.shared.dto.command.CompensateCancelReservationCommand;
import com.dac.fly.shared.dto.command.CompensateCreateReservationCommand;
import com.dac.fly.shared.dto.command.CreateReservationCommand;
import com.dac.fly.shared.dto.command.UpdateMilesCommand;
import com.dac.fly.shared.dto.command.UpdateSeatsCommand;
import com.dac.fly.shared.dto.events.MilesUpdatedEvent;
import com.dac.fly.shared.dto.events.SeatsUpdatedEvent;
import com.dac.fly.shared.dto.request.CreateReservationDto;
import com.dac.fly.shared.dto.response.CanceledReservationResponseDto;
import com.dac.fly.shared.dto.response.CreatedReservationResponseDto;
import com.dac.fly.shared.util.ReservationCodeGenerator;

@Service
public class ReservationSagaOrchestrator {

    private static final long TIMEOUT_SECONDS = 5;

    private final RabbitTemplate rabbit;
    private final ConcurrentHashMap<String, CompletableFuture<MilesUpdatedEvent>> milesResponses;
    private final ConcurrentHashMap<String, CompletableFuture<SeatsUpdatedEvent>> seatsResponses;
    private final ConcurrentHashMap<String, CompletableFuture<CreatedReservationResponseDto>> reservationResponses;
    private final ConcurrentHashMap<String, CompletableFuture<CanceledReservationResponseDto>> reservationCancelResponses;

    public ReservationSagaOrchestrator(
            RabbitTemplate rabbit,
            ConcurrentHashMap<String, CompletableFuture<MilesUpdatedEvent>> milesResponses,
            ConcurrentHashMap<String, CompletableFuture<SeatsUpdatedEvent>> seatsResponses,
            ConcurrentHashMap<String, CompletableFuture<CreatedReservationResponseDto>> reservationResponses,
            ConcurrentHashMap<String, CompletableFuture<CanceledReservationResponseDto>> reservationCancelResponses) {
        this.rabbit = rabbit;
        this.milesResponses = milesResponses;
        this.seatsResponses = seatsResponses;
        this.reservationResponses = reservationResponses;
        this.reservationCancelResponses = reservationCancelResponses;
    }

    public CreatedReservationResponseDto createReservation(CreateReservationDto createDto) {
        String reservationId = ReservationCodeGenerator.generateReservationCode();
        CreateReservationCommand cmd = new CreateReservationCommand(
                reservationId,
                createDto.codigo_cliente(),
                createDto.valor(),
                createDto.milhas_utilizadas(),
                createDto.quantidade_poltronas(),
                createDto.codigo_voo(),
                createDto.codigo_aeroporto_origem(),
                createDto.codigo_aeroporto_destino());

        EnumSet<CreateReservationSagaStep> completedSteps = EnumSet.noneOf(CreateReservationSagaStep.class);

        try {
            if (cmd.milhas_utilizadas() != null && cmd.milhas_utilizadas() > 0) {
                deductMiles(cmd);
                completedSteps.add(CreateReservationSagaStep.MILES_DEDUCTED);
            }

            updateSeats(cmd);
            completedSteps.add(CreateReservationSagaStep.SEATS_UPDATED);

            CreatedReservationResponseDto response = sendCreateReservation(cmd);
            completedSteps.add(CreateReservationSagaStep.RESERVATION_CREATED);

            return response;

        } catch (RuntimeException ex) {
            compensateCreateReservationSaga(cmd, completedSteps);
            throw new RuntimeException("Falha ao criar reserva. Todas as ações realizadas foram compensadas.", ex);
        } finally {
            milesResponses.remove(reservationId);
            seatsResponses.remove(reservationId);
            reservationResponses.remove(reservationId);
        }
    }

    private void deductMiles(CreateReservationCommand cmd) {
        UpdateMilesCommand milesCmd = new UpdateMilesCommand(
                cmd.codigo(),
                cmd.codigo_cliente(),
                cmd.milhas_utilizadas(),
                false);
        CompletableFuture<MilesUpdatedEvent> future = new CompletableFuture<>();
        milesResponses.put(cmd.codigo(), future);
        rabbit.convertAndSend(RabbitConstants.EXCHANGE, RabbitConstants.UPDATE_MILES_CMD_QUEUE, milesCmd);

        MilesUpdatedEvent event = getFuture(future, "dedução de milhas", cmd.codigo());
        if (!event.success()) {
            throw new RuntimeException("Falha na dedução de milhas para reserva: " + cmd.codigo());
        }
    }

    private void updateSeats(CreateReservationCommand cmd) {
        UpdateSeatsCommand seatsCmd = new UpdateSeatsCommand(
                cmd.codigo(),
                cmd.codigo_voo(),
                cmd.quantidade_poltronas(),
                false);
        CompletableFuture<SeatsUpdatedEvent> future = new CompletableFuture<>();
        seatsResponses.put(cmd.codigo(), future);
        rabbit.convertAndSend(RabbitConstants.EXCHANGE, RabbitConstants.UPDATE_SEATS_CMD_QUEUE, seatsCmd);

        SeatsUpdatedEvent event;
        try {
            event = future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            // rollback miles
            rabbit.convertAndSend(RabbitConstants.EXCHANGE, RabbitConstants.UPDATE_MILES_CMD_QUEUE,
                    new UpdateMilesCommand(cmd.codigo(), cmd.codigo_cliente(), cmd.milhas_utilizadas(), true));
            throw new RuntimeException("Timeout/erro ao reservar assentos, milhas revertidas", ex);
        }
        if (!event.success()) {
            rabbit.convertAndSend(RabbitConstants.EXCHANGE, RabbitConstants.UPDATE_MILES_CMD_QUEUE,
                    new UpdateMilesCommand(cmd.codigo(), cmd.codigo_cliente(), cmd.milhas_utilizadas(), true));
            throw new RuntimeException("Falha ao reservar assentos, milhas revertidas");
        }
    }

    private CreatedReservationResponseDto sendCreateReservation(CreateReservationCommand cmd) {
        CompletableFuture<CreatedReservationResponseDto> future = new CompletableFuture<>();
        reservationResponses.put(cmd.codigo(), future);
        rabbit.convertAndSend(RabbitConstants.EXCHANGE, RabbitConstants.CREATE_RESERVATION_CMD_QUEUE, cmd);

        try {
            return future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            throw new RuntimeException("Erro ao aguardar resposta de criação de reserva: " + cmd.codigo(), ex);
        }
    }

    public CanceledReservationResponseDto cancelReservationSaga(String codigo) {
        EnumSet<CancelReservationSagaStep> completed = EnumSet.noneOf(CancelReservationSagaStep.class);
        CanceledReservationResponseDto cancelResp = null;

        try {
            CancelReservationCommand cancelCmd = new CancelReservationCommand(codigo);
            CompletableFuture<CanceledReservationResponseDto> cancelFuture = new CompletableFuture<>();
            reservationCancelResponses.put(codigo, cancelFuture);
            rabbit.convertAndSend(RabbitConstants.EXCHANGE, RabbitConstants.CANCEL_RESERVATION_CMD_QUEUE, cancelCmd);

            cancelResp = cancelFuture.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            if (!"CANCELADA".equals(cancelResp.estado())) {
                throw new RuntimeException("Não foi possível cancelar reserva: " + codigo);
            }
            completed.add(CancelReservationSagaStep.RESERVATION_CANCELLED);

            UpdateMilesCommand refundMiles = new UpdateMilesCommand(
                    codigo,
                    cancelResp.codigo_cliente(),
                    cancelResp.milhas_utilizadas(),
                    true);
            CompletableFuture<MilesUpdatedEvent> milesFuture = new CompletableFuture<>();
            milesResponses.put(codigo, milesFuture);
            rabbit.convertAndSend(RabbitConstants.EXCHANGE, RabbitConstants.UPDATE_MILES_CMD_QUEUE, refundMiles);

            MilesUpdatedEvent milesEvt = getFuture(milesFuture, "devolução de milhas", codigo);
            if (!milesEvt.success()) {
                throw new RuntimeException("Falha ao devolver milhas para reserva: " + codigo);
            }
            completed.add(CancelReservationSagaStep.MILES_REFUNDED);

            UpdateSeatsCommand freeSeats = new UpdateSeatsCommand(
                    codigo,
                    cancelResp.codigo_voo(),
                    cancelResp.quantidade_poltronas(),
                    true);
            CompletableFuture<SeatsUpdatedEvent> seatsFuture = new CompletableFuture<>();
            seatsResponses.put(codigo, seatsFuture);
            rabbit.convertAndSend(RabbitConstants.EXCHANGE, RabbitConstants.UPDATE_SEATS_CMD_QUEUE, freeSeats);

            SeatsUpdatedEvent seatsEvt = getFuture(seatsFuture, "liberação de assentos", codigo);
            if (!seatsEvt.success()) {
                throw new RuntimeException("Falha ao liberar assentos para reserva: " + codigo);
            }
            completed.add(CancelReservationSagaStep.SEATS_FREED);

            return cancelResp;

        } catch (InterruptedException | RuntimeException | ExecutionException | TimeoutException ex) {
            compensateCancelReservationSaga(codigo, cancelResp, completed);
            throw new RuntimeException("Erro no cancelamento — iniciando compensação", ex);
        } finally {
            reservationCancelResponses.remove(codigo);
            milesResponses.remove(codigo);
            seatsResponses.remove(codigo);
        }
    }

    private void compensateCancelReservationSaga(
            String codigo,
            CanceledReservationResponseDto cancelResp,
            EnumSet<CancelReservationSagaStep> completed) {

        if (completed.contains(CancelReservationSagaStep.SEATS_FREED)) {
            UpdateSeatsCommand reserveSeats = new UpdateSeatsCommand(
                    codigo,
                    cancelResp.codigo_voo(),
                    cancelResp.quantidade_poltronas(),
                    false);
            CompletableFuture<SeatsUpdatedEvent> seatsFut = new CompletableFuture<>();
            seatsResponses.put(codigo, seatsFut);
            rabbit.convertAndSend(
                    RabbitConstants.EXCHANGE,
                    RabbitConstants.ROLLBACK_SEATS_CMD_QUEUE,
                    reserveSeats);
            SeatsUpdatedEvent seatsEvt = getFuture(seatsFut, "re-reserva de assentos", codigo);
            if (!seatsEvt.success()) {
                throw new RuntimeException("Falha ao re-reservar assentos para reserva: " + codigo);
            }
            seatsResponses.remove(codigo);
        }

        if (completed.contains(CancelReservationSagaStep.MILES_REFUNDED)) {
            UpdateMilesCommand deductMiles = new UpdateMilesCommand(
                    codigo,
                    cancelResp.codigo_cliente(),
                    cancelResp.milhas_utilizadas(),
                    false);
            CompletableFuture<MilesUpdatedEvent> milesFut = new CompletableFuture<>();
            milesResponses.put(codigo, milesFut);
            rabbit.convertAndSend(
                    RabbitConstants.EXCHANGE,
                    RabbitConstants.ROLLBACK_MILES_CMD_QUEUE,
                    deductMiles);
            MilesUpdatedEvent milesEvt = getFuture(milesFut, "dedução de milhas compensatória", codigo);
            if (!milesEvt.success()) {
                throw new RuntimeException("Falha ao deduzir milhas na compensação: " + codigo);
            }
            milesResponses.remove(codigo);
        }

        if (completed.contains(CancelReservationSagaStep.RESERVATION_CANCELLED)) {
            var compCancel = new CompensateCancelReservationCommand(
                    codigo,
                    cancelResp.estado_anterior());

            rabbit.convertAndSend(
                    RabbitConstants.EXCHANGE,
                    RabbitConstants.COMPENSATE_CANCEL_RESERVATION_CMD_QUEUE,
                    compCancel);
        }
    }

    private <T> T getFuture(CompletableFuture<T> future, String action, String codigo) {
        try {
            return future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (TimeoutException ex) {
            throw new RuntimeException("Timeout ao aguardar " + action + " para reserva: " + codigo, ex);
        } catch (InterruptedException | ExecutionException ex) {
            throw new RuntimeException("Erro ao aguardar " + action + " para reserva: " + codigo, ex);
        }
    }

    public void compensateCreateReservationSaga(
            CreateReservationCommand failedCmd,
            EnumSet<CreateReservationSagaStep> completedSteps) {
        String reservationId = failedCmd.codigo();

        if (completedSteps.contains(CreateReservationSagaStep.RESERVATION_CREATED)) {
            CompensateCreateReservationCommand compRes = new CompensateCreateReservationCommand(reservationId);
            rabbit.convertAndSend(
                    RabbitConstants.EXCHANGE,
                    RabbitConstants.COMPENSATE_CREATE_RESERVATION_CMD_QUEUE,
                    compRes);
        }

        if (completedSteps.contains(CreateReservationSagaStep.SEATS_UPDATED)) {
            UpdateSeatsCommand compSeats = new UpdateSeatsCommand(
                    reservationId,
                    failedCmd.codigo_voo(),
                    failedCmd.quantidade_poltronas(),
                    true);
            CompletableFuture<SeatsUpdatedEvent> fut = new CompletableFuture<>();
            seatsResponses.put(reservationId, fut);
            rabbit.convertAndSend(
                    RabbitConstants.EXCHANGE,
                    RabbitConstants.COMPENSATE_SEATS_CMD_QUEUE,
                    compSeats);
            SeatsUpdatedEvent evt = getFuture(fut, "compensação de assentos", reservationId);
            if (!evt.success()) {
                throw new RuntimeException("Falha ao compensar assentos da reserva: " + reservationId);
            }
            seatsResponses.remove(reservationId);
        }

        if (completedSteps.contains(CreateReservationSagaStep.MILES_DEDUCTED)) {
            UpdateMilesCommand compMiles = new UpdateMilesCommand(
                    reservationId,
                    failedCmd.codigo_cliente(),
                    failedCmd.milhas_utilizadas(),
                    true);
            CompletableFuture<MilesUpdatedEvent> fut = new CompletableFuture<>();
            milesResponses.put(reservationId, fut);
            rabbit.convertAndSend(
                    RabbitConstants.EXCHANGE,
                    RabbitConstants.COMPENSATE_MILES_CMD_QUEUE,
                    compMiles);
            MilesUpdatedEvent evt = getFuture(fut, "compensação de milhas", reservationId);
            if (!evt.success()) {
                throw new RuntimeException("Falha ao compensar milhas da reserva: " + reservationId);
            }
            milesResponses.remove(reservationId);
        }
    }
}
