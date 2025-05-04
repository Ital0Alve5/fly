package com.dac.fly.saga.consumer;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import com.dac.fly.shared.dto.events.UserCreatedEventDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.dac.fly.shared.config.RabbitConstants;

@Component
public class AuthSagaConsumer {

    private final ConcurrentHashMap<String, CompletableFuture<UserCreatedEventDto>> userCreateResponses;

    public AuthSagaConsumer(ConcurrentHashMap<String, CompletableFuture<UserCreatedEventDto>> userCreateResponses) {
        this.userCreateResponses = userCreateResponses;
    }

    @RabbitListener(queues = RabbitConstants.CREATE_USER_RESP_QUEUE)
    public void onUserCreated(UserCreatedEventDto evt) {
        System.out.println("Received user created resp on saga" + evt.email());
        var future = userCreateResponses.get(evt.email());
        if (future != null) {
            future.complete(evt);
        }
    }
}
