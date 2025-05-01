package com.dac.fly.reservationservice.publisher;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.dac.fly.reservationservice.config.RabbitMQConfig;
import com.dac.fly.shared.config.RabbitConstants;
import com.dac.fly.shared.dto.events.FlightReservationsCancelledEventDto;

@Component
public class FlightReservationPublisher {

    private final RabbitTemplate rabbit;
    private final DirectExchange internalExchange;

    public FlightReservationPublisher(RabbitTemplate rabbit,
            @Qualifier("internalExchange") DirectExchange internalExchange) {
        this.rabbit = rabbit;
        this.internalExchange = internalExchange;
    }

    public void publishCancelledFlightReservations(FlightReservationsCancelledEventDto evt) {

        rabbit.convertAndSend(
                RabbitConstants.EXCHANGE,
                RabbitConstants.FLIGHT_RESERVATIONS_CANCELLED_QUEUE,
                evt);
    }

    public void publishFlightReservationsCancelledForCqrs(FlightReservationsCancelledEventDto evt) {
        rabbit.convertAndSend(
            internalExchange.getName(),
            RabbitMQConfig.INTERNAL_CANCEL_FLIGHT_KEY,
            evt);
    }
    
}
