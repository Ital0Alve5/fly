package com.dac.fly.saga.consumer;

import com.dac.fly.shared.config.RabbitConstants;
import com.dac.fly.shared.dto.events.EmployeeCreatedEventDto;
import com.dac.fly.shared.dto.events.EmployeeDeletedEventDto;
import com.dac.fly.shared.dto.events.EmployeeUpdatedEventDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EmployeeSagaConsumer {
    private final ConcurrentHashMap<String, CompletableFuture<EmployeeCreatedEventDto>> createEmployeeResponses;
    private final ConcurrentHashMap<String, CompletableFuture<EmployeeUpdatedEventDto>> updateEmployeeResponses;
    private final ConcurrentHashMap<String, CompletableFuture<EmployeeDeletedEventDto>> deleteEmployeeResponses;

    public EmployeeSagaConsumer(
            ConcurrentHashMap<String, CompletableFuture<EmployeeCreatedEventDto>> createEmployeeResponses,
            ConcurrentHashMap<String, CompletableFuture<EmployeeDeletedEventDto>> deleteEmployeeResponses,
            ConcurrentHashMap<String, CompletableFuture<EmployeeUpdatedEventDto>> updateEmployeeResponses
    ) {
        this.createEmployeeResponses = createEmployeeResponses;
        this.deleteEmployeeResponses = deleteEmployeeResponses;
        this.updateEmployeeResponses = updateEmployeeResponses;
    }

    @RabbitListener(queues = RabbitConstants.CREATE_EMPLOYEE_RESP_QUEUE)
    public void onEmployeeCreated(EmployeeCreatedEventDto evt) {
        var future = createEmployeeResponses.remove(evt.email());
        if (future != null) {
            future.complete(evt);
        }
    }

    @RabbitListener(queues = RabbitConstants.UPDATE_EMPLOYEE_RESP_QUEUE)
    public void onEmployeeUpdated(EmployeeUpdatedEventDto evt) {
        var future = updateEmployeeResponses.remove(evt.email());
        if (future != null) {
            future.complete(evt);
        }
    }

    @RabbitListener(queues = RabbitConstants.DELETE_EMPLOYEE_RESP_QUEUE)
    public void onEmployeeDeleted(EmployeeDeletedEventDto evt) {
        var future = deleteEmployeeResponses.remove(evt.codigo().toString());
        if (future != null) {
            future.complete(evt);
        }
    }
}
