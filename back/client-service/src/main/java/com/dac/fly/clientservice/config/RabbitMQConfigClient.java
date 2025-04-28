package com.dac.fly.clientservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.dac.fly.shared.config.RabbitConfig;

@Configuration
public class RabbitMQConfigClient {

    @Bean
    public Queue milesQueue() {
        return new Queue(RabbitConfig.MILES_QUEUE);
    }

    @Bean
    public TopicExchange sagaExchange() {
        return new TopicExchange(RabbitConfig.EXCHANGE);
    }

    @Bean
    public Binding bindMilesCommand(Queue milesQueue, TopicExchange sagaExchange) {
        return BindingBuilder
                .bind(milesQueue)
                .to(sagaExchange)
                .with(RabbitConfig.MILES_QUEUE);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}

