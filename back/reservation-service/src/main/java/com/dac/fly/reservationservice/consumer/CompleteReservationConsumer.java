package com.dac.fly.reservationservice.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.dac.fly.reservationservice.service.command.ReservationCommandService;
import com.dac.fly.shared.config.RabbitConstants;

@Component
public class CompleteReservationConsumer {

    private final RabbitTemplate rabbit;
    private final ReservationCommandService reservationCommandService;

    public CompleteReservationConsumer(
            RabbitTemplate rabbit,
            ReservationCommandService reservationCommandService) {
        this.rabbit = rabbit;
        this.reservationCommandService = reservationCommandService;
    }
}
