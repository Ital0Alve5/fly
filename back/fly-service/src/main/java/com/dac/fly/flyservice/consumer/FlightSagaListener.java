package com.dac.fly.flyservice.consumer;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.dac.fly.flyservice.enums.FlightStatusEnum;
import com.dac.fly.flyservice.publisher.FlightPublisher;
import com.dac.fly.flyservice.service.FlightService;
import com.dac.fly.shared.config.RabbitConstants;
import com.dac.fly.shared.dto.request.CancelFlightDto;

@Service
public class FlightSagaListener {

    private final FlightService service;
    private final FlightPublisher publisher;
    private final Set<String> processedFlights = ConcurrentHashMap.newKeySet();

    public FlightSagaListener(FlightService service, FlightPublisher publisher) {
        this.service = service;
        this.publisher = publisher;
    }

    @RabbitListener(queues = RabbitConstants.CANCEL_FLIGHT_QUEUE)
    public void handleCancelFlight(CancelFlightDto cmd) {
        try {
            if (!processedFlights.contains(cmd.codigo())) {
                var flightResponse = service.updateStatus(cmd.codigo(), FlightStatusEnum.CANCELADO);
                processedFlights.add(cmd.codigo());

                publisher.publishFlightCancelled(flightResponse);
            }
        } catch (Exception e) {
            System.err.println("Erro ao cancelar voo " + cmd.codigo() + ": " + e.getMessage());
        }
    }
}
