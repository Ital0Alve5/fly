package com.dac.fly.flyservice.publisher;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.dac.fly.shared.config.RabbitConstants;
import com.dac.fly.shared.dto.events.SeatsUpdatedEvent;

@Component
public class SeatsPublisher {
    private final RabbitTemplate rabbit;

    public SeatsPublisher(RabbitTemplate rabbit) {
        this.rabbit = rabbit;
    }

    public void publishSeatsUpdated(SeatsUpdatedEvent event) {
        rabbit.convertAndSend(RabbitConstants.EXCHANGE, RabbitConstants.UPDATE_SEATS_RESP_QUEUE, event);
    }
}
