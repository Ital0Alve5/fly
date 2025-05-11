package com.dac.fly.reservationservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dac.fly.shared.config.RabbitConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class RabbitMQConfig {

    public static final String INTERNAL_EXCHANGE_NAME = "reserva-exchange";
    public static final String INTERNAL_CREATED_KEY = "reserva.criada";
    public static final String INTERNAL_CANCELLED_KEY = "reserva.cancelada";
    public static final String INTERNAL_UPDATED_KEY = "reserva.atualizada";
    public static final String INTERNAL_CANCEL_FLIGHT_KEY = "reserva.flight-reservations-cancel";
    public static final String INTERNAL_COMPENSATE_CREATE_KEY = "reserva.removida";

    @Bean
    public TopicExchange sagaExchange() {
        return new TopicExchange(RabbitConstants.EXCHANGE);
    }

    @Bean
    public DirectExchange internalExchange() {
        return new DirectExchange(RabbitMQConfig.INTERNAL_EXCHANGE_NAME);
    }

    @Bean
    public Jackson2JsonMessageConverter jacksonConverter(ObjectMapper mapper) {
        return new Jackson2JsonMessageConverter(mapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory cf, Jackson2JsonMessageConverter conv) {
        RabbitTemplate tpl = new RabbitTemplate(cf);
        tpl.setMessageConverter(conv);
        return tpl;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory cf, Jackson2JsonMessageConverter conv) {
        var f = new SimpleRabbitListenerContainerFactory();
        f.setConnectionFactory(cf);
        f.setMessageConverter(conv);
        return f;
    }

    // filas "externas" (saga commands / responses)
    @Bean
    public Queue createReservationCmd() {
        return new Queue(RabbitConstants.CREATE_RESERVATION_CMD_QUEUE, true);
    }

    @Bean
    public Queue cancelReservationCmd() {
        return new Queue(RabbitConstants.CANCEL_RESERVATION_CMD_QUEUE, true);
    }

    @Bean
    public Queue flightReservationsCancelled() {
        return new Queue(RabbitConstants.FLIGHT_RESERVATIONS_CANCELLED_QUEUE, true);
    }

    // filas "internas" CQRS
    @Bean
    public Queue internalCreated() {
        return new Queue(RabbitMQConfig.INTERNAL_CREATED_KEY, true);
    }

    @Bean
    public Queue internalCanceled() {
        return new Queue(RabbitMQConfig.INTERNAL_CANCELLED_KEY, true);
    }

    @Bean
    public Queue internalFlightResCancel() {
        return new Queue(RabbitMQConfig.INTERNAL_CANCEL_FLIGHT_KEY, true);
    }

    // binds
    @Bean
    public Binding bindCreate() {
        return BindingBuilder.bind(createReservationCmd())
                .to(sagaExchange())
                .with(RabbitConstants.CREATE_RESERVATION_CMD_QUEUE);
    }

    @Bean
    public Binding bindCancelCmd() {
        return BindingBuilder.bind(cancelReservationCmd())
                .to(sagaExchange())
                .with(RabbitConstants.CANCEL_RESERVATION_CMD_QUEUE);
    }

    @Bean
    public Binding bindFlightResCancelled() {
        return BindingBuilder.bind(flightReservationsCancelled())
                .to(sagaExchange())
                .with(RabbitConstants.FLIGHT_RESERVATIONS_CANCELLED_QUEUE);
    }

    @Bean
    public Binding bindInternalCreated() {
        return BindingBuilder.bind(internalCreated())
                .to(internalExchange())
                .with(RabbitMQConfig.INTERNAL_CREATED_KEY);
    }

    @Bean
    public Binding bindInternalCanceled() {
        return BindingBuilder.bind(internalCanceled())
                .to(internalExchange())
                .with(RabbitMQConfig.INTERNAL_CANCELLED_KEY);
    }

    @Bean
    public Binding bindInternalFlightResCancelled() {
        return BindingBuilder.bind(internalFlightResCancel())
                .to(internalExchange())
                .with(RabbitMQConfig.INTERNAL_CANCEL_FLIGHT_KEY);
    }

    @Bean
    public Queue compensateCreateReservationQueue() {
        return new Queue(RabbitConstants.COMPENSATE_CREATE_RESERVATION_CMD_QUEUE, true);
    }

    @Bean
    public Binding bindCompensateCreateReservation(TopicExchange exchange, Queue compensateQueue) {
        return BindingBuilder
                .bind(compensateQueue)
                .to(exchange)
                .with(RabbitConstants.COMPENSATE_CREATE_RESERVATION_CMD_QUEUE);
    }

    @Bean
    public Queue internalCompensateCreateQueue() {
        return new Queue(RabbitMQConfig.INTERNAL_COMPENSATE_CREATE_KEY, true);
    }

    @Bean
    public Binding bindInternalCompensateCreate(
            @Qualifier("internalExchange") DirectExchange internalExchange,
            Queue internalCompensateCreateQueue) {
        return BindingBuilder
                .bind(internalCompensateCreateQueue)
                .to(internalExchange)
                .with(RabbitMQConfig.INTERNAL_COMPENSATE_CREATE_KEY);
    }

    @Bean
    public Queue compensateCreateReservationRespQueue() {
        return new Queue(RabbitConstants.COMPENSATE_CREATE_RESERVATION_RESP_QUEUE, true);
    }

    @Bean
    public Binding bindCompensateCreateReservationResp(
            @Qualifier("sagaExchange") TopicExchange sagaExchange,
            @Qualifier("compensateCreateReservationRespQueue") Queue respQueue) {
        return BindingBuilder
                .bind(respQueue)
                .to(sagaExchange)
                .with(RabbitConstants.COMPENSATE_CREATE_RESERVATION_RESP_QUEUE);
    }

    @Bean
    public Queue failedCreateReservationQueue() {
        return new Queue(RabbitConstants.FAILED_CREATE_RESERVATION_QUEUE, true);
    }

    @Bean
    public Binding bindFailedCreateReservation(
            @Qualifier("sagaExchange") TopicExchange sagaExchange,
            Queue failedCreateReservationQueue) {
        return BindingBuilder
                .bind(failedCreateReservationQueue)
                .to(sagaExchange)
                .with(RabbitConstants.FAILED_CREATE_RESERVATION_QUEUE);
    }
}
