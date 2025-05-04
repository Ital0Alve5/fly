package com.dac.fly.saga.service;

import com.dac.fly.shared.config.RabbitConstants;
import com.dac.fly.shared.dto.command.CreateClientCommandDto;
import com.dac.fly.shared.dto.command.CreateEmployeeCommandDto;
import com.dac.fly.shared.dto.command.CreateUserCommandDto;
import com.dac.fly.shared.dto.events.*;
import com.dac.fly.shared.dto.request.CreateClientRequestDto;
import com.dac.fly.shared.dto.request.CreateNewEmployeeDto;
import com.dac.fly.shared.dto.response.ClientCreatedResponseDto;
import com.dac.fly.shared.dto.response.EmployeeDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

@Service
public class EmployeeSagaOrchestrator {

    private static final long TIMEOUT_SECONDS = 5;

    private final RabbitTemplate rabbit;
    private final ConcurrentHashMap<String, CompletableFuture<EmployeeCreatedEventDto>> employeeCreateResponses;
    private final ConcurrentHashMap<String, CompletableFuture<UserCreatedEventDto>> userCreateResponses;
    private final ConcurrentHashMap<String, CompletableFuture<EmployeeUpdatedEventDto>> employeeUpdatedResponses;
    private final ConcurrentHashMap<String, CompletableFuture<UserUpdatedEventDto>> userUpdateResponses;
    private final ConcurrentHashMap<String, CompletableFuture<EmployeeDeletedEventDto>> employeeDeleteResponses;
    private final ConcurrentHashMap<String, CompletableFuture<UserDeletedEventDto>> userDeleteResponses;

    public EmployeeSagaOrchestrator(
            RabbitTemplate rabbit,
            ConcurrentHashMap<String, CompletableFuture<EmployeeCreatedEventDto>> employeeCreateResponses,
            ConcurrentHashMap<String, CompletableFuture<UserCreatedEventDto>> userCreateResponses,
            ConcurrentHashMap<String, CompletableFuture<EmployeeUpdatedEventDto>> employeeUpdatedResponses,
            ConcurrentHashMap<String, CompletableFuture<UserUpdatedEventDto>> userUpdateResponses,
            ConcurrentHashMap<String, CompletableFuture<EmployeeDeletedEventDto>> employeeDeleteResponses,
            ConcurrentHashMap<String, CompletableFuture<UserDeletedEventDto>> userDeleteResponses
    ) {
        this.rabbit = rabbit;
        this.employeeCreateResponses = employeeCreateResponses;
        this.userCreateResponses = userCreateResponses;
        this.employeeUpdatedResponses = employeeUpdatedResponses;
        this.userUpdateResponses = userUpdateResponses;
        this.employeeDeleteResponses = employeeDeleteResponses;
        this.userDeleteResponses = userDeleteResponses;
    }

    public EmployeeDto createEmployeeSaga(CreateNewEmployeeDto dto) {
        CompletableFuture<EmployeeCreatedEventDto> employeeFuture = new CompletableFuture<>();
        employeeCreateResponses.put(dto.email(), employeeFuture);

        System.out.println("Publiishin create employee");
        rabbit.convertAndSend(
                RabbitConstants.EXCHANGE,
                RabbitConstants.CREATE_EMPLOYEE_CMD_QUEUE,
                new CreateEmployeeCommandDto(
                        dto.cpf(),
                        dto.email(),
                        dto.nome(),
                        dto.telefone()
                )
        );

        EmployeeCreatedEventDto employeeCreated = getWithTimeout(employeeCreateResponses, dto.email());
        System.out.println("Received employee created");

        CompletableFuture<UserCreatedEventDto> userFuture = new CompletableFuture<>();
        userCreateResponses.put(dto.email(), userFuture);

        rabbit.convertAndSend(
                RabbitConstants.EXCHANGE,
                RabbitConstants.CREATE_USER_CMD_QUEUE,
                new CreateUserCommandDto(dto.nome(), dto.email(), "FUNCIONARIO", dto.senha()));

        System.out.println("Publishing creating user");

        getWithTimeout(userCreateResponses, dto.email());

        return new EmployeeDto(employeeCreated.codigo(), dto.cpf(), dto.email(), dto.nome(), dto.telefone());
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
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            throw new RuntimeException("Erro/timeout na chave " + key, ex);
        } finally {
            map.remove(key);
        }
    }

}
