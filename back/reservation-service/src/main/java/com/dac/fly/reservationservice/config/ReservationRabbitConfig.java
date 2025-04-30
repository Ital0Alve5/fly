package com.dac.fly.reservationservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dac.fly.shared.config.RabbitConstants;

@Configuration
public class ReservationRabbitConfig {

    @Bean
    public Queue resQueue() {
        return new Queue(RabbitConstants.RES_QUEUE); 
    }

    @Bean
    public Queue cancelQueue() {
        return new Queue(RabbitConstants.CANCEL_QUEUE); 
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(RabbitConstants.EXCHANGE);
    }

    @Bean
    public Binding bindRes() {
        return BindingBuilder
                .bind(resQueue())
                .to(exchange())
                .with(RabbitConstants.RES_QUEUE);
    }

    @Bean
    public Binding bindCancel() {
        return BindingBuilder
                .bind(cancelQueue())
                .to(exchange())
                .with(RabbitConstants.CANCEL_QUEUE);
    }
}
