package com.dac.fly.flyservice.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.dac.fly.flyservice.publisher.SeatsPublisher;
import com.dac.fly.flyservice.service.FlightService;
import com.dac.fly.shared.config.RabbitConstants;
import com.dac.fly.shared.dto.command.UpdateSeatsCommand;
import com.dac.fly.shared.dto.events.SeatsUpdatedEvent;

@Service
public class SeatsCommandListener {

    private final FlightService flightService;
    private final SeatsPublisher publisher;

    public SeatsCommandListener(FlightService flightService, SeatsPublisher publisher, RabbitTemplate rabbit) {
        this.flightService = flightService;
        this.publisher = publisher;
    }

    @RabbitListener(queues = RabbitConstants.UPDATE_SEATS_CMD_QUEUE)
    public void handleUpdateSeats(UpdateSeatsCommand cmd) {

        try {
            boolean success;

            if (cmd.isCompensate()) {
                success = flightService.updateSeats(cmd.codigoVoo(), -cmd.quantidadePoltronas());
            } else {
                success = flightService.updateSeats(cmd.codigoVoo(), +cmd.quantidadePoltronas());
            }

            publisher.publishSeatsUpdated(new SeatsUpdatedEvent(cmd.codigoReserva(), success));

        } catch (Exception e) {
            System.err.println("Erro ao processar assentos: " + e.getMessage());

            throw e;
        }
    }
}
