package com.dac.fly.authservice.publisher;

import com.dac.fly.shared.config.RabbitConstants;
import com.dac.fly.shared.dto.events.UserCreatedEventDto;
import com.dac.fly.shared.dto.events.UserDeletedEventDto;
import com.dac.fly.shared.dto.events.UserUpdatedEventDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class AuthPublisher {
    private final RabbitTemplate rabbit;

    public AuthPublisher(RabbitTemplate rabbit) {
        this.rabbit = rabbit;
    }

    public void publishUserCreatedResponse(String email, boolean success) {
        rabbit.convertAndSend(
                RabbitConstants.EXCHANGE,
                RabbitConstants.CREATE_USER_RESP_QUEUE,
                new UserCreatedEventDto(email, success)
        );
    }

    public void publishUserUpdatedResponse(String email, boolean success) {
        rabbit.convertAndSend(
                RabbitConstants.EXCHANGE,
                RabbitConstants.UPDATE_USER_RESP_QUEUE,
                new UserUpdatedEventDto(email, success)
        );
    }

    public void publishUserDeleteResponse(String email, boolean success) {
        rabbit.convertAndSend(
                RabbitConstants.EXCHANGE,
                RabbitConstants.DELETE_USER_RESP_QUEUE,
                new UserDeletedEventDto(email, success)
        );
    }
}
