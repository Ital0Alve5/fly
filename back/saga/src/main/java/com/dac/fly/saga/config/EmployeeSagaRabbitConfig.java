package com.dac.fly.saga.config;

import com.dac.fly.shared.config.RabbitConstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmployeeSagaRabbitConfig {

    @Bean("employeeSagaExchange")
    public TopicExchange employeeSagaExchange() {
        return new TopicExchange(RabbitConstants.EXCHANGE);
    }

    @Bean("employeeCreateCmdQueue")
    public Queue employeeCreateCmdQueue() {
        return new Queue(RabbitConstants.CREATE_EMPLOYEE_CMD_QUEUE, true);
    }

    @Bean("employeeCreateRespQueue")
    public Queue employeeCreateRespQueue() {
        return new Queue(RabbitConstants.CREATE_EMPLOYEE_RESP_QUEUE, true);
    }

    @Bean("employeeUpdateCmdQueue")
    public Queue employeeUpdateCmdQueue() {
        return new Queue(RabbitConstants.UPDATE_EMPLOYEE_CMD_QUEUE, true);
    }

    @Bean("employeeUpdateRespQueue")
    public Queue employeeUpdateRespQueue() {
        return new Queue(RabbitConstants.UPDATE_EMPLOYEE_RESP_QUEUE, true);
    }

    @Bean("employeeDeleteCmdQueue")
    public Queue employeeDeleteCmdQueue() {
        return new Queue(RabbitConstants.DELETE_EMPLOYEE_CMD_QUEUE, true);
    }

    @Bean("employeeDeleteRespQueue")
    public Queue employeeDeleteRespQueue() {
        return new Queue(RabbitConstants.DELETE_EMPLOYEE_RESP_QUEUE, true);
    }

    @Bean
    public Binding bindEmployeeCreate() {
        return BindingBuilder.bind(employeeCreateCmdQueue())
                .to(employeeSagaExchange())
                .with(RabbitConstants.CREATE_EMPLOYEE_CMD_QUEUE);
    }

    @Bean
    public Binding onEmployeeCreate() {
        return BindingBuilder.bind(employeeCreateRespQueue())
                .to(employeeSagaExchange())
                .with(RabbitConstants.CREATE_EMPLOYEE_RESP_QUEUE);
    }

    @Bean
    public Binding bindEmployeeUpdate() {
        return BindingBuilder.bind(employeeUpdateCmdQueue())
                .to(employeeSagaExchange())
                .with(RabbitConstants.UPDATE_EMPLOYEE_CMD_QUEUE);
    }

    @Bean
    public Binding onEmployeeUpdate() {
        return BindingBuilder.bind(employeeUpdateRespQueue())
                .to(employeeSagaExchange())
                .with(RabbitConstants.UPDATE_EMPLOYEE_RESP_QUEUE);
    }

    @Bean
    public Binding bindEmployeeDelete() {
        return BindingBuilder.bind(employeeDeleteCmdQueue())
                .to(employeeSagaExchange())
                .with(RabbitConstants.DELETE_EMPLOYEE_CMD_QUEUE);
    }

    @Bean
    public Binding onEmployeeDelete() {
        return BindingBuilder.bind(employeeDeleteRespQueue())
                .to(employeeSagaExchange())
                .with(RabbitConstants.DELETE_EMPLOYEE_RESP_QUEUE);
    }
}
