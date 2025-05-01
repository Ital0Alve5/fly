package com.dac.fly.reservationservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dac.fly.shared.config.RabbitConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class RabbitMQConfig {

    @Bean
    public TopicExchange sagaExchange() {
        return new TopicExchange(RabbitConstants.EXCHANGE);
    }

    @Bean
    public Queue sagaCancelledQueue() {
        return new Queue(RabbitConstants.CANCELED_RESERVATION_RESP_QUEUE, true);
    }

    @Bean
    public Queue sagaCreatedQueue() {
        return new Queue(RabbitConstants.CREATED_QUEUE, true);
    }

    @Bean
    public Binding bindSagaCancelled(
            @Qualifier("sagaCancelledQueue") Queue sagaCancelledQueue,
            TopicExchange sagaExchange) {
        return BindingBuilder
                .bind(sagaCancelledQueue)
                .to(sagaExchange)
                .with(RabbitConstants.CANCELED_RESERVATION_RESP_QUEUE);
    }

    @Bean
    public Binding bindSagaCreated(
            @Qualifier("sagaCreatedQueue") Queue sagaCreatedQueue,
            TopicExchange sagaExchange) {
        return BindingBuilder
                .bind(sagaCreatedQueue)
                .to(sagaExchange)
                .with(RabbitConstants.CREATED_QUEUE);
    }

    @Bean
    public DirectExchange internalExchange() {
        return new DirectExchange("reserva-exchange");
    }

    // 4) Queues internas + Bindings
    public static final String IN_QUEUE_CRIADA = "reserva.criada";
    public static final String IN_QUEUE_CANCELADA = "reserva.cancelada";
    public static final String IN_QUEUE_ATUALIZADA = "reserva.atualizada";

    @Bean
    public Queue internalCreatedQueue() {
        return new Queue(IN_QUEUE_CRIADA, true);
    }

    @Bean
    public Binding bindInternalCreated(
            @Qualifier("internalCreatedQueue") Queue q,
            DirectExchange internalExchange) {
        return BindingBuilder
                .bind(q)
                .to(internalExchange)
                .with(IN_QUEUE_CRIADA);
    }

    @Bean
    public Queue internalCancelledQueue() {
        return new Queue(IN_QUEUE_CANCELADA, true);
    }

    @Bean
    public Binding bindInternalCancelled(
            @Qualifier("internalCancelledQueue") Queue q,
            DirectExchange internalExchange) {
        return BindingBuilder
                .bind(q)
                .to(internalExchange)
                .with(IN_QUEUE_CANCELADA);
    }

    @Bean
    public Queue internalUpdatedQueue() {
        return new Queue(IN_QUEUE_ATUALIZADA, true);
    }

    @Bean
    public Binding bindInternalUpdated(
            @Qualifier("internalUpdatedQueue") Queue q,
            DirectExchange internalExchange) {
        return BindingBuilder
                .bind(q)
                .to(internalExchange)
                .with(IN_QUEUE_ATUALIZADA);
    }

    @Bean
    public Queue cancelReservationsByFlightCmdQueue() {
        return new Queue(RabbitConstants.CANCEL_RESERVATION_BY_FLIGHT_CMD_QUEUE, true);
    }

    @Bean
    public Binding bindCancelReservationsByFlightCmd(
            @Qualifier("cancelReservationsByFlightCmdQueue") Queue q,
            TopicExchange sagaExchange) {
        return BindingBuilder.bind(q)
                .to(sagaExchange)
                .with(RabbitConstants.CANCEL_RESERVATION_BY_FLIGHT_CMD_QUEUE);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter(ObjectMapper mapper) {
        return new Jackson2JsonMessageConverter(mapper);
    }
}