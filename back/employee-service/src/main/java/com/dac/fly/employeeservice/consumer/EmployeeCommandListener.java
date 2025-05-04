package com.dac.fly.employeeservice.consumer;

import com.dac.fly.employeeservice.dto.response.EmployeeDto;
import com.dac.fly.employeeservice.publisher.EmployeePublisher;
import com.dac.fly.employeeservice.service.EmployeeService;
import com.dac.fly.shared.config.RabbitConstants;
import com.dac.fly.shared.dto.command.CreateEmployeeCommandDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class EmployeeCommandListener {
    private final EmployeeService employeeService;
    private final EmployeePublisher publisher;

    public EmployeeCommandListener(EmployeeService clientService, EmployeePublisher publisher) {
        this.employeeService = clientService;
        this.publisher = publisher;
    }

    @RabbitListener(queues = RabbitConstants.CREATE_EMPLOYEE_CMD_QUEUE)
    public void handleUpdateMiles(CreateEmployeeCommandDto cmd) {
        boolean success = false;
        EmployeeDto response = null;

        try {
            response = employeeService.createNewEmployee(cmd);
            success = Objects.nonNull(response);
        } catch (Exception e) {
            System.err.println("Erro ao criar funcionário: " + e.getMessage());
        }

        Long codigo = (response != null) ? response.codigo() : null;
        publisher.publishEmployeeCreatedResponse(cmd.email(), codigo, success);
    }
}
