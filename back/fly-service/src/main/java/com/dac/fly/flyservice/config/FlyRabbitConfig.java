package com.dac.fly.flyservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
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
    public Queue cancelFlightQueue() {
        return new Queue(RabbitConstants.CANCEL_FLIGHT_CMD_QUEUE);
    }

    @Bean
    public Queue flightCancelledQueue() {
        return new Queue(RabbitConstants.FLIGHT_CANCELLED_RESP_QUEUE);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(RabbitConstants.EXCHANGE);
    }

    @Bean("flightCompleteCmdQueue")
    public Queue flightCompleteCmdQueue() {
        return new Queue(RabbitConstants.COMPLETE_FLIGHT_CMD_QUEUE, true);
    }

    @Bean("flightCompletedEventQueue")
    public Queue flightCompletedEventQueue() { return new Queue(RabbitConstants.FLIGHT_COMPLETED_RESP_QUEUE, true); }

    @Bean("compensateCompleteFlightCmdQueue")
    public Queue compensateCompleteFlight() {
        return new Queue(RabbitConstants.COMPENSATE_COMPLETE_FLIGHT_CMD_QUEUE, true);
    }

    @Bean
    public Binding bindUpdateSeatsCommand(
            @Qualifier("updateSeatsCommandQueue") Queue updateSeatsCommandQueue,
            TopicExchange exchange) {
        return BindingBuilder
                .bind(updateSeatsCommandQueue)
                .to(exchange)
                .with(RabbitConstants.UPDATE_SEATS_CMD_QUEUE);
    }

    @Bean
    public Binding bindCancelFlightCommand(
            @Qualifier("cancelFlightQueue") Queue cancelFlightQueue,
            TopicExchange exchange) {
        return BindingBuilder
                .bind(cancelFlightQueue)
                .to(exchange)
                .with(RabbitConstants.CANCEL_FLIGHT_CMD_QUEUE);
    }

    @Bean
    public Binding bindFlightCancelledResponse(
            @Qualifier("flightCancelledQueue") Queue flightCancelledQueue,
            TopicExchange exchange) {
        return BindingBuilder
                .bind(flightCancelledQueue)
                .to(exchange)
                .with(RabbitConstants.FLIGHT_CANCELLED_RESP_QUEUE);
    }

    @Bean
    public Queue compensateSeatsCmdQueue() {
        return new Queue(RabbitConstants.COMPENSATE_SEATS_CMD_QUEUE, true);
    }

    @Bean
    public Binding bindCompensateSeatsCmd(TopicExchange exchange) {
        return BindingBuilder.bind(compensateSeatsCmdQueue())
                .to(exchange)
                .with(RabbitConstants.COMPENSATE_SEATS_CMD_QUEUE);
    }

    @Bean
    public Queue rollbackSeatsCmdQueue() {
        return new Queue(RabbitConstants.ROLLBACK_SEATS_CMD_QUEUE, true);
    }

    @Bean
    public Binding bindRollbackSeatsCmd(TopicExchange exchange) {
        return BindingBuilder
                .bind(rollbackSeatsCmdQueue())
                .to(exchange)
                .with(RabbitConstants.ROLLBACK_SEATS_CMD_QUEUE);
    }

    @Bean
    public Queue compensateCancelFlightQueue() {
        return new Queue(RabbitConstants.COMPENSATE_CANCEL_FLIGHT_CMD_QUEUE, true);
    }

    @Bean
    public Binding bindCompensateCancelFlight(
            @Qualifier("compensateCancelFlightQueue") Queue q,
            TopicExchange ex) {
        return BindingBuilder
                .bind(q)
                .to(ex)
                .with(RabbitConstants.COMPENSATE_CANCEL_FLIGHT_CMD_QUEUE);
    }

    @Bean
    public Binding bindFlightComplete() {
        return BindingBuilder.bind(flightCompleteCmdQueue())
                .to(exchange())
                .with(RabbitConstants.COMPLETE_FLIGHT_CMD_QUEUE);
    }

    @Bean
    public Binding bindFlightCompleted() {
        return BindingBuilder.bind(flightCompletedEventQueue())
                .to(exchange())
                .with(RabbitConstants.FLIGHT_COMPLETED_RESP_QUEUE);
    }

    @Bean
    public Binding bindCompensateCompleteFlight() {
        return BindingBuilder.bind(compensateCompleteFlight())
                .to(exchange())
                .with(RabbitConstants.COMPENSATE_COMPLETE_FLIGHT_CMD_QUEUE);
    }
}
