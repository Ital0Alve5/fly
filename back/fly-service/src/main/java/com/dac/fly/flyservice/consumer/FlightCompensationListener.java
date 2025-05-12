package com.dac.fly.flyservice.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.dac.fly.flyservice.enums.FlightStatusEnum;
import com.dac.fly.flyservice.service.FlightService;
import com.dac.fly.shared.config.RabbitConstants;
import com.dac.fly.shared.dto.command.CompensateCancelFlightCommand;

@Component
public class FlightCompensationListener {

    private final FlightService flightService;

    public FlightCompensationListener(FlightService flightService) {
        this.flightService = flightService;
    }

    @RabbitListener(queues = RabbitConstants.COMPENSATE_CANCEL_FLIGHT_CMD_QUEUE)
    public void handleCompensateCancelFlight(CompensateCancelFlightCommand cmd) {
        try {
            FlightStatusEnum previous = FlightStatusEnum.valueOf(cmd.estadoAnterior());

            flightService.updateStatus(
                    cmd.flightCode(),
                    previous);

        } catch (IllegalArgumentException iae) {
            System.err.printf("Estado anterior inválido para compensação de voo %s: %s%n",
                    cmd.flightCode(), cmd.estadoAnterior());
            throw iae;
        } catch (Exception e) {
            System.err.printf("Erro ao compensar cancelamento de voo %s: %s%n",
                    cmd.flightCode(), e.getMessage());
            throw e;
        }
    }
}
