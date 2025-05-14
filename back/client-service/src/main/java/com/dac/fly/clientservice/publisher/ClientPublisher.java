package com.dac.fly.clientservice.publisher;

import com.dac.fly.shared.dto.events.ClientCreatedEventDto;
import com.dac.fly.shared.dto.events.ClientDeletedEventDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.dac.fly.shared.config.RabbitConstants;

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
                new ClientCreatedEventDto(codigo, email, success)
        );
    }

    public void publishClientDeletedResponse(Long codigo, String email, boolean success) {
        rabbit.convertAndSend(
                RabbitConstants.EXCHANGE,
                RabbitConstants.DELETE_CLIENT_RESP_QUEUE,
                new ClientDeletedEventDto(codigo, email, success)
        );
    }
}