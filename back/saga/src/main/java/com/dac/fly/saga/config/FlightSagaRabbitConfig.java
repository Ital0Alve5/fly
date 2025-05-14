package com.dac.fly.saga.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dac.fly.shared.config.RabbitConstants;

@Configuration
public class FlightSagaRabbitConfig {

    @Bean("flightSagaExchange")
    public TopicExchange flightSagaExchange() {
        return new TopicExchange(RabbitConstants.EXCHANGE);
    }

    @Bean("flightCancelCmdQueue")
    public Queue flightCancelCmdQueue() {
        return new Queue(RabbitConstants.CANCEL_FLIGHT_CMD_QUEUE, true);
    }

    @Bean("flightCancelledEventQueue")
    public Queue flightCancelledEventQueue() {
        return new Queue(RabbitConstants.FLIGHT_CANCELLED_RESP_QUEUE, true);
    }

    @Bean("flightCompleteCmdQueue")
    public Queue flightCompleteCmdQueue() {
        return new Queue(RabbitConstants.COMPLETE_FLIGHT_CMD_QUEUE, true);
    }

    @Bean("flightCompletedEventQueue")
    public Queue flightCompletedEventQueue() { return new Queue(RabbitConstants.FLIGHT_COMPLETED_RESP_QUEUE, true); }

    @Bean("completeReservationsByFlightCmdQueue")
    public Queue completeReservationsByFlightCmdQueue() {
        return new Queue(RabbitConstants.COMPLETE_RESERVATION_BY_FLIGHT_CMD_QUEUE, true);
    }

    @Bean("flightReservationsCompletedEventQueue")
    public Queue flightReservationsCompletedEventQueue() { return new Queue(RabbitConstants.FLIGHT_RESERVATIONS_COMPLETED_QUEUE, true); }

    @Bean("flightReservationsCancelledEventQueue")
    public Queue flightReservationsCancelledEventQueue() {
        return new Queue(RabbitConstants.FLIGHT_RESERVATIONS_CANCELLED_QUEUE, true);
    }

    @Bean("flightUpdateMilesRespQueue")
    public Queue flightUpdateMilesRespQueue() {
        return new Queue(RabbitConstants.UPDATE_MILES_RESP_QUEUE, true);
    }

    @Bean("compensateCompleteFlightCmdQueue")
    public Queue compensateCompleteFlight() {
        return new Queue(RabbitConstants.COMPENSATE_COMPLETE_FLIGHT_CMD_QUEUE, true);
    }

    @Bean
    public Binding bindFlightCancel() {
        return BindingBuilder.bind(flightCancelCmdQueue())
                .to(flightSagaExchange())
                .with(RabbitConstants.CANCEL_FLIGHT_CMD_QUEUE);
    }

    @Bean
    public Binding bindFlightCancelled() {
        return BindingBuilder.bind(flightCancelledEventQueue())
                .to(flightSagaExchange())
                .with(RabbitConstants.FLIGHT_CANCELLED_RESP_QUEUE);
    }

    @Bean
    public Binding bindFlightReservationsCancelled() {
        return BindingBuilder.bind(flightReservationsCancelledEventQueue())
                .to(flightSagaExchange())
                .with(RabbitConstants.FLIGHT_RESERVATIONS_CANCELLED_QUEUE);
    }

    @Bean
    public Binding bindFlightMilesResp() {
        return BindingBuilder.bind(flightUpdateMilesRespQueue())
                .to(flightSagaExchange())
                .with(RabbitConstants.UPDATE_MILES_RESP_QUEUE);
    }

    @Bean("cancelReservationsByFlightCmdQueue")
    public Queue cancelReservationsByFlightCmdQueue() {
        return new Queue(RabbitConstants.CANCEL_RESERVATION_BY_FLIGHT_CMD_QUEUE, true);
    }

    @Bean
    public Binding bindCancelReservationsByFlightCmd(
            @Qualifier("cancelReservationsByFlightCmdQueue") Queue q) {
        return BindingBuilder.bind(q)
                .to(flightSagaExchange())
                .with(RabbitConstants.CANCEL_RESERVATION_BY_FLIGHT_CMD_QUEUE);
    }

    @Bean
    public Binding bindFlightComplete() {
        return BindingBuilder.bind(flightCompleteCmdQueue())
                .to(flightSagaExchange())
                .with(RabbitConstants.COMPLETE_FLIGHT_CMD_QUEUE);
    }

    @Bean
    public Binding bindFlightCompleted() {
        return BindingBuilder.bind(flightCompletedEventQueue())
                .to(flightSagaExchange())
                .with(RabbitConstants.FLIGHT_COMPLETED_RESP_QUEUE);
    }

    @Bean
    public Binding bindCompleteReservationsByFlightCmd(
            @Qualifier("completeReservationsByFlightCmdQueue") Queue q) {
        return BindingBuilder.bind(q)
                .to(flightSagaExchange())
                .with(RabbitConstants.COMPLETE_RESERVATION_BY_FLIGHT_CMD_QUEUE);
    }

    @Bean
    public Binding bindFlightReservationsCompleted() {
        return BindingBuilder.bind(flightReservationsCompletedEventQueue())
                .to(flightSagaExchange())
                .with(RabbitConstants.FLIGHT_RESERVATIONS_COMPLETED_QUEUE);
    }

    @Bean
    public Binding bindCompensateCompleteFlight() {
        return BindingBuilder.bind(compensateCompleteFlight())
                .to(flightSagaExchange())
                .with(RabbitConstants.COMPENSATE_COMPLETE_FLIGHT_CMD_QUEUE);
    }
}
