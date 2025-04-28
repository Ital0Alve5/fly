package com.dac.fly.shared.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String EXCHANGE = "reservation-saga-exchange";
    public static final String START_QUEUE = "saga.start";
    public static final String MILES_QUEUE = "saga.deduct-miles";
    public static final String SEATS_QUEUE = "saga.update-seats";
    public static final String RES_QUEUE = "saga.create-reservation";
    public static final String MILES_COMP_QUEUE = "saga.compensate-miles";
    public static final String RES_COMP_QUEUE = "saga.compensate-reservation";

    @Bean
    Queue startQueue() {
        return new Queue(START_QUEUE);
    }

    @Bean
    Queue milesQueue() {
        return new Queue(MILES_QUEUE);
    }

    @Bean
    Queue seatsQueue() {
        return new Queue(SEATS_QUEUE);
    }

    @Bean
    Queue resQueue() {
        return new Queue(RES_QUEUE);
    }

    @Bean
    Queue milesCompQueue() {
        return new Queue(MILES_COMP_QUEUE);
    }

    @Bean
    Queue resCompQueue() {
        return new Queue(RES_COMP_QUEUE);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    Binding bindStart() {
        return BindingBuilder.bind(startQueue()).to(exchange()).with(START_QUEUE);
    }

    @Bean
    Binding bindDeduct() {
        return BindingBuilder.bind(milesQueue()).to(exchange()).with(MILES_QUEUE);
    }

    @Bean
    Binding bindUpdate() {
        return BindingBuilder.bind(seatsQueue()).to(exchange()).with(SEATS_QUEUE);
    }

    @Bean
    Binding bindCreate() {
        return BindingBuilder.bind(resQueue()).to(exchange()).with(RES_QUEUE);
    }

    @Bean
    Binding bindCompMiles() {
        return BindingBuilder.bind(milesCompQueue()).to(exchange()).with(MILES_COMP_QUEUE);
    }

    @Bean
    Binding bindCompRes() {
        return BindingBuilder.bind(resCompQueue()).to(exchange()).with(RES_COMP_QUEUE);
    }
}
