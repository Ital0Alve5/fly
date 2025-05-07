package com.dac.fly.saga.consumer;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import com.dac.fly.shared.dto.events.UserCreatedEventDto;
import com.dac.fly.shared.dto.events.UserDeletedEventDto;
import com.dac.fly.shared.dto.events.UserUpdatedEventDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.dac.fly.shared.config.RabbitConstants;

@Component
public class AuthSagaConsumer {

    private final ConcurrentHashMap<String, CompletableFuture<UserCreatedEventDto>> userCreateResponses;
    private final ConcurrentHashMap<String, CompletableFuture<UserDeletedEventDto>> userDeleteResponses;
    private final ConcurrentHashMap<String, CompletableFuture<UserUpdatedEventDto>> userUpdateResponses;

    public AuthSagaConsumer(
            ConcurrentHashMap<String, CompletableFuture<UserCreatedEventDto>> userCreateResponses,
            ConcurrentHashMap<String, CompletableFuture<UserDeletedEventDto>> userDeleteResponses,
            ConcurrentHashMap<String, CompletableFuture<UserUpdatedEventDto>> userUpdateResponses
    ) {
        this.userCreateResponses = userCreateResponses;
        this.userDeleteResponses = userDeleteResponses;
        this.userUpdateResponses = userUpdateResponses;
    }

    @RabbitListener(queues = RabbitConstants.CREATE_USER_RESP_QUEUE)
    public void onUserCreated(UserCreatedEventDto evt) {
        var future = userCreateResponses.get(evt.email());
        if (future != null) {
            future.complete(evt);
        }
    }

    @RabbitListener(queues = RabbitConstants.UPDATE_USER_RESP_QUEUE)
    public void onUserCreated(UserUpdatedEventDto evt) {
        var future = userUpdateResponses.get(evt.email());
        if (future != null) {
            future.complete(evt);
        }
    }

    @RabbitListener(queues = RabbitConstants.DELETE_USER_RESP_QUEUE)
    public void onUserCreated(UserDeletedEventDto evt) {
        var future = userDeleteResponses.get(evt.email());
        if (future != null) {
            future.complete(evt);
        }
    }
}
