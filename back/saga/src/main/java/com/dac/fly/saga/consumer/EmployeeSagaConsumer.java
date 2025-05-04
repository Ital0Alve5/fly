package com.dac.fly.saga.consumer;

import com.dac.fly.shared.config.RabbitConstants;
import com.dac.fly.shared.dto.events.EmployeeCreatedEventDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EmployeeSagaConsumer {
    private final ConcurrentHashMap<String, CompletableFuture<EmployeeCreatedEventDto>> createEmployeeResponses;

    public EmployeeSagaConsumer(ConcurrentHashMap<String, CompletableFuture<EmployeeCreatedEventDto>> createEmployeeResponses) {
        this.createEmployeeResponses = createEmployeeResponses;
    }

    @RabbitListener(queues = RabbitConstants.CREATE_EMPLOYEE_RESP_QUEUE)
    public void onEmployeeCreated(EmployeeCreatedEventDto evt) {
        System.out.println("Employee created event resp on saga: " + evt);
        var future = createEmployeeResponses.get(evt.email());
        if (future != null) {
            future.complete(evt);
        }
    }
}
