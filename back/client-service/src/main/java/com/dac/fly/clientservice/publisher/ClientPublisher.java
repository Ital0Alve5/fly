package com.dac.fly.clientservice.publisher;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.dac.fly.shared.config.RabbitConstants;
import com.dac.fly.shared.dto.response.ClientCreatedResponseDto;

@Component
public class ClientPublisher {

    private final RabbitTemplate rabbit;

    public ClientPublisher(RabbitTemplate rabbit) {
        this.rabbit = rabbit;
    }

    public void publishClientCreatedResponse(Long codigo, String email, boolean success) {
        rabbit.convertAndSend(
                RabbitConstants.EXCHANGE,
                RabbitConstants.CREATE_CLIENT_RESP_QUEUE,
                new ClientCreatedResponseDto(codigo, email, success)
        );
    }
}