package com.dac.fly.reservationservice.consumer;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.dac.fly.shared.config.RabbitConfig;
import com.dac.fly.shared.dto.command.CreateReservationCommand;
import com.dac.fly.shared.dto.events.SeatsUpdatedEvent;
import com.dac.fly.shared.dto.start.StartReservationDto;

@Component
public class SeatsUpdatedListener {

    private final RabbitTemplate rabbitTemplate;
    private final ConcurrentHashMap<String, StartReservationDto> store;

    public SeatsUpdatedListener(RabbitTemplate rabbitTemplate, ConcurrentHashMap<String, StartReservationDto> store) {
        this.rabbitTemplate = rabbitTemplate;
        this.store = store;
    }

    @RabbitListener(queues = RabbitConfig.SEATS_QUEUE)
    public void onSeatsUpdated(SeatsUpdatedEvent event) {
        StartReservationDto dto = store.get(event.reservationId());

        if (dto == null) {
            throw new RuntimeException("Reserva não encontrada para ID: " + event.reservationId());
        }

        if (event.success()) {
            CreateReservationCommand command = new CreateReservationCommand(
                    event.reservationId(),
                    dto.clientId(),
                    dto.flightCode(),
                    dto.seatsReserved());
            rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.RES_QUEUE, command);
        } else {
            // TODO: Tratar caso de falha na atualização de assentos
            System.out.println("Falha ao atualizar assentos para reserva: " + event.reservationId());
        }
    }
}
