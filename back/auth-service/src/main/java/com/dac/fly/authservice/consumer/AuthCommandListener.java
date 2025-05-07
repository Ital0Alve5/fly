package com.dac.fly.authservice.consumer;

import com.dac.fly.authservice.publisher.AuthPublisher;
import com.dac.fly.authservice.service.AuthService;
import com.dac.fly.shared.config.RabbitConstants;
import com.dac.fly.shared.dto.command.CreateUserCommandDto;
import com.dac.fly.shared.dto.command.DeleteUserCommandDto;
import com.dac.fly.shared.dto.command.UpdateUserCommandDto;
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
        System.out.println("Received create user event");
        boolean success = true;
        authService.registerUser(cmd);
        publisher.publishUserCreatedResponse(cmd.email(), success);
    }


    @RabbitListener(queues = RabbitConstants.UPDATE_USER_CMD_QUEUE)
    public void handleCreateUser(UpdateUserCommandDto cmd) {
        boolean success = true;
        authService.updateUser(cmd);
        publisher.publishUserUpdatedResponse(cmd.codigoExterno(), cmd.email(), success);
    }

    @RabbitListener(queues = RabbitConstants.DELETE_USER_CMD_QUEUE)
    public void handleCreateUser(DeleteUserCommandDto cmd) {
        boolean success = true;
        authService.deleteUser(cmd.email());
        publisher.publishUserDeleteResponse(cmd.email(), success);
    }
}
