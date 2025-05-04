package com.dac.fly.employeeservice.publisher;

import com.dac.fly.employeeservice.dto.response.EmployeeDto;
import com.dac.fly.shared.config.RabbitConstants;
import com.dac.fly.shared.dto.events.EmployeeCreatedEventDto;
import com.dac.fly.shared.dto.events.EmployeeDeletedEventDto;
import com.dac.fly.shared.dto.events.EmployeeUpdatedEventDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class EmployeePublisher {

    private final RabbitTemplate rabbit;

    public EmployeePublisher(RabbitTemplate rabbit) {
        this.rabbit = rabbit;
    }

    public void publishEmployeeCreatedResponse(String email, Long codigo, boolean success) {
        System.out.println("On employee service: Publishing created event for " + email);
        rabbit.convertAndSend(
                RabbitConstants.EXCHANGE,
                RabbitConstants.CREATE_EMPLOYEE_RESP_QUEUE,
                new EmployeeCreatedEventDto(email, codigo, success)
        );
    }

    public void publishEmployeeUpdatedResponse(String email, Long codigo, boolean success) {
        System.out.println("On employee service: Publishing updated event for " + email);
        rabbit.convertAndSend(
                RabbitConstants.EXCHANGE,
                RabbitConstants.UPDATE_EMPLOYEE_RESP_QUEUE,
                new EmployeeUpdatedEventDto(email, codigo, success)
        );
    }

    public void publishEmployeeDeletedResponse(EmployeeDto dto, boolean success) {
        System.out.println("On employee service: Publishing deleted event for " + dto.email());
        rabbit.convertAndSend(
                RabbitConstants.EXCHANGE,
                RabbitConstants.DELETE_EMPLOYEE_RESP_QUEUE,
                new EmployeeDeletedEventDto(dto.codigo(), dto.email(), dto.cpf(), dto.nome(), dto.telefone(), success)
        );
    }
}
