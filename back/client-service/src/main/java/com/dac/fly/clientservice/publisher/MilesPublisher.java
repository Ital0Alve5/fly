package com.dac.fly.clientservice.publisher;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.dac.fly.shared.config.RabbitConstants;
import com.dac.fly.shared.dto.events.MilesUpdatedEvent;

@Component
public class MilesPublisher {

    private final RabbitTemplate rabbit;

    public MilesPublisher(RabbitTemplate rabbit) {
        this.rabbit = rabbit;
    }

    public void publishMilesUpdateResponse(String codigoReserva, boolean success) {
        rabbit.convertAndSend(
                RabbitConstants.EXCHANGE,
                RabbitConstants.UPDATE_MILES_RESP_QUEUE,
                new MilesUpdatedEvent(codigoReserva, success));
    }
}
