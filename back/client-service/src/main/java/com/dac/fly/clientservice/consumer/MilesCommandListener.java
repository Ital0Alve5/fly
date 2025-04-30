package com.dac.fly.clientservice.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.dac.fly.clientservice.publisher.MilesPublisher;
import com.dac.fly.clientservice.service.ClientService;
import com.dac.fly.shared.config.RabbitConstants;
import com.dac.fly.shared.dto.command.UpdateMilesCommand;

@Component
public class MilesCommandListener {

    private final ClientService clientService;
    private final MilesPublisher publisher;

    public MilesCommandListener(ClientService clientService, MilesPublisher publisher) {
        this.clientService = clientService;
        this.publisher = publisher;
    }

    @RabbitListener(queues = RabbitConstants.UPDATE_MILES_CMD_QUEUE)
    public void handleUpdateMiles(UpdateMilesCommand cmd) {
        boolean success = false;
        try {
            if (cmd.isCompensate()) {
                success = clientService.updateMiles(cmd.codigoCliente(), +cmd.milhasUtilizadas());
            } else {
                success = clientService.updateMiles(cmd.codigoCliente(), -cmd.milhasUtilizadas());
            }
        } catch (Exception e) {
            System.err.println("Erro ao atualizar milhas: " + e.getMessage());
        }

        publisher.publishMilesUpdateResponse(cmd.codigoReserva(), success);
    }
}
