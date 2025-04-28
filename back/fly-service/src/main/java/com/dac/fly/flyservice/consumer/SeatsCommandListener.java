package com.dac.fly.flyservice.consumer;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.dac.fly.flyservice.service.FlightService;
import com.dac.fly.shared.config.RabbitConfig;
import com.dac.fly.shared.dto.command.UpdateSeatsCommand;
import com.dac.fly.shared.dto.events.SeatsUpdatedEvent;

@Component
public class SeatsCommandListener {

    private final FlightService flightService;
    private final RabbitTemplate rabbit;
    private final Set<String> processedReservations = ConcurrentHashMap.newKeySet();

    public SeatsCommandListener(FlightService flightService, RabbitTemplate rabbit) {
        this.flightService = flightService;
        this.rabbit = rabbit;
    }

    @RabbitListener(queues = RabbitConfig.SEATS_QUEUE)
    public void handleUpdateSeats(UpdateSeatsCommand cmd) {
        boolean success = false;
        try {
            if (!processedReservations.contains(cmd.reservationId())) {
                success = flightService.updateSeats(cmd.flightCode(), cmd.seatsReserved());
                processedReservations.add(cmd.reservationId());
            } else {
                success = true; 
            }
        } catch (Exception e) {
            System.err.println("Erro ao atualizar assentos: " + e.getMessage());
        }
        SeatsUpdatedEvent event = new SeatsUpdatedEvent(cmd.reservationId(), success);
        rabbit.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.SEATS_QUEUE, event);
    }
}
