package com.dac.fly.saga.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dac.fly.shared.config.RabbitConstants;

@Configuration
public class FlightSagaRabbitConfig {
    @Bean
    Queue cancelFlightQueue() {
        return new Queue(RabbitConstants.CANCEL_FLIGHT_QUEUE);
    }

    @Bean
    Queue flightCancelledQueue() {
        return new Queue(RabbitConstants.FLIGHT_CANCELLED_QUEUE);
    }

    @Bean
    Queue flightReservationsCancelledQueue() {
        return new Queue(RabbitConstants.FLIGHT_RESERVATIONS_CANCELLED_QUEUE);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(RabbitConstants.EXCHANGE);
    }

    @Bean
    Binding bindCancelFlight() {
        return BindingBuilder
                .bind(cancelFlightQueue())
                .to(exchange())
                .with(RabbitConstants.CANCEL_FLIGHT_QUEUE);
    }

    @Bean
    Binding bindFlightCancelled() {
        return BindingBuilder
                .bind(flightCancelledQueue())
                .to(exchange())
                .with(RabbitConstants.FLIGHT_CANCELLED_QUEUE);
    }

    @Bean
    Binding bindFlightReservationsCancelled() {
        return BindingBuilder
                .bind(flightReservationsCancelledQueue())
                .to(exchange())
                .with(RabbitConstants.FLIGHT_RESERVATIONS_CANCELLED_QUEUE);
    }
}
