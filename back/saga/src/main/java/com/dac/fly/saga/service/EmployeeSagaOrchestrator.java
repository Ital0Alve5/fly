package com.dac.fly.saga.service;

import com.dac.fly.saga.enums.CreateClientSagaStep;
import com.dac.fly.saga.enums.CreateEmployeeSagaStep;
import com.dac.fly.saga.enums.DeleteEmployeeSagaStep;
import com.dac.fly.saga.enums.UpdateEmployeeSagaStep;
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
import org.springframework.stereotype.Service;

import java.util.EnumSet;
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
        if (Objects.nonNull(existsByEmail)) {
            throw new IllegalArgumentException(
                    "E-mail já cadastrado"
            );
        }

        EmployeeDto existsByCpf = employeeClient.findEmployeeByCpf(dto.cpf());
        if (Objects.nonNull(existsByCpf)) {
            throw new IllegalArgumentException(
                    "CPF já cadastrado"
            );
        }

        EnumSet<CreateEmployeeSagaStep> completedSteps = EnumSet.allOf(CreateEmployeeSagaStep.class);
        Long createdCode = null;

        try {
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
            createdCode = employeeCreated.codigo();
            completedSteps.add(CreateEmployeeSagaStep.EMPLOYEE_CREATED);

            CompletableFuture<UserCreatedEventDto> userFuture = new CompletableFuture<>();
            userCreateResponses.put(dto.email(), userFuture);

            rabbit.convertAndSend(
                    RabbitConstants.EXCHANGE,
                    RabbitConstants.CREATE_USER_CMD_QUEUE,
                    new CreateUserCommandDto(dto.nome(), dto.email(), employeeCreated.codigo(), "FUNCIONARIO", dto.senha()));

            getWithTimeout(userCreateResponses, dto.email());
            completedSteps.add(CreateEmployeeSagaStep.USER_CREATED);

            System.out.println("Returning dto");
            return new EmployeeDto(
                    employeeCreated.codigo(),
                    dto.cpf(),
                    dto.email(),
                    dto.nome(),
                    dto.telefone(),
                    "FUNCIONARIO"
            );
        } catch (Exception e) {
            compensateCreateEmployee(createdCode, completedSteps);
        } finally {
            employeeCreateResponses.remove(dto.email());
            userCreateResponses.remove(dto.email());
        }

        System.out.println("Returning null");
        return null;
    }

    private void compensateCreateEmployee(Long codigo, EnumSet<CreateEmployeeSagaStep> completedSteps) {
        if (completedSteps.contains(CreateEmployeeSagaStep.EMPLOYEE_CREATED)) {
            rabbit.convertAndSend(
                    RabbitConstants.EXCHANGE,
                    RabbitConstants.DELETE_EMPLOYEE_CMD_QUEUE,
                    new DeleteEmployeeCommandDto(codigo)
            );
        }
    }

    public EmployeeDto updateEmployeeSaga(UpdateEmployeeDto dto) {
        EmployeeDto original = employeeClient.findEmployeeByCode(dto.codigo());
        if (Objects.isNull(original)) {
            throw new IllegalArgumentException("Funcionario não encontrado");
        }
        AuthDTO emailExists = authClient.findUserByEmail(dto.email());
        if (Objects.nonNull(emailExists)
                && (!emailExists.codigoExterno().equals(dto.codigo())
                || emailExists.role().equals("CLIENTE"))) {
            throw new IllegalArgumentException("Email já cadastrado");
        }
        EmployeeDto cpfExists = employeeClient.findEmployeeByCpf(dto.cpf());
        if (Objects.nonNull(cpfExists) && !cpfExists.codigo().equals(dto.codigo())) {
            throw new IllegalArgumentException("CPF já cadastrado");
        }

        EnumSet<UpdateEmployeeSagaStep> completedSteps = EnumSet.noneOf(UpdateEmployeeSagaStep.class);

        try {
            CompletableFuture<EmployeeUpdatedEventDto> empFuture = new CompletableFuture<>();
            employeeUpdatedResponses.put(dto.codigo().toString(), empFuture);
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
            completedSteps.add(UpdateEmployeeSagaStep.EMPLOYEE_UPDATED);

            CompletableFuture<UserUpdatedEventDto> userFuture = new CompletableFuture<>();
            userUpdateResponses.put(dto.codigo().toString(), userFuture);
            rabbit.convertAndSend(
                    RabbitConstants.EXCHANGE,
                    RabbitConstants.UPDATE_USER_CMD_QUEUE,
                    new UpdateUserCommandDto(
                            dto.codigo(),
                            dto.nome(),
                            dto.email(),
                            "FUNCIONARIO",
                            dto.senha()
                    )
            );
            getWithTimeout(userUpdateResponses, dto.codigo().toString());
            completedSteps.add(UpdateEmployeeSagaStep.USER_UPDATED);

            return new EmployeeDto(
                    dto.codigo(),
                    dto.cpf(),
                    dto.email(),
                    dto.nome(),
                    dto.telefone(),
                    "FUNCIONARIO"
            );

        } catch (Exception e) {
            compensateUpdateEmployee(original, completedSteps);
            throw new RuntimeException("Falha na saga de atualização: " + e.getMessage(), e);
        } finally {
            employeeUpdatedResponses.remove(dto.codigo().toString());
            userUpdateResponses.remove(dto.codigo().toString());
        }
    }

    private void compensateUpdateEmployee(EmployeeDto original, EnumSet<UpdateEmployeeSagaStep> steps) {
        if (steps.contains(UpdateEmployeeSagaStep.EMPLOYEE_UPDATED)
                && !steps.contains(UpdateEmployeeSagaStep.USER_UPDATED)) {

            rabbit.convertAndSend(
                    RabbitConstants.EXCHANGE,
                    RabbitConstants.UPDATE_EMPLOYEE_CMD_QUEUE,
                    new UpdateEmployeeCommandDto(
                            original.codigo(),
                            original.cpf(),
                            original.email(),
                            original.nome(),
                            original.telefone(),
                            null
                    )
            );
        }
    }

    public EmployeeDto deleteEmployeeSaga(Long codigo) {
        EmployeeDto original = employeeClient.findEmployeeByCode(codigo);
        if (Objects.isNull(original)) {
            throw new IllegalArgumentException("Funcionario não encontrado");
        }

        EnumSet<DeleteEmployeeSagaStep> completedSteps = EnumSet.noneOf(DeleteEmployeeSagaStep.class);

        try {
            CompletableFuture<EmployeeDeletedEventDto> empDelFuture = new CompletableFuture<>();
            employeeDeleteResponses.put(codigo.toString(), empDelFuture);
            rabbit.convertAndSend(
                    RabbitConstants.EXCHANGE,
                    RabbitConstants.DELETE_EMPLOYEE_CMD_QUEUE,
                    new DeleteEmployeeCommandDto(codigo)
            );
            EmployeeDeletedEventDto empDeleted = getWithTimeout(employeeDeleteResponses, codigo.toString());
            completedSteps.add(DeleteEmployeeSagaStep.EMPLOYEE_DELETED);

            CompletableFuture<UserDeletedEventDto> userDelFuture = new CompletableFuture<>();
            userDeleteResponses.put(empDeleted.email(), userDelFuture);
            rabbit.convertAndSend(
                    RabbitConstants.EXCHANGE,
                    RabbitConstants.DELETE_USER_CMD_QUEUE,
                    new DeleteUserCommandDto(empDeleted.email())
            );
            getWithTimeout(userDeleteResponses, empDeleted.email());
            completedSteps.add(DeleteEmployeeSagaStep.USER_DELETED);

            return new EmployeeDto(
                    empDeleted.codigo(),
                    empDeleted.cpf(),
                    empDeleted.email(),
                    empDeleted.nome(),
                    empDeleted.telefone(),
                    "FUNCIONARIO"
            );

        } catch (Exception e) {
            compensateDeleteEmployee(original, completedSteps);
            throw new RuntimeException("Falha na saga de deleção: " + e.getMessage(), e);
        } finally {
            employeeDeleteResponses.remove(codigo.toString());
            userDeleteResponses.remove(original.email());
        }
    }

    private void compensateDeleteEmployee(EmployeeDto original, EnumSet<DeleteEmployeeSagaStep> steps) {
        if (steps.contains(DeleteEmployeeSagaStep.EMPLOYEE_DELETED)
                && !steps.contains(DeleteEmployeeSagaStep.USER_DELETED)) {

            rabbit.convertAndSend(
                    RabbitConstants.EXCHANGE,
                    RabbitConstants.CREATE_EMPLOYEE_CMD_QUEUE,
                    new CreateEmployeeCommandDto(
                            original.cpf(),
                            original.email(),
                            original.nome(),
                            original.telefone()
                    )
            );
        }
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
