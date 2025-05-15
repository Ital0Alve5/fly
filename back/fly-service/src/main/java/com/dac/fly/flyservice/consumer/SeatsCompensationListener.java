package com.dac.fly.flyservice.consumer;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.dac.fly.flyservice.service.FlightService;
import com.dac.fly.shared.config.RabbitConstants;
import com.dac.fly.shared.dto.command.UpdateSeatsCommand;
import com.dac.fly.shared.dto.events.SeatsUpdatedEvent;

@Component
public class SeatsCompensationListener {

    private final FlightService flightService;
    private final RabbitTemplate rabbit;

    public SeatsCompensationListener(FlightService flightService, RabbitTemplate rabbit) {
        this.flightService = flightService;
        this.rabbit = rabbit;
    }

    @RabbitListener(queues = RabbitConstants.COMPENSATE_SEATS_CMD_QUEUE)
    public void handleCompensateSeats(UpdateSeatsCommand cmd) {
        try {
            flightService.updateSeats(
                    cmd.codigoVoo(),
                    -cmd.quantidade_poltronas());

            rabbit.convertAndSend(
                    RabbitConstants.EXCHANGE,
                    RabbitConstants.UPDATE_SEATS_RESP_QUEUE,
                    new SeatsUpdatedEvent(cmd.codigo_reserva(), true));

        } catch (AmqpException e) {
            rabbit.convertAndSend(
                    RabbitConstants.EXCHANGE,
                    RabbitConstants.UPDATE_SEATS_RESP_QUEUE,
                    new SeatsUpdatedEvent(cmd.codigo_reserva(), false));

            throw new RuntimeException("Falha na compensação dos assentos.", e);
        }
    }
}
