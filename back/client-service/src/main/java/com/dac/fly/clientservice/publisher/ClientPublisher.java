package com.dac.fly.clientservice.publisher;

import com.dac.fly.shared.config.RabbitConstants;
import com.dac.fly.shared.dto.events.ClientCreatedEventDto;
import com.dac.fly.shared.dto.response.ClientCreatedResponseDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ClientPublisher {

    private final RabbitTemplate rabbit;

    public ClientPublisher(RabbitTemplate rabbit) {
        this.rabbit = rabbit;
    }

    public void publishClientCreatedResponse(String email, boolean success) {
        rabbit.convertAndSend(
                RabbitConstants.EXCHANGE,
                RabbitConstants.CREATE_CLIENT_RESP_QUEUE,
                new ClientCreatedResponseDto(email, success)
        );
    }
}