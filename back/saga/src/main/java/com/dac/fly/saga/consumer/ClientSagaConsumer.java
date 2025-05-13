package com.dac.fly.saga.consumer;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import com.dac.fly.shared.dto.events.ClientCreatedEventDto;
import com.dac.fly.shared.dto.response.ClientCreatedResponseDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.dac.fly.shared.config.RabbitConstants;

@Component
public class ClientSagaConsumer {

    private final ConcurrentHashMap<String, CompletableFuture<ClientCreatedEventDto>> createClientResponses;

    public ClientSagaConsumer(ConcurrentHashMap<String, CompletableFuture<ClientCreatedEventDto>> createClientResponses) {
        this.createClientResponses = createClientResponses;
    }

    @RabbitListener(queues = RabbitConstants.CREATE_CLIENT_RESP_QUEUE)
    public void onClientCreated(ClientCreatedEventDto evt) {
        var future = createClientResponses.remove(evt.email());
        if (future != null) {
            future.complete(evt);
        }
    }
}
