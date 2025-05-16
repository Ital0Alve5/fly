package com.dac.fly.clientservice.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.dac.fly.clientservice.publisher.MilesPublisher;
import com.dac.fly.clientservice.service.ClientService;
import com.dac.fly.shared.config.RabbitConstants;
import com.dac.fly.shared.dto.command.UpdateMilesCommand;

@Component
public class MilesRollbackListener {

    private final ClientService clientService;
    private final MilesPublisher publisher;

    public MilesRollbackListener(ClientService clientService,
            MilesPublisher publisher) {
        this.clientService = clientService;
        this.publisher = publisher;
    }

    @RabbitListener(queues = RabbitConstants.ROLLBACK_MILES_CMD_QUEUE)
    public void handleRollbackMiles(UpdateMilesCommand cmd) {
        boolean success = false;
        try {
            success = clientService.updateMiles(
                    cmd.codigoCliente(),
                    -cmd.milhasUtilizadas(), cmd.codigo_reserva());

        } catch (Exception e) {
            throw new RuntimeException(
                    "Falha ao rollback de milhas para reserva " + cmd.codigo_reserva());
        } finally {
            publisher.publishMilesUpdateResponse(cmd.codigo_reserva(), success);
        }
    }
}
