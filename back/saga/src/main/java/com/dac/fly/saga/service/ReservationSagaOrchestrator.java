package com.dac.fly.saga.service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.dac.fly.shared.config.RabbitConstants;
import com.dac.fly.shared.dto.command.CancelReservationCommand;
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

    public CreatedReservationResponseDto startSaga(CreateReservationDto createDto) {
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

        try {
            if (cmd.milhas_utilizadas() != null && cmd.milhas_utilizadas() > 0) {
                deductMiles(cmd);
            }

            updateSeats(cmd);

            return createReservation(cmd);

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
                cmd.milhas_utilizadas(), false);

        CompletableFuture<MilesUpdatedEvent> future = new CompletableFuture<>();
        milesResponses.put(cmd.codigo(), future);
        rabbit.convertAndSend(RabbitConstants.EXCHANGE, RabbitConstants.UPDATE_MILES_CMD_QUEUE, milesCmd);

        MilesUpdatedEvent event;
        try {
            event = future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (TimeoutException ex) {
            throw new RuntimeException("Timeout ao aguardar dedução de milhas para reserva: " + cmd.codigo());
        } catch (InterruptedException | ExecutionException ex) {
            throw new RuntimeException("Erro ao aguardar dedução de milhas para reserva: " + cmd.codigo(), ex);
        }

        if (!event.success()) {
            throw new RuntimeException("Falha na dedução de milhas para reserva: " + cmd.codigo());
        }
    }

    private void updateSeats(CreateReservationCommand cmd) {
        UpdateSeatsCommand seatsCmd = new UpdateSeatsCommand(
                cmd.codigo(),
                cmd.codigo_voo(),
                cmd.quantidade_poltronas(), false);

        CompletableFuture<SeatsUpdatedEvent> future = new CompletableFuture<>();
        seatsResponses.put(cmd.codigo(), future);
        rabbit.convertAndSend(RabbitConstants.EXCHANGE, RabbitConstants.UPDATE_SEATS_CMD_QUEUE, seatsCmd);

        SeatsUpdatedEvent event;
        try {
            event = future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
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

    private CreatedReservationResponseDto createReservation(CreateReservationCommand cmd) {
        CompletableFuture<CreatedReservationResponseDto> future = new CompletableFuture<>();
        reservationResponses.put(cmd.codigo(), future);
        rabbit.convertAndSend(RabbitConstants.EXCHANGE, RabbitConstants.CREATE_RESERVATION_CMD_QUEUE, cmd);

        CreatedReservationResponseDto response;
        try {
            response = future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (TimeoutException ex) {
            throw new RuntimeException("Timeout ao aguardar criação de reserva: " + cmd.codigo());
        } catch (InterruptedException | ExecutionException ex) {
            throw new RuntimeException("Erro ao aguardar resposta de criação de reserva: " + cmd.codigo(), ex);
        }

        return response;
    }

    public CanceledReservationResponseDto cancelReservationSaga(String codigo) {
        try {
            CancelReservationCommand cancelCmd = new CancelReservationCommand(codigo);
            CompletableFuture<CanceledReservationResponseDto> cancelFuture = new CompletableFuture<>();
            reservationCancelResponses.put(codigo, cancelFuture);

            rabbit.convertAndSend(RabbitConstants.EXCHANGE, RabbitConstants.CANCEL_RESERVATION_CMD_QUEUE, cancelCmd);

            CanceledReservationResponseDto cancelResp;
            try {
                cancelResp = cancelFuture.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            } catch (TimeoutException ex) {
                throw new RuntimeException("Timeout ao aguardar cancelamento da reserva: " + codigo, ex);
            } catch (InterruptedException | ExecutionException ex) {
                throw new RuntimeException("Erro ao aguardar cancelamento da reserva: " + codigo, ex);
            }

            if (!"CANCELADA".equals(cancelResp.estado())) {
                throw new RuntimeException("Não foi possível cancelar reserva: " + codigo);
            }

            UpdateMilesCommand compMiles = new UpdateMilesCommand(
                    codigo,
                    cancelResp.codigo_cliente(),
                    cancelResp.milhas_utilizadas(), true);

            CompletableFuture<MilesUpdatedEvent> milesFuture = new CompletableFuture<>();
            milesResponses.put(codigo, milesFuture);

            rabbit.convertAndSend(
                    RabbitConstants.EXCHANGE,
                    RabbitConstants.UPDATE_MILES_CMD_QUEUE,
                    compMiles);

            MilesUpdatedEvent milesEvt;
            try {
                milesEvt = milesFuture.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            } catch (TimeoutException ex) {
                throw new RuntimeException("Timeout ao aguardar devolução de milhas para reserva: " + codigo, ex);
            } catch (InterruptedException | ExecutionException ex) {
                throw new RuntimeException("Erro ao aguardar devolução de milhas para reserva: " + codigo, ex);
            }
            if (!milesEvt.success()) {
                throw new RuntimeException("Falha ao devolver milhas para reserva: " + codigo);
            }

            UpdateSeatsCommand compSeats = new UpdateSeatsCommand(
                    codigo,
                    cancelResp.codigo_voo(),
                    cancelResp.quantidade_poltronas(), true);

            CompletableFuture<SeatsUpdatedEvent> seatsFuture = new CompletableFuture<>();
            seatsResponses.put(codigo, seatsFuture);

            rabbit.convertAndSend(
                    RabbitConstants.EXCHANGE,
                    RabbitConstants.UPDATE_SEATS_CMD_QUEUE,
                    compSeats);

            SeatsUpdatedEvent seatsEvt;
            try {
                seatsEvt = seatsFuture.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            } catch (TimeoutException ex) {
                throw new RuntimeException("Timeout ao aguardar liberação de assentos para reserva: " + codigo, ex);
            } catch (InterruptedException | ExecutionException ex) {
                throw new RuntimeException("Erro ao aguardar liberação de assentos para reserva: " + codigo, ex);
            }
            if (!seatsEvt.success()) {
                throw new RuntimeException("Falha ao liberar assentos para reserva: " + codigo);
            }

            return cancelResp;

        } finally {
            reservationCancelResponses.remove(codigo);
            milesResponses.remove(codigo);
            seatsResponses.remove(codigo);
        }
    }

    public void compensateCreateReservationSaga(CreateReservationCommand failedCmd) {
        String reservationId = failedCmd.codigo();

        if (failedCmd.quantidade_poltronas() > 0) {
            UpdateSeatsCommand compensateSeatsCmd = new UpdateSeatsCommand(
                    reservationId,
                    failedCmd.codigo_voo(),
                    failedCmd.quantidade_poltronas(),
                    true);

            CompletableFuture<SeatsUpdatedEvent> seatsFuture = new CompletableFuture<>();
            seatsResponses.put(reservationId, seatsFuture);

            rabbit.convertAndSend(RabbitConstants.EXCHANGE, RabbitConstants.UPDATE_SEATS_CMD_QUEUE, compensateSeatsCmd);

            try {
                SeatsUpdatedEvent seatsEvt = seatsFuture.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
                if (!seatsEvt.success()) {
                    throw new RuntimeException("Falha ao compensar assentos da reserva: " + reservationId);
                }
            } catch (InterruptedException | ExecutionException | TimeoutException ex) {
                throw new RuntimeException("Erro ou timeout ao compensar assentos da reserva: " + reservationId, ex);
            } finally {
                seatsResponses.remove(reservationId);
            }
        }

        if (failedCmd.milhas_utilizadas() != null && failedCmd.milhas_utilizadas() > 0) {
            UpdateMilesCommand compensateMilesCmd = new UpdateMilesCommand(
                    reservationId,
                    failedCmd.codigo_cliente(),
                    failedCmd.milhas_utilizadas(),
                    true);

            CompletableFuture<MilesUpdatedEvent> milesFuture = new CompletableFuture<>();
            milesResponses.put(reservationId, milesFuture);

            rabbit.convertAndSend(RabbitConstants.EXCHANGE, RabbitConstants.UPDATE_MILES_CMD_QUEUE, compensateMilesCmd);

            try {
                MilesUpdatedEvent milesEvt = milesFuture.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
                if (!milesEvt.success()) {
                    throw new RuntimeException("Falha ao compensar milhas da reserva: " + reservationId);
                }
            } catch (InterruptedException | ExecutionException | TimeoutException ex) {
                throw new RuntimeException("Erro ou timeout ao compensar milhas da reserva: " + reservationId, ex);
            } finally {
                milesResponses.remove(reservationId);
            }
        }

        CompensateCreateReservationCommand compensateReservationCmd = new CompensateCreateReservationCommand(
                reservationId);
        rabbit.convertAndSend(RabbitConstants.EXCHANGE,
                RabbitConstants.COMPENSATE_CREATE_RESERVATION_CMD_QUEUE,
                compensateReservationCmd);
    }

}
