package com.dac.fly.reservationservice.consumer;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.dac.fly.reservationservice.config.RabbitMQConfig;
import com.dac.fly.reservationservice.service.command.ReservationCommandService;
import com.dac.fly.shared.config.RabbitConstants;
import com.dac.fly.shared.dto.command.CreateReservationCommand;
import com.dac.fly.shared.dto.response.CreatedReservationResponseDto;

@Component
public class CreateReservationConsumer {

    private final RabbitTemplate rabbit;
    private final DirectExchange internalExchange;
    private final ReservationCommandService reservationService;

    public CreateReservationConsumer(
            RabbitTemplate rabbit,
            @Qualifier("internalExchange") DirectExchange internalExchange,
            ReservationCommandService reservationService) {
        this.rabbit = rabbit;
        this.internalExchange = internalExchange;
        this.reservationService = reservationService;
    }

    @RabbitListener(queues = RabbitConstants.CREATE_RESERVATION_CMD_QUEUE)
    public void handle(CreateReservationCommand cmd) {
        try {
            CreatedReservationResponseDto response = reservationService.createReservation(cmd);

            rabbit.convertAndSend(
                    RabbitConstants.EXCHANGE,
                    RabbitConstants.CREATED_QUEUE,
                    response);

            rabbit.convertAndSend(
                    internalExchange.getName(),
                    RabbitMQConfig.INTERNAL_CREATED_KEY,
                    response);

        } catch (RuntimeException e) {
            System.err.println("Erro ao criar a reserva: " + e.getMessage());

            throw e;
        }
    }
}
