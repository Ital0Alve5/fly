package com.dac.fly.authservice.consumer;

import com.dac.fly.authservice.publisher.AuthPublisher;
import com.dac.fly.shared.config.RabbitConstants;
import com.dac.fly.shared.dto.request.CreateClientRequestDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthCommandListener {

    private final AuthPublisher publisher;

    public AuthCommandListener(AuthPublisher publisher) {
        this.publisher = publisher;
    }

    @RabbitListener(queues = RabbitConstants.CREATE_USER_CMD_QUEUE)
    public void handleCreateUser(CreateClientRequestDto cmd) {
        boolean success = true;
        publisher.publishUserCreatedResponse(cmd.email(), success);
    }
}
