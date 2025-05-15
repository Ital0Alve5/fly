package com.dac.fly.reservationservice.consumer;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.dac.fly.reservationservice.config.RabbitMQConfig;
import com.dac.fly.reservationservice.service.command.ReservationCommandService;
import com.dac.fly.shared.config.RabbitConstants;
import com.dac.fly.shared.dto.command.CompensateCreateReservationCommand;

@Component
public class CompensateCreateReservationConsumer {

    private final ReservationCommandService reservationService;
    private final RabbitTemplate rabbit;
    private final DirectExchange internalExchange;

    public CompensateCreateReservationConsumer(ReservationCommandService reservationService, RabbitTemplate rabbit,
            DirectExchange internalExchange) {
        this.reservationService = reservationService;
        this.rabbit = rabbit;
        this.internalExchange = internalExchange;
    }

    @RabbitListener(queues = RabbitConstants.COMPENSATE_CREATE_RESERVATION_CMD_QUEUE)
    public void onCompensateCreate(CompensateCreateReservationCommand cmd) {
        if (reservationService.exists(cmd.codigo_reserva())) {
            reservationService.removeReservation(cmd.codigo_reserva());

            rabbit.convertAndSend(
                    internalExchange.getName(),
                    RabbitMQConfig.INTERNAL_COMPENSATE_CREATE_KEY,
                    cmd.codigo_reserva());
        }
    }
}
