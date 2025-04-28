package com.dac.fly.reservationservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.dac.fly.shared.config.RabbitConfig;

@Configuration
public class RabbitMQSagaConfigReservation {

    @Bean
    public Queue startSagaQueue() {
        return new Queue(RabbitConfig.START_QUEUE);
    }

    @Bean
    public Queue seatsSagaQueue() {
        return new Queue(RabbitConfig.SEATS_QUEUE);
    }

    @Bean
    public Queue reservationSagaQueue() {
        return new Queue(RabbitConfig.RES_QUEUE);
    }

    @Bean
    public TopicExchange sagaExchange() {
        return new TopicExchange(RabbitConfig.EXCHANGE);
    }

    @Bean
    public Binding bindStartSaga(Queue startSagaQueue, TopicExchange sagaExchange) {
        return BindingBuilder.bind(startSagaQueue).to(sagaExchange).with(RabbitConfig.START_QUEUE);
    }

    @Bean
    public Binding bindSeatsSaga(Queue seatsSagaQueue, TopicExchange sagaExchange) {
        return BindingBuilder.bind(seatsSagaQueue).to(sagaExchange).with(RabbitConfig.SEATS_QUEUE);
    }

    @Bean
    public Binding bindReservationSaga(Queue reservationSagaQueue, TopicExchange sagaExchange) {
        return BindingBuilder.bind(reservationSagaQueue).to(sagaExchange).with(RabbitConfig.RES_QUEUE);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverterSaga() {
        return new Jackson2JsonMessageConverter();
    }
}
