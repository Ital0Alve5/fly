package com.dac.fly.reservationservice.consumer;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.dac.fly.shared.config.RabbitConfig;
import com.dac.fly.shared.dto.start.StartReservationDto;

@Component
public class ReservationSagaListener {

    private final ConcurrentHashMap<String, StartReservationDto> store;

    public ReservationSagaListener(ConcurrentHashMap<String, StartReservationDto> store) {
        this.store = store;
    }

    @RabbitListener(queues = RabbitConfig.START_QUEUE)
    public void handleStartReservation(StartReservationDto dto) {
        store.put(dto.reservationId(), dto);
    }
}
