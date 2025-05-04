package com.dac.fly.employeeservice.publisher;

import com.dac.fly.shared.config.RabbitConstants;
import com.dac.fly.shared.dto.events.EmployeeCreatedEventDto;
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
}
