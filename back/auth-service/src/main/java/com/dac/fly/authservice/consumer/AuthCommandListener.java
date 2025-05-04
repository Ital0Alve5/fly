package com.dac.fly.authservice.consumer;

import com.dac.fly.authservice.publisher.AuthPublisher;
import com.dac.fly.authservice.service.AuthService;
import com.dac.fly.shared.config.RabbitConstants;
import com.dac.fly.shared.dto.command.CreateUserCommandDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthCommandListener {

    private final AuthPublisher publisher;
    private final AuthService authService;

    public AuthCommandListener(AuthPublisher publisher, AuthService authService) {
        this.publisher = publisher;
        this.authService = authService;
    }

    @RabbitListener(queues = RabbitConstants.CREATE_USER_CMD_QUEUE)
    public void handleCreateUser(CreateUserCommandDto cmd) {
        System.out.println("Received create user command on auth");
        boolean success = true;
        authService.registerUser(cmd);
        publisher.publishUserCreatedResponse(cmd.email(), success);
    }
}
