package com.dac.fly.clientservice.consumer;

import com.dac.fly.clientservice.dto.response.ClientResponseDTO;
import com.dac.fly.clientservice.publisher.ClientPublisher;
import com.dac.fly.clientservice.service.ClientService;
import com.dac.fly.shared.config.RabbitConstants;
import com.dac.fly.shared.dto.command.CreateClientCommandDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ClientCommandListener {
    private final ClientService clientService;
    private final ClientPublisher publisher;

    public ClientCommandListener(ClientService clientService, ClientPublisher publisher) {
        this.clientService = clientService;
        this.publisher = publisher;
    }

    @RabbitListener(queues = RabbitConstants.CREATE_CLIENT_CMD_QUEUE)
    public void handleUpdateMiles(CreateClientCommandDto cmd) {
        boolean success = false;

        try {
            ClientResponseDTO response = clientService.createClient(cmd);
            success = Objects.nonNull(response);
        } catch (Exception e) {
            System.err.println("Erro ao criar cliente: " + e.getMessage());
        }

        publisher.publishClientCreatedResponse(cmd.email(), success);
    }
}
