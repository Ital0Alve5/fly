package com.dac.fly.saga.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dac.fly.shared.config.RabbitConstants;

@Configuration
public class ReservationSagaRabbitConfig {

    @Bean
    Queue resQueue() {
        return new Queue(RabbitConstants.RES_QUEUE);
    }

    @Bean
    Queue createdQueue() {
        return new Queue(RabbitConstants.CREATED_QUEUE);
    }

    @Bean
    Queue cancelQueue() {
        return new Queue(RabbitConstants.CANCEL_QUEUE);
    }

    @Bean
    Queue canceledQueue() {
        return new Queue(RabbitConstants.CANCELED_QUEUE);
    }

    @Bean
    Queue milesResponseQueue() {
        return new Queue(RabbitConstants.UPDATE_MILES_RESP_QUEUE);
    }

    @Bean
    Queue seatsResponseQueue() {
        return new Queue(RabbitConstants.UPDATE_SEATS_RESP_QUEUE);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(RabbitConstants.EXCHANGE);
    }

    @Bean
    Binding bindRes() {
        return BindingBuilder.bind(resQueue()).to(exchange()).with(RabbitConstants.RES_QUEUE);
    }

    @Bean
    Binding bindCreated() {
        return BindingBuilder.bind(createdQueue()).to(exchange()).with(RabbitConstants.CREATED_QUEUE);
    }

    @Bean
    Binding bindCancel() {
        return BindingBuilder.bind(cancelQueue()).to(exchange()).with(RabbitConstants.CANCEL_QUEUE);
    }

    @Bean
    Binding bindCanceled() {
        return BindingBuilder.bind(canceledQueue()).to(exchange()).with(RabbitConstants.CANCELED_QUEUE);
    }

    @Bean
    Binding bindMilesResponse() {
        return BindingBuilder.bind(milesResponseQueue()).to(exchange()).with(RabbitConstants.UPDATE_MILES_RESP_QUEUE);
    }

    @Bean
    Binding bindSeatsResponse() {
        return BindingBuilder.bind(seatsResponseQueue()).to(exchange()).with(RabbitConstants.UPDATE_SEATS_RESP_QUEUE);
    }
}
