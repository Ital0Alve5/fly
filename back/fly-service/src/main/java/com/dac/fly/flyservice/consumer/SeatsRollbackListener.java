package com.dac.fly.flyservice.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.dac.fly.flyservice.service.FlightService;
import com.dac.fly.shared.config.RabbitConstants;
import com.dac.fly.shared.dto.command.UpdateSeatsCommand;
import com.dac.fly.shared.dto.events.SeatsUpdatedEvent;

@Component
public class SeatsRollbackListener {

    private final FlightService flightService;
    private final RabbitTemplate rabbit;

    public SeatsRollbackListener(FlightService flightService, RabbitTemplate rabbit) {
        this.flightService = flightService;
        this.rabbit = rabbit;
    }

    @RabbitListener(queues = RabbitConstants.ROLLBACK_SEATS_CMD_QUEUE)
    public void handleRollbackSeats(UpdateSeatsCommand cmd) {
        boolean success = false;
        try {
            success = flightService.updateSeats(cmd.codigoVoo(), cmd.quantidadePoltronas());
        } catch (Exception e) {
            throw new RuntimeException(
                    "Falha no rollback de assentos para reserva " + cmd.codigoReserva());
        } finally {
            rabbit.convertAndSend(
                    RabbitConstants.EXCHANGE,
                    RabbitConstants.UPDATE_SEATS_RESP_QUEUE,
                    new SeatsUpdatedEvent(cmd.codigoReserva(), success));
        }
    }
}
