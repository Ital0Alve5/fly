package com.dac.fly.clientservice.consumer;

import java.util.Objects;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.dac.fly.clientservice.dto.response.ClientResponseDTO;
import com.dac.fly.clientservice.publisher.ClientPublisher;
import com.dac.fly.clientservice.service.ClientService;
import com.dac.fly.shared.config.RabbitConstants;
import com.dac.fly.shared.dto.command.CreateClientCommandDto;

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
        boolean success;
        ClientResponseDTO response;

        try {
            response = clientService.createClient(cmd);
            success = Objects.nonNull(response);

            publisher.publishClientCreatedResponse(response.getCodigo(), cmd.email(), success);
        } catch (Exception e) {
            System.err.println("Erro ao criar cliente: " + e.getMessage());
        }
    }
}
