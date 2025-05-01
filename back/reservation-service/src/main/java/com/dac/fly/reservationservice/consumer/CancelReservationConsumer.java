package com.dac.fly.reservationservice.consumer;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.dac.fly.reservationservice.config.RabbitMQConfig;
import com.dac.fly.reservationservice.service.command.ReservationCommandService;
import com.dac.fly.shared.config.RabbitConstants;
import com.dac.fly.shared.dto.command.CancelReservationCommand;
import com.dac.fly.shared.dto.response.CanceledReservationResponseDto;

@Component
public class CancelReservationConsumer {

    private final RabbitTemplate rabbit;
    private final DirectExchange internalExchange;
    private final ReservationCommandService reservationCommandService;

    public CancelReservationConsumer(
            RabbitTemplate rabbit,
            @Qualifier("internalExchange") DirectExchange internalExchange,
            ReservationCommandService reservationCommandService) {
        this.rabbit = rabbit;
        this.reservationCommandService = reservationCommandService;
        this.internalExchange = internalExchange;
    }

    @RabbitListener(queues = RabbitConstants.CANCEL_RESERVATION_CMD_QUEUE)
    public void handle(CancelReservationCommand cmd) {
        CanceledReservationResponseDto resp = reservationCommandService.cancelReservationByCode(cmd.codigo());

        rabbit.convertAndSend(
                internalExchange.getName(),
                RabbitMQConfig.IN_QUEUE_CANCELADA,
                resp);

        rabbit.convertAndSend(
                RabbitConstants.EXCHANGE,
                RabbitConstants.CANCELED_RESERVATION_RESP_QUEUE,
                resp);

    }
}
