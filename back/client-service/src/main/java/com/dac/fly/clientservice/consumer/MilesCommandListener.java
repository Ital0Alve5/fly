package com.dac.fly.clientservice.consumer;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.dac.fly.clientservice.service.ClientService;
import com.dac.fly.shared.config.RabbitConfig;
import com.dac.fly.shared.dto.command.DeductMilesCommand;
import com.dac.fly.shared.dto.events.MilesDeductedEvent;

@Component
public class MilesCommandListener {

    private final ClientService clientService;
    private final RabbitTemplate rabbit;
    private final Set<String> processedReservations = ConcurrentHashMap.newKeySet();

    public MilesCommandListener(ClientService clientService, RabbitTemplate rabbit) {
        this.clientService = clientService;
        this.rabbit = rabbit;
    }

    @RabbitListener(queues = RabbitConfig.MILES_QUEUE)
    public void handleDeductMiles(DeductMilesCommand cmd) {
        boolean success = false;
        try {
            if (!processedReservations.contains(cmd.reservationId())) {
                success = clientService.deductMiles(cmd.clientId(), cmd.miles());
                processedReservations.add(cmd.reservationId());
            } else {
                success = true; 
            }
        } catch (Exception e) {
            System.err.println("Erro ao deduzir milhas: " + e.getMessage());
        }
        MilesDeductedEvent event = new MilesDeductedEvent(cmd.reservationId(), success);
        rabbit.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.MILES_QUEUE, event);
    }
}
