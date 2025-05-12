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

    @Bean("reservationSagaExchange")
    public TopicExchange reservationSagaExchange() {
        return new TopicExchange(RabbitConstants.EXCHANGE);
    }

    @Bean("reservationCreateCmdQueue")
    public Queue reservationCreateCmdQueue() {
        return new Queue(RabbitConstants.CREATE_RESERVATION_CMD_QUEUE, true);
    }

    @Bean("reservationCreatedEventQueue")
    public Queue reservationCreatedEventQueue() {
        return new Queue(RabbitConstants.CREATED_QUEUE, true);
    }

    @Bean("reservationCancelCmdQueue")
    public Queue reservationCancelCmdQueue() {
        return new Queue(RabbitConstants.CANCEL_RESERVATION_CMD_QUEUE, true);
    }

    @Bean("reservationCanceledEventQueue")
    public Queue reservationCanceledEventQueue() {
        return new Queue(RabbitConstants.CANCELED_RESERVATION_RESP_QUEUE, true);
    }

    @Bean("reservationUpdateMilesCmdQueue")
    public Queue reservationUpdateMilesCmdQueue() {
        return new Queue(RabbitConstants.UPDATE_MILES_CMD_QUEUE, true);
    }

    @Bean("reservationUpdateMilesRespQueue")
    public Queue reservationUpdateMilesRespQueue() {
        return new Queue(RabbitConstants.UPDATE_MILES_RESP_QUEUE, true);
    }

    @Bean("reservationUpdateSeatsCmdQueue")
    public Queue reservationUpdateSeatsCmdQueue() {
        return new Queue(RabbitConstants.UPDATE_SEATS_CMD_QUEUE, true);
    }

    @Bean("reservationUpdateSeatsRespQueue")
    public Queue reservationUpdateSeatsRespQueue() {
        return new Queue(RabbitConstants.UPDATE_SEATS_RESP_QUEUE, true);
    }

    // --- nova fila de compensação do cancelamento ---
    @Bean("compensateCancelReservationCmdQueue")
    public Queue compensateCancelReservationCmdQueue() {
        return new Queue(RabbitConstants.COMPENSATE_CANCEL_RESERVATION_CMD_QUEUE, true);
    }

    @Bean
    public Binding bindCompensateCancelReservation() {
        return BindingBuilder
            .bind(compensateCancelReservationCmdQueue())
            .to(reservationSagaExchange())
            .with(RabbitConstants.COMPENSATE_CANCEL_RESERVATION_CMD_QUEUE);
    }
    // -----------------------------------------------

    @Bean
    public Binding bindReservationCreate() {
        return BindingBuilder.bind(reservationCreateCmdQueue())
                .to(reservationSagaExchange())
                .with(RabbitConstants.CREATE_RESERVATION_CMD_QUEUE);
    }

    @Bean
    public Binding bindReservationCreated() {
        return BindingBuilder.bind(reservationCreatedEventQueue())
                .to(reservationSagaExchange())
                .with(RabbitConstants.CREATED_QUEUE);
    }

    @Bean
    public Binding bindReservationCancel() {
        return BindingBuilder.bind(reservationCancelCmdQueue())
                .to(reservationSagaExchange())
                .with(RabbitConstants.CANCEL_RESERVATION_CMD_QUEUE);
    }

    @Bean
    public Binding bindReservationCanceled() {
        return BindingBuilder.bind(reservationCanceledEventQueue())
                .to(reservationSagaExchange())
                .with(RabbitConstants.CANCELED_RESERVATION_RESP_QUEUE);
    }

    @Bean
    public Binding bindReservationMilesCmd() {
        return BindingBuilder.bind(reservationUpdateMilesCmdQueue())
                .to(reservationSagaExchange())
                .with(RabbitConstants.UPDATE_MILES_CMD_QUEUE);
    }

    @Bean
    public Binding bindReservationMilesResp() {
        return BindingBuilder.bind(reservationUpdateMilesRespQueue())
                .to(reservationSagaExchange())
                .with(RabbitConstants.UPDATE_MILES_RESP_QUEUE);
    }

    @Bean
    public Binding bindReservationSeatsCmd() {
        return BindingBuilder.bind(reservationUpdateSeatsCmdQueue())
                .to(reservationSagaExchange())
                .with(RabbitConstants.UPDATE_SEATS_CMD_QUEUE);
    }

    @Bean
    public Binding bindReservationSeatsResp() {
        return BindingBuilder.bind(reservationUpdateSeatsRespQueue())
                .to(reservationSagaExchange())
                .with(RabbitConstants.UPDATE_SEATS_RESP_QUEUE);
    }
}
