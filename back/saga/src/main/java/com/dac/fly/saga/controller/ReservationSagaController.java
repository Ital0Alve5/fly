package com.dac.fly.saga.controller;

import com.dac.fly.shared.config.RabbitConfig;
import com.dac.fly.shared.dto.start.StartReservationDto;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sagas")
public class ReservationSagaController {
    private final RabbitTemplate rabbit;

    public ReservationSagaController(RabbitTemplate rabbit) {
        this.rabbit = rabbit;
    }

    @PostMapping("/reservations")
    public String startSaga(@RequestBody StartReservationDto dto) {
        rabbit.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.START_QUEUE, dto);
        return "Saga iniciada para reserva " + dto.reservationId();
    }
}
