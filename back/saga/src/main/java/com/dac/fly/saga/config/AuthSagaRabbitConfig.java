package com.dac.fly.saga.config;

import com.dac.fly.shared.config.RabbitConstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthSagaRabbitConfig {

    @Bean("authSagaExchange")
    public TopicExchange authSagaExchange() {
        return new TopicExchange(RabbitConstants.EXCHANGE);
    }

    @Bean("userCreateCmdQueue")
    public Queue userCreateCmdQueue() {
        return new Queue(RabbitConstants.CREATE_USER_CMD_QUEUE, true);
    }

    @Bean("userCreateRespQueue")
    public Queue userCreateRespQueue() {
        return new Queue(RabbitConstants.CREATE_USER_RESP_QUEUE, true);
    }

    @Bean("userUpdateCmdQueue")
    public Queue userUpdateCmdQueue() {
        return new Queue(RabbitConstants.UPDATE_USER_CMD_QUEUE, true);
    }

    @Bean("userUpdateRespQueue")
    public Queue userUpdateRespQueue() {
        return new Queue(RabbitConstants.UPDATE_USER_RESP_QUEUE, true);
    }

    @Bean("userDeleteCmdQueue")
    public Queue userDeleteCmdQueue() {
        return new Queue(RabbitConstants.DELETE_USER_CMD_QUEUE, true);
    }

    @Bean("userDeleteRespQueue")
    public Queue userDeleteRespQueue() {
        return new Queue(RabbitConstants.DELETE_USER_RESP_QUEUE, true);
    }

    @Bean
    public Binding bindUserCreate() {
        return BindingBuilder.bind(userCreateCmdQueue())
                .to(authSagaExchange())
                .with(RabbitConstants.CREATE_USER_CMD_QUEUE);
    }

    @Bean
    public Binding onUserCreate() {
        return BindingBuilder.bind(userCreateRespQueue())
                .to(authSagaExchange())
                .with(RabbitConstants.CREATE_USER_RESP_QUEUE);
    }

    @Bean
    public Binding bindUserUpdate() {
        return BindingBuilder.bind(userUpdateCmdQueue())
                .to(authSagaExchange())
                .with(RabbitConstants.UPDATE_USER_CMD_QUEUE);
    }

    @Bean
    public Binding onUserUpdate() {
        return BindingBuilder.bind(userUpdateRespQueue())
                .to(authSagaExchange())
                .with(RabbitConstants.UPDATE_USER_RESP_QUEUE);
    }

    @Bean
    public Binding bindUserDelete() {
        return BindingBuilder.bind(userDeleteCmdQueue())
                .to(authSagaExchange())
                .with(RabbitConstants.DELETE_USER_CMD_QUEUE);
    }

    @Bean
    public Binding onUserDelete() {
        return BindingBuilder.bind(userDeleteRespQueue())
                .to(authSagaExchange())
                .with(RabbitConstants.DELETE_USER_RESP_QUEUE);
    }
}
