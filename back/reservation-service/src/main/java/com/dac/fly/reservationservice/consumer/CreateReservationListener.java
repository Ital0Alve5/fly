package com.dac.fly.reservationservice.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.dac.fly.shared.config.RabbitConfig;
import com.dac.fly.shared.dto.command.CreateReservationCommand;
import com.dac.fly.reservationservice.service.ReservationSagaService;

@Component
public class CreateReservationListener {

    private final ReservationSagaService reservationSagaService;

    public CreateReservationListener(ReservationSagaService reservationSagaService) {
        this.reservationSagaService = reservationSagaService;
    }

    @RabbitListener(queues = RabbitConfig.RES_QUEUE)
    public void handleCreateReservation(CreateReservationCommand cmd) {
        reservationSagaService.createReservation(cmd);
    }
}
