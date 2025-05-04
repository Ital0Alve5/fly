package com.dac.fly.employeeservice.consumer;

import com.dac.fly.employeeservice.dto.response.EmployeeDto;
import com.dac.fly.employeeservice.publisher.EmployeePublisher;
import com.dac.fly.employeeservice.service.EmployeeService;
import com.dac.fly.shared.config.RabbitConstants;
import com.dac.fly.shared.dto.command.CreateEmployeeCommandDto;
import com.dac.fly.shared.dto.command.DeleteEmployeeCommandDto;
import com.dac.fly.shared.dto.command.UpdateEmployeeCommandDto;
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
    public void handleUpdateEmployee(CreateEmployeeCommandDto cmd) {
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

    @RabbitListener(queues = RabbitConstants.UPDATE_EMPLOYEE_CMD_QUEUE)
    public void handleUpdateEmployee(UpdateEmployeeCommandDto cmd) {
        boolean success = false;
        EmployeeDto response = null;

        try {
            response = employeeService.updateEmployee(cmd.codigo(), cmd);
            success = Objects.nonNull(response);
        } catch (Exception e) {
            System.err.println("Erro ao atualizar funcionário: " + e.getMessage());
        }

        Long codigo = (response != null) ? response.codigo() : null;
        publisher.publishEmployeeUpdatedResponse(cmd.email(), codigo, success);
    }

    @RabbitListener(queues = RabbitConstants.DELETE_EMPLOYEE_CMD_QUEUE)
    public void handleDeleteEmployee(DeleteEmployeeCommandDto cmd) {
        boolean success = false;
        EmployeeDto response = null;

        try {
            response = employeeService.deleteEmployee(cmd.codigo());
            success = Objects.nonNull(response);
        } catch (Exception e) {
            System.err.println("Erro ao deletar funcionário: " + e.getMessage());
        }

        publisher.publishEmployeeDeletedResponse(response, success );
    }
}
