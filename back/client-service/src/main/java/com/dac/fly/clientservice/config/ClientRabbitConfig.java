package com.dac.fly.clientservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dac.fly.shared.config.RabbitConstants;

@Configuration
public class ClientRabbitConfig {

    @Bean
    public Queue milesCommandQueue() {
        return new Queue(RabbitConstants.UPDATE_MILES_CMD_QUEUE);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(RabbitConstants.EXCHANGE);
    }

    @Bean
    public Binding bindUpdateMiles() {
        return BindingBuilder
                .bind(milesCommandQueue())
                .to(exchange())
                .with(RabbitConstants.UPDATE_MILES_CMD_QUEUE);
    }

    @Bean("clientCreateCmdQueue")
    public Queue clientCreateCmdQueue() {
        return new Queue(RabbitConstants.CREATE_CLIENT_CMD_QUEUE, true);
    }

    @Bean("clientCreateRespQueue")
    public Queue clientCreateRespQueue() {
        return new Queue(RabbitConstants.CREATE_CLIENT_RESP_QUEUE, true);
    }

    @Bean("clientDeleteCmdQueue")
    public Queue clientDeleteCmdQueue() {
        return new Queue(RabbitConstants.DELETE_CLIENT_CMD_QUEUE, true);
    }

    @Bean("clientDeleteRespQueue")
    public Queue clientDeleteRespQueue() {
        return new Queue(RabbitConstants.DELETE_EMPLOYEE_RESP_QUEUE, true);
    }

    @Bean
    public Binding bindClientCreate() {
        return BindingBuilder.bind(clientCreateCmdQueue())
                .to(exchange())
                .with(RabbitConstants.CREATE_CLIENT_CMD_QUEUE);
    }

    @Bean
    public Binding onClientCreate() {
        return BindingBuilder.bind(clientCreateRespQueue())
                .to(exchange())
                .with(RabbitConstants.CREATE_CLIENT_RESP_QUEUE);
    }

    @Bean
    public Queue compensateMilesCmdQueue() {
        return new Queue(RabbitConstants.COMPENSATE_MILES_CMD_QUEUE, true);
    }

    @Bean
    public Binding bindCompensateMilesCmd() {
        return BindingBuilder.bind(compensateMilesCmdQueue())
                .to(exchange())
                .with(RabbitConstants.COMPENSATE_MILES_CMD_QUEUE);
    }

    @Bean
    public Queue rollbackMilesCmdQueue() {
        return new Queue(RabbitConstants.ROLLBACK_MILES_CMD_QUEUE, true);
    }

    @Bean
    public Binding bindRollbackMilesCmd() {
        return BindingBuilder
                .bind(rollbackMilesCmdQueue())
                .to(exchange())
                .with(RabbitConstants.ROLLBACK_MILES_CMD_QUEUE);
    }

    @Bean
    public Binding bindClientDelete() {
        return BindingBuilder.bind(clientDeleteCmdQueue())
                .to(exchange())
                .with(RabbitConstants.DELETE_CLIENT_CMD_QUEUE);
    }

    @Bean
    public Binding onClientDelete() {
        return BindingBuilder.bind(clientDeleteRespQueue())
                .to(exchange())
                .with(RabbitConstants.DELETE_CLIENT_RESP_QUEUE);
    }
}
