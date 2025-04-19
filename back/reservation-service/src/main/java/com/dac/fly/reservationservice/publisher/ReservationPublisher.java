package com.dac.fly.reservationservice.publisher;

import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.dac.fly.reservationservice.config.RabbitMQConfig;
import com.dac.fly.reservationservice.dto.HistoryDto;
import com.dac.fly.reservationservice.dto.ReservationDto;
import com.dac.fly.reservationservice.dto.events.ReservationUpdatedEventDto;

@Component
public class ReservationPublisher {

    private final RabbitTemplate rabbitTemplate;

    public ReservationPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishCreatedReservation(ReservationDto payloadJson) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_RESERVA,
                RabbitMQConfig.ROUTING_KEY_RESERVA_CRIADA,
                payloadJson);
    }

    public void publishReservationCancelled(List<HistoryDto> history) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_RESERVA,
                RabbitMQConfig.ROUTING_KEY_RESERVA_CANCELADA,
                history);
    }

    public void publishReservationUpdated(ReservationUpdatedEventDto event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_RESERVA,
                RabbitMQConfig.ROUTING_KEY_RESERVA_ATUALIZADA,
                event);
    }

}
