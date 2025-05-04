package com.dac.fly.saga.service;

import com.dac.fly.shared.config.RabbitConstants;
import com.dac.fly.shared.dto.events.UserCreatedEventDto;
import com.dac.fly.shared.dto.request.CreateClientRequestDto;
import com.dac.fly.shared.dto.request.CreateUserRequestDto;
import com.dac.fly.shared.dto.response.ClientCreatedResponseDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

@Service
public class ClientSagaOrchestrator {

    private static final long TIMEOUT_SECONDS = 5;

    private final RabbitTemplate rabbit;
    private final ConcurrentHashMap<String, CompletableFuture<ClientCreatedResponseDto>> clientCreateResponses;
    private final ConcurrentHashMap<String, CompletableFuture<UserCreatedEventDto>> userCreateResponses;

    public ClientSagaOrchestrator(
            RabbitTemplate rabbit,
            ConcurrentHashMap<String, CompletableFuture<ClientCreatedResponseDto>> clientCreateResponses,
            ConcurrentHashMap<String, CompletableFuture<UserCreatedEventDto>> userCreateResponses) {
        this.rabbit = rabbit;
        this.clientCreateResponses = clientCreateResponses;
        this.userCreateResponses = userCreateResponses;
    }

    public void createClientSaga(CreateClientRequestDto dto) {
        CompletableFuture<ClientCreatedResponseDto> clientFuture = new CompletableFuture<>();
        clientCreateResponses.put(dto.email(), clientFuture);

        rabbit.convertAndSend(
                RabbitConstants.EXCHANGE,
                RabbitConstants.CREATE_CLIENT_CMD_QUEUE,
                dto);

        getWithTimeout(clientCreateResponses, dto.email());

        CompletableFuture<UserCreatedEventDto> userFuture = new CompletableFuture<>();
        userCreateResponses.put(dto.email(), userFuture);

        rabbit.convertAndSend(
                RabbitConstants.EXCHANGE,
                RabbitConstants.CREATE_USER_CMD_QUEUE,
                new CreateUserRequestDto(dto.email(), "CLIENTE"));

        getWithTimeout(userCreateResponses, dto.email());
        System.out.println("Getted auth service timeout");
    }

    private <T> T getWithTimeout(
            ConcurrentHashMap<String, CompletableFuture<T>> map,
            String key) {
        CompletableFuture<T> future = map.get(key);
        if (future == null) {
            throw new RuntimeException("Nenhum futuro para a chave " + key);
        }
        try {
            return future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (InterruptedException|ExecutionException|TimeoutException ex) {
            throw new RuntimeException("Erro/timeout na chave " + key, ex);
        }finally {
            map.remove(key);
        }
    }

}
