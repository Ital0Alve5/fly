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

}
