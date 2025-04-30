package com.dac.fly.reservationservice.publisher;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.dac.fly.shared.config.RabbitConstants;
import com.dac.fly.shared.dto.events.FlightReservationsCancelledEventDto;

@Component
public class FlightReservationPublisher {

    private final RabbitTemplate rabbit;

    public FlightReservationPublisher(RabbitTemplate rabbit) {
        this.rabbit = rabbit;
    }

    public void publishCancelledFlightReservations(FlightReservationsCancelledEventDto evt) {
        rabbit.convertAndSend(
                RabbitConstants.EXCHANGE,
                RabbitConstants.FLIGHT_RESERVATIONS_CANCELLED_QUEUE,
                evt);
    }
}
