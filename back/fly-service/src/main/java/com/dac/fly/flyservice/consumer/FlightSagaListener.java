package com.dac.fly.flyservice.consumer;

import com.dac.fly.shared.dto.command.CompleteFlightDto;
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

    public FlightSagaListener(FlightService service, FlightPublisher publisher) {
        this.service = service;
        this.publisher = publisher;
    }

    @RabbitListener(queues = RabbitConstants.CANCEL_FLIGHT_CMD_QUEUE)
    public void handleCancelFlight(CancelFlightDto cmd) {
        try {
            var flightResponse = service.updateStatus(cmd.codigo(), FlightStatusEnum.CANCELADO);
            publisher.publishFlightCancelled(flightResponse);
        } catch (Exception e) {
            System.err.println("Erro ao cancelar voo " + cmd.codigo() + ": " + e.getMessage());
        }
    }

    @RabbitListener(queues = RabbitConstants.COMPLETE_FLIGHT_CMD_QUEUE)
    public void handleCompleteFlight(CompleteFlightDto cmd) {
        try {
            var flightResponse = service.updateStatus(cmd.codigo(), FlightStatusEnum.REALIZADO);
            publisher.publishFlightCompleted(flightResponse);
        } catch (Exception e) {
            System.err.println("Erro ao completar voo " + cmd.codigo() + ": " + e.getMessage());
        }
    }
}
