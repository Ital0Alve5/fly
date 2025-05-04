package com.dac.fly.authservice.config;

import com.dac.fly.shared.config.RabbitConstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthRabbitConfig {
    @Bean("authSagaExchange")
    public TopicExchange exchange() {
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

    @Bean
    public Binding bindUserCreate() {
        return BindingBuilder.bind(userCreateCmdQueue())
                .to(exchange())
                .with(RabbitConstants.CREATE_USER_CMD_QUEUE);
    }

    @Bean
    public Binding onUserCreate() {
        return BindingBuilder.bind(userCreateRespQueue())
                .to(exchange())
                .with(RabbitConstants.CREATE_USER_RESP_QUEUE);
    }
}
