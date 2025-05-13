package com.dac.fly.saga.service;

import com.dac.fly.saga.feign.AuthClient;
import com.dac.fly.saga.feign.ClientClient;
import com.dac.fly.shared.config.RabbitConstants;
import com.dac.fly.shared.dto.command.CreateClientCommandDto;
import com.dac.fly.shared.dto.command.CreateUserCommandDto;
import com.dac.fly.shared.dto.events.ClientCreatedEventDto;
import com.dac.fly.shared.dto.events.UserCreatedEventDto;
import com.dac.fly.shared.dto.request.CreateClientRequestDto;
import com.dac.fly.shared.dto.response.AuthDTO;
import com.dac.fly.shared.dto.response.ClientCreatedResponseDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;


import java.util.Objects;
import java.util.concurrent.*;

@Service
public class ClientSagaOrchestrator {

    private static final long TIMEOUT_SECONDS = 5;

    private final RabbitTemplate rabbit;
    private final ConcurrentHashMap<String, CompletableFuture<ClientCreatedEventDto>> clientCreateResponses;
    private final ConcurrentHashMap<String, CompletableFuture<UserCreatedEventDto>> userCreateResponses;
    private final AuthClient authClient;
    private final ClientClient clientClient;

    public ClientSagaOrchestrator(
            RabbitTemplate rabbit,
            ConcurrentHashMap<String, CompletableFuture<ClientCreatedEventDto>> clientCreateResponses,
            ConcurrentHashMap<String, CompletableFuture<UserCreatedEventDto>> userCreateResponses,
            AuthClient authClient, ClientClient clientClient
    ) {
        this.rabbit = rabbit;
        this.clientCreateResponses = clientCreateResponses;
        this.userCreateResponses = userCreateResponses;
        this.authClient = authClient;
        this.clientClient = clientClient;
    }

    public ClientCreatedResponseDto createClientSaga(CreateClientRequestDto dto) {
        AuthDTO emailExists = authClient.findUserByEmail(dto.email());
        if (Objects.nonNull(emailExists)) {
            throw new IllegalArgumentException(
                    "E-mail já cadastrado"
            );
        }

        if(clientClient.clientExistsByCpf(dto.cpf())){
            throw new IllegalArgumentException(
                    "CPF já cadastrado"
            );
        }

        CompletableFuture<ClientCreatedEventDto> clientFuture = new CompletableFuture<>();
        clientCreateResponses.put(dto.email(), clientFuture);
        
        rabbit.convertAndSend(
                RabbitConstants.EXCHANGE,
                RabbitConstants.CREATE_CLIENT_CMD_QUEUE,
                new CreateClientCommandDto(
                        dto.cpf(),
                        dto.email(),
                        dto.nome(),
                        dto.saldoMilhas(),
                        new CreateClientCommandDto.AddressDTO(
                                dto.endereco().cep(),
                                dto.endereco().uf(),
                                dto.endereco().cidade(),
                                dto.endereco().bairro(),
                                dto.endereco().rua(),
                                dto.endereco().numero(),
                                dto.endereco().complemento()
                        )
                )
        );

        ClientCreatedEventDto response = getWithTimeout(clientCreateResponses, dto.email());

        CompletableFuture<UserCreatedEventDto> userFuture = new CompletableFuture<>();
        userCreateResponses.put(dto.email(), userFuture);
        rabbit.convertAndSend(
                RabbitConstants.EXCHANGE,
                RabbitConstants.CREATE_USER_CMD_QUEUE,
                new CreateUserCommandDto(dto.nome(), dto.email(), response.codigo(), "CLIENTE", null));

        getWithTimeout(userCreateResponses, dto.email());

        return new ClientCreatedResponseDto(
               response.codigo(),
                dto.cpf(),
                dto.email(),
                dto.nome(),
                dto.saldoMilhas(),
                new ClientCreatedResponseDto.AddressDTO(
                        dto.endereco().cep(),
                        dto.endereco().uf(),
                        dto.endereco().cidade(),
                        dto.endereco().bairro(),
                        dto.endereco().rua(),
                        dto.endereco().numero(),
                        dto.endereco().complemento()
                )
        );
    }


    private <T> T getWithTimeout(
            ConcurrentHashMap<String, CompletableFuture<T>> map,
            String key) {
        CompletableFuture<T> future = map.get(key);
        try {
            return future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            throw new RuntimeException("Erro/timeout na chave " + key, ex);
        } finally {
            map.remove(key);
        }
    }
}
