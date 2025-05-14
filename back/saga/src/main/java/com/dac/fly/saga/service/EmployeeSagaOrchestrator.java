package com.dac.fly.saga.service;

import com.dac.fly.saga.feign.AuthClient;
import com.dac.fly.saga.feign.EmployeeClient;
import com.dac.fly.shared.config.RabbitConstants;
import com.dac.fly.shared.dto.command.*;
import com.dac.fly.shared.dto.events.*;
import com.dac.fly.shared.dto.request.CreateNewEmployeeDto;
import com.dac.fly.shared.dto.request.UpdateEmployeeDto;
import com.dac.fly.shared.dto.response.AuthDTO;
import com.dac.fly.shared.dto.response.EmployeeDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
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
    private final EmployeeClient employeeClient;
    private final AuthClient authClient;

    public EmployeeSagaOrchestrator(
            RabbitTemplate rabbit,
            ConcurrentHashMap<String, CompletableFuture<EmployeeCreatedEventDto>> employeeCreateResponses,
            ConcurrentHashMap<String, CompletableFuture<UserCreatedEventDto>> userCreateResponses,
            ConcurrentHashMap<String, CompletableFuture<EmployeeUpdatedEventDto>> employeeUpdatedResponses,
            ConcurrentHashMap<String, CompletableFuture<UserUpdatedEventDto>> userUpdateResponses,
            ConcurrentHashMap<String, CompletableFuture<EmployeeDeletedEventDto>> employeeDeleteResponses,
            ConcurrentHashMap<String, CompletableFuture<UserDeletedEventDto>> userDeleteResponses,
            EmployeeClient employeeClient, AuthClient authClient
    ) {
        this.rabbit = rabbit;
        this.employeeCreateResponses = employeeCreateResponses;
        this.userCreateResponses = userCreateResponses;
        this.employeeUpdatedResponses = employeeUpdatedResponses;
        this.userUpdateResponses = userUpdateResponses;
        this.employeeDeleteResponses = employeeDeleteResponses;
        this.userDeleteResponses = userDeleteResponses;
        this.employeeClient = employeeClient;
        this.authClient = authClient;
    }

    public EmployeeDto createEmployeeSaga(CreateNewEmployeeDto dto) {
        AuthDTO existsByEmail = authClient.findUserByEmail(dto.email());
        if(Objects.nonNull(existsByEmail)) {
            throw new IllegalArgumentException(
                    "E-mail já cadastrado"
            );
        }

        EmployeeDto existsByCpf = employeeClient.findEmployeeByCpf(dto.cpf());
        if(Objects.nonNull(existsByCpf)) {
            throw new IllegalArgumentException(
                    "CPF já cadastrado"
            );
        }

        CompletableFuture<EmployeeCreatedEventDto> employeeFuture = new CompletableFuture<>();
        employeeCreateResponses.put(dto.email(), employeeFuture);

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

        CompletableFuture<UserCreatedEventDto> userFuture = new CompletableFuture<>();
        userCreateResponses.put(dto.email(), userFuture);

        rabbit.convertAndSend(
                RabbitConstants.EXCHANGE,
                RabbitConstants.CREATE_USER_CMD_QUEUE,
                new CreateUserCommandDto(dto.nome(), dto.email(), employeeCreated.codigo(), "FUNCIONARIO", dto.senha()));

        getWithTimeout(userCreateResponses, dto.email());

        return new EmployeeDto(employeeCreated.codigo(), dto.cpf(), dto.email(), dto.nome(), dto.telefone());
    }

    public EmployeeDto updateEmployeeSaga(UpdateEmployeeDto dto) {
        EmployeeDto existsByCode = employeeClient.findEmployeeByCode(dto.codigo());
        if(Objects.isNull(existsByCode)) {
            throw new IllegalArgumentException(
                    "Funcionario não encontrado"
            );
        }

        AuthDTO emailExists = authClient.findUserByEmail(dto.email());
        if(Objects.nonNull(emailExists) && !emailExists.codigoExterno().equals(dto.codigo())) {
            throw new IllegalArgumentException(
                    "Email já cadastrado"
            );
        }

        EmployeeDto existsByCpf = employeeClient.findEmployeeByCpf(dto.cpf());
        if(Objects.nonNull(existsByCpf) && !existsByCpf.codigo().equals(dto.codigo())) {
            throw new IllegalArgumentException(
                    "CPF já cadastrado"
            );
        }

        CompletableFuture<EmployeeUpdatedEventDto> employeeFuture = new CompletableFuture<>();
        employeeUpdatedResponses.put(dto.codigo().toString(), employeeFuture);

        rabbit.convertAndSend(
                RabbitConstants.EXCHANGE,
                RabbitConstants.UPDATE_EMPLOYEE_CMD_QUEUE,
                new UpdateEmployeeCommandDto(
                        dto.codigo(),
                        dto.cpf(),
                        dto.email(),
                        dto.nome(),
                        dto.telefone(),
                        dto.senha()
                )
        );

        getWithTimeout(employeeUpdatedResponses, dto.codigo().toString());

        CompletableFuture<UserUpdatedEventDto> userFuture = new CompletableFuture<>();
        userUpdateResponses.put(dto.codigo().toString(), userFuture);

        rabbit.convertAndSend(
                RabbitConstants.EXCHANGE,
                RabbitConstants.UPDATE_USER_CMD_QUEUE,
                new UpdateUserCommandDto(dto.codigo(), dto.nome(), dto.email(), "FUNCIONARIO", dto.senha()));

        getWithTimeout(userUpdateResponses, dto.codigo().toString());

        return new EmployeeDto(
                dto.codigo(),
                dto.cpf(),
                dto.email(),
                dto.nome(),
                dto.telefone());
    }

    public EmployeeDto deleteEmployeeSaga(Long codigo) {
        EmployeeDto existsByCode = employeeClient.findEmployeeByCode(codigo);
        if(Objects.isNull(existsByCode)) {
            throw new IllegalArgumentException(
                    "Funcionario não encontrado"
            );
        }

        CompletableFuture<EmployeeDeletedEventDto> employeeFuture = new CompletableFuture<>();
        employeeDeleteResponses.put(codigo.toString(), employeeFuture);

        rabbit.convertAndSend(
                RabbitConstants.EXCHANGE,
                RabbitConstants.DELETE_EMPLOYEE_CMD_QUEUE,
                new DeleteEmployeeCommandDto(codigo)
        );

        EmployeeDeletedEventDto employeeDeleted = getWithTimeout(employeeDeleteResponses, codigo.toString());

        CompletableFuture<UserDeletedEventDto> userFuture = new CompletableFuture<>();
        userDeleteResponses.put(employeeDeleted.email(), userFuture);

        rabbit.convertAndSend(
                RabbitConstants.EXCHANGE,
                RabbitConstants.DELETE_USER_CMD_QUEUE,
                new DeleteUserCommandDto(employeeDeleted.email()));

        getWithTimeout(userDeleteResponses, employeeDeleted.email());

        return new EmployeeDto(
                employeeDeleted.codigo(),
                employeeDeleted.cpf(),
                employeeDeleted.email(),
                employeeDeleted.nome(),
                employeeDeleted.telefone());
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
