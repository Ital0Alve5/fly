package com.dac.fly.saga.config;

import com.dac.fly.shared.config.RabbitConstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientSagaRabbitConfig {

    @Bean("clientSagaExchange")
    public TopicExchange clientSagaExchange() {
        return new TopicExchange(RabbitConstants.EXCHANGE);
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
                .to(clientSagaExchange())
                .with(RabbitConstants.CREATE_CLIENT_CMD_QUEUE);
    }

    @Bean
    public Binding onClientCreate() {
        return BindingBuilder.bind(clientCreateRespQueue())
                .to(clientSagaExchange())
                .with(RabbitConstants.CREATE_CLIENT_RESP_QUEUE);
    }

    @Bean
    public Binding bindClientDelete() {
        return BindingBuilder.bind(clientDeleteCmdQueue())
                .to(clientSagaExchange())
                .with(RabbitConstants.DELETE_CLIENT_CMD_QUEUE);
    }

    @Bean
    public Binding onClientDelete() {
        return BindingBuilder.bind(clientDeleteRespQueue())
                .to(clientSagaExchange())
                .with(RabbitConstants.DELETE_CLIENT_RESP_QUEUE);
    }
}
