package com.dac.fly.flyservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.dac.fly.shared.config.RabbitConfig;

@Configuration
public class RabbitMQConfigFly {

    @Bean
    public Queue seatsQueue() {
        return new Queue(RabbitConfig.SEATS_QUEUE);
    }

    @Bean
    public TopicExchange sagaExchange() {
        return new TopicExchange(RabbitConfig.EXCHANGE);
    }

    @Bean
    public Binding bindSeatsCommand(Queue seatsQueue, TopicExchange sagaExchange) {
        return BindingBuilder
                .bind(seatsQueue)
                .to(sagaExchange)
                .with(RabbitConfig.SEATS_QUEUE);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
