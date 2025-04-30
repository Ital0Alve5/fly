package com.dac.fly.flyservice.consumer;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
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
    private final Set<String> processedReservations = ConcurrentHashMap.newKeySet();

    public SeatsCommandListener(FlightService flightService, SeatsPublisher publisher) {
        this.flightService = flightService;
        this.publisher = publisher;
    }

    @RabbitListener(queues = RabbitConstants.UPDATE_SEATS_CMD_QUEUE)
    public void handleUpdateSeats(UpdateSeatsCommand cmd) {
        boolean success = false;

        try {
            if (cmd.isCompensate()) {
                success = flightService.updateSeats(cmd.codigoVoo(), +cmd.quantidadePoltronas());
            } else {
                if (!processedReservations.contains(cmd.codigoReserva())) {
                    success = flightService.updateSeats(cmd.codigoVoo(), -cmd.quantidadePoltronas());
                    processedReservations.add(cmd.codigoReserva());
                } else {
                    success = true;
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao processar assentos: " + e.getMessage());
        }

        publisher.publishSeatsUpdated(new SeatsUpdatedEvent(cmd.codigoReserva(), success));
    }
}
