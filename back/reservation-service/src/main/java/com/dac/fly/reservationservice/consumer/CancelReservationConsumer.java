package com.dac.fly.reservationservice.consumer;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.dac.fly.reservationservice.config.RabbitMQConfig;
import com.dac.fly.reservationservice.entity.command.Reserva;
import com.dac.fly.reservationservice.enums.ReservationStatusEnum;
import com.dac.fly.reservationservice.repository.command.ReservaCommandRepository;
import com.dac.fly.reservationservice.repository.query.ReservaQueryRepository;
import com.dac.fly.reservationservice.service.command.ReservationCommandService;
import com.dac.fly.shared.config.RabbitConstants;
import com.dac.fly.shared.dto.command.CancelReservationCommand;
import com.dac.fly.shared.dto.command.CompensateCancelReservationCommand;
import com.dac.fly.shared.dto.response.CanceledReservationResponseDto;

@Component
public class CancelReservationConsumer {

    private final RabbitTemplate rabbit;
    private final DirectExchange internalExchange;
    private final ReservationCommandService reservationCommandService;
    private final ReservaQueryRepository reservaQueryRepository;
    private final ReservaCommandRepository reservaCommandRepository;

    public CancelReservationConsumer(
            RabbitTemplate rabbit,
            DirectExchange internalExchange,
            ReservationCommandService reservationCommandService,
            ReservaQueryRepository reservaQueryRepository, ReservaCommandRepository reservaCommandRepository) {
        this.rabbit = rabbit;
        this.internalExchange = internalExchange;
        this.reservationCommandService = reservationCommandService;
        this.reservaQueryRepository = reservaQueryRepository;
        this.reservaCommandRepository = reservaCommandRepository;
    }

    @RabbitListener(queues = RabbitConstants.CANCEL_RESERVATION_CMD_QUEUE)
    public void handle(CancelReservationCommand cmd) {
        CanceledReservationResponseDto resp = reservationCommandService.cancelReservationByCode(cmd.codigo());

        // publica internamente para o CQRS (query side)
        rabbit.convertAndSend(
                internalExchange.getName(),
                RabbitMQConfig.INTERNAL_CANCELLED_KEY,
                resp);

        // publica resposta da saga
        rabbit.convertAndSend(
                RabbitConstants.EXCHANGE,
                RabbitConstants.CANCELED_RESERVATION_RESP_QUEUE,
                resp);
    }

    @RabbitListener(queues = RabbitConstants.COMPENSATE_CANCEL_RESERVATION_CMD_QUEUE)
    public void handle(CompensateCancelReservationCommand cmd) {
        reservationCommandService.revertReservationStatus(
                cmd.codigoReserva(),
                cmd.estadoAnterior());

        Reserva reservaCmd = reservaCommandRepository
                .findById(cmd.codigoReserva())
                .orElseThrow(() -> new RuntimeException("Reserva (command) não encontrada: " + cmd.codigoReserva()));

        com.dac.fly.reservationservice.entity.query.Reserva reservaQuery = reservaQueryRepository
                .findById(cmd.codigoReserva())
                .orElseThrow(() -> new RuntimeException("Reserva (query) não encontrada: " + cmd.codigoReserva()));

        var resp = new CanceledReservationResponseDto(
                reservaCmd.getCodigo(),
                reservaCmd.getDataReserva(), 
                reservaCmd.getValorPago(), 
                reservaCmd.getMilhasUtilizadas(), 
                reservaCmd.getQuantidadePoltronas(), 
                reservaCmd.getCodigoCliente(), 
                cmd.estadoAnterior(), 
                ReservationStatusEnum.CANCELADA.toString(), 
                reservaCmd.getCodigoVoo(), 
                reservaQuery.getAeroportoOrigem(),
                reservaQuery.getAeroportoDestino() 
        );

        rabbit.convertAndSend(
                internalExchange.getName(),
                RabbitMQConfig.INTERNAL_CANCELLED_KEY,
                resp);

        rabbit.convertAndSend(
                RabbitConstants.EXCHANGE,
                RabbitConstants.CANCELED_RESERVATION_RESP_QUEUE,
                resp);

    }
}
