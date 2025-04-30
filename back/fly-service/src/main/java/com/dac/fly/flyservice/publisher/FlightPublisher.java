package com.dac.fly.flyservice.publisher;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.dac.fly.shared.config.RabbitConstants;
import com.dac.fly.shared.dto.response.CancelledFlightResponseDto;

@Component
public class FlightPublisher {
    private final RabbitTemplate rabbit;

    public FlightPublisher(RabbitTemplate rabbit) {
        this.rabbit = rabbit;
    }

    public void publishFlightCancelled(CancelledFlightResponseDto dto) {
        rabbit.convertAndSend(RabbitConstants.EXCHANGE, RabbitConstants.FLIGHT_CANCELLED_QUEUE, dto);
    }
}
