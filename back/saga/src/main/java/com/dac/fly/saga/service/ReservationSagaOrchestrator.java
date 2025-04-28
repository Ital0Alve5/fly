package com.dac.fly.saga.service;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.dac.fly.shared.config.RabbitConfig;
import com.dac.fly.shared.dto.command.CreateReservationCommand;
import com.dac.fly.shared.dto.command.DeductMilesCommand;
import com.dac.fly.shared.dto.command.UpdateSeatsCommand;
import com.dac.fly.shared.dto.events.MilesDeductedEvent;
import com.dac.fly.shared.dto.events.SeatsUpdatedEvent;
import com.dac.fly.shared.dto.start.StartReservationDto;

@Service
public class ReservationSagaOrchestrator {

    private final RabbitTemplate rabbit;
    private final ConcurrentHashMap<String, StartReservationDto> store = new ConcurrentHashMap<>();

    public ReservationSagaOrchestrator(RabbitTemplate rabbit) {
        this.rabbit = rabbit;
    }

    @RabbitListener(queues = RabbitConfig.START_QUEUE)
    public void onStart(StartReservationDto dto) {
        store.put(dto.reservationId(), dto);
        if (dto.usedMiles() != null && dto.usedMiles() > 0) {
            var cmd = new DeductMilesCommand(dto.reservationId(), dto.clientId(), dto.usedMiles());
            rabbit.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.MILES_QUEUE, cmd);
        } else {
            var cmd = new UpdateSeatsCommand(dto.reservationId(), dto.flightCode(), dto.seatsReserved());
            rabbit.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.SEATS_QUEUE, cmd);
        }
    }

    @RabbitListener(queues = RabbitConfig.MILES_QUEUE)
    public void onMilesDeducted(MilesDeductedEvent evt) {
        var dto = store.get(evt.reservationId());
        if (dto == null) {
            return;
        }

        if (evt.success()) {
            var cmd = new UpdateSeatsCommand(evt.reservationId(), dto.flightCode(), dto.seatsReserved());
            rabbit.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.SEATS_QUEUE, cmd);
        } else {
            var cmd = new CreateReservationCommand(evt.reservationId(), dto.clientId(), dto.flightCode(),
                    dto.seatsReserved());
            rabbit.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.RES_QUEUE, cmd);

            store.remove(evt.reservationId());
        }
    }

    @RabbitListener(queues = RabbitConfig.SEATS_QUEUE)
    public void onSeatsUpdated(SeatsUpdatedEvent evt) {
        var dto = store.get(evt.reservationId());
        if (dto == null) {
            return;
        }

        if (evt.success()) {
            var cmd = new CreateReservationCommand(evt.reservationId(), dto.clientId(), dto.flightCode(),
                    dto.seatsReserved());
            rabbit.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.RES_QUEUE, cmd);
        } else {
            System.err.println("Falha na reserva de assentos para reservaId: " + evt.reservationId());
        }

        store.remove(evt.reservationId());
    }
}
