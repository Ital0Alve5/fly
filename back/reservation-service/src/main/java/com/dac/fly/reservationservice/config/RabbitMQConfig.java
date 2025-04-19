package com.dac.fly.reservationservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_RESERVA = "evento.reserva";
    public static final String QUEUE_RESERVA_CRIADA = "reserva.criada";
    public static final String ROUTING_KEY_RESERVA_CRIADA = "reserva.criada";

    public static final String QUEUE_RESERVA_CANCELADA = "reserva.cancelada";
    public static final String ROUTING_KEY_RESERVA_CANCELADA = "reserva.cancelada";

    @Bean
    public DirectExchange reservaExchange() {
        return new DirectExchange(EXCHANGE_RESERVA);
    }

    @Bean
    public Queue createdReservationQueue() {
        return new Queue(QUEUE_RESERVA_CRIADA, true);
    }

    @Bean
    public Binding bindingReservaCriada(Queue createdReservationQueue, DirectExchange reservaExchange) {
        return BindingBuilder
                .bind(createdReservationQueue)
                .to(reservaExchange)
                .with(ROUTING_KEY_RESERVA_CRIADA);
    }

    @Bean
    public Queue cancelledReservationQueue() {
        return new Queue(QUEUE_RESERVA_CANCELADA, true);
    }

    @Bean
    public Binding bindingReservaCancelada(Queue cancelledReservationQueue, DirectExchange reservaExchange) {
        return BindingBuilder
                .bind(cancelledReservationQueue)
                .to(reservaExchange)
                .with(ROUTING_KEY_RESERVA_CANCELADA);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
