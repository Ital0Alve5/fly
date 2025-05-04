package com.dac.fly.authservice.publisher;

import com.dac.fly.shared.config.RabbitConstants;
import com.dac.fly.shared.dto.events.UserCreatedEventDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class AuthPublisher {
    private final RabbitTemplate rabbit;

    public AuthPublisher(RabbitTemplate rabbit) {
        this.rabbit = rabbit;
    }

    public void publishUserCreatedResponse(String email, boolean success) {
        System.out.println("Publishing create user response");
        rabbit.convertAndSend(
                RabbitConstants.EXCHANGE,
                RabbitConstants.CREATE_USER_RESP_QUEUE,
                new UserCreatedEventDto(email, success)
        );
    }
}
