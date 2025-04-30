package com.dac.fly.flyservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dac.fly.shared.config.RabbitConstants;

@Configuration
public class FlyRabbitConfig {

    @Bean
    public Queue updateSeatsCommandQueue() {
        return new Queue(RabbitConstants.UPDATE_SEATS_CMD_QUEUE);
    }

    @Bean
    public Queue flightCancelledResponseQueue() {
        return new Queue(RabbitConstants.FLIGHT_CANCELLED_QUEUE);
    }

    @Bean
    public Queue flightCancelledQueue() {
        return new Queue(RabbitConstants.FLIGHT_CANCELLED_QUEUE);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(RabbitConstants.EXCHANGE);
    }

    @Bean
    public Binding bindUpdateSeatsCommand() {
        return BindingBuilder
                .bind(updateSeatsCommandQueue())
                .to(exchange())
                .with(RabbitConstants.UPDATE_SEATS_CMD_QUEUE);
    }

    @Bean
    public Binding bindCancelFlightCommand(Queue cancelFlightCommandQueue, TopicExchange exchange) {
        return BindingBuilder
                .bind(cancelFlightCommandQueue)
                .to(exchange)
                .with(RabbitConstants.CANCEL_FLIGHT_QUEUE);
    }

    @Bean
    public Binding bindFlightCancelledResponse(Queue flightCancelledResponseQueue, TopicExchange exchange) {
        return BindingBuilder
                .bind(flightCancelledResponseQueue)
                .to(exchange)
                .with(RabbitConstants.FLIGHT_CANCELLED_QUEUE);
    }
}
