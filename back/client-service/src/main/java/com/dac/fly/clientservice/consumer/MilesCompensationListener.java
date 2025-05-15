package com.dac.fly.clientservice.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.dac.fly.clientservice.service.ClientService;
import com.dac.fly.shared.config.RabbitConstants;
import com.dac.fly.shared.dto.command.UpdateMilesCommand;
import com.dac.fly.shared.dto.events.MilesUpdatedEvent;

@Component
public class MilesCompensationListener {

    private final ClientService clientService;
    private final RabbitTemplate rabbit;

    public MilesCompensationListener(ClientService clientService, RabbitTemplate rabbit) {
        this.clientService = clientService;
        this.rabbit = rabbit;
    }

    @RabbitListener(queues = RabbitConstants.COMPENSATE_MILES_CMD_QUEUE)
    public void handleCompensateMiles(UpdateMilesCommand cmd) {
        try {
            boolean success = clientService.updateMiles(
                    cmd.codigoCliente(),
                    cmd.milhasUtilizadas());

            rabbit.convertAndSend(
                    RabbitConstants.EXCHANGE,
                    RabbitConstants.UPDATE_MILES_RESP_QUEUE,
                    new MilesUpdatedEvent(cmd.codigo_reserva(), success));

        } catch (RuntimeException e) {
            rabbit.convertAndSend(
                    RabbitConstants.EXCHANGE,
                    RabbitConstants.UPDATE_MILES_RESP_QUEUE,
                    new MilesUpdatedEvent(cmd.codigo_reserva(), false));

            throw new RuntimeException("Falha na compensação das milhas", e);
        }
    }
}
