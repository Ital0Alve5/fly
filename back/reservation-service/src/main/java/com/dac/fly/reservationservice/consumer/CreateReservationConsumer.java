package com.dac.fly.reservationservice.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.dac.fly.reservationservice.service.command.ReservationCommandService;
import com.dac.fly.shared.config.RabbitConstants;
import com.dac.fly.shared.dto.command.CreateReservationCommand;
import com.dac.fly.shared.dto.response.CreatedReservationResponseDto;

@Component
public class CreateReservationConsumer {

    private final RabbitTemplate rabbit;
    private final ReservationCommandService reservationCommandService;

    public CreateReservationConsumer(
            RabbitTemplate rabbit, ReservationCommandService reservationCommandService) {
        this.reservationCommandService = reservationCommandService;
        this.rabbit = rabbit;
    }

    @RabbitListener(queues = RabbitConstants.RES_QUEUE)
    public void handle(CreateReservationCommand cmd) {
        CreatedReservationResponseDto response = reservationCommandService.createReservation(cmd);
        
        rabbit.convertAndSend(
                RabbitConstants.EXCHANGE,
                RabbitConstants.CREATED_QUEUE,
                response);
    }
}
