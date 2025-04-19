package com.dac.fly.reservationservice.consumer;

import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.dac.fly.reservationservice.config.RabbitMQConfig;
import com.dac.fly.reservationservice.dto.HistoryDto;
import com.dac.fly.reservationservice.dto.ReservationDto;
import com.dac.fly.reservationservice.dto.events.ReservationUpdatedEventDto;
import com.dac.fly.reservationservice.entity.query.Reserva;
import com.dac.fly.reservationservice.enums.ReservationStatusEnum;
import com.dac.fly.reservationservice.repository.query.ReservaQueryRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ReservationConsumer {

    private final ReservaQueryRepository repository;
    private final ObjectMapper objectMapper;

    public ReservationConsumer(ReservaQueryRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_RESERVA_CRIADA)
    public void listenReservationCreated(@Payload ReservationDto message) throws JsonProcessingException {

        Reserva reserva = new Reserva();
        reserva.setCodigo(message.getCodigo());
        reserva.setDataReserva(message.getDataReserva());
        reserva.setMilhasUtilizadas(message.getMilhas_utilizadas());
        reserva.setQuantidadePoltronas(message.getQuantidade_poltronas());
        reserva.setCodigoCliente(message.getCodigo_cliente());
        reserva.setEstado(ReservationStatusEnum.CRIADA.name());
        reserva.setCodigoVoo(message.getCodigo_voo());
        reserva.setAeroportoOrigem(message.getCodigo_aeroporto_origem());
        reserva.setAeroportoDestino(message.getCodigo_aeroporto_destino());
        reserva.setValor(message.getValor());

        String historicoJson = objectMapper.writeValueAsString(message.getHistorico());

        reserva.setHistorico(historicoJson);

        repository.save(reserva);
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_RESERVA_CANCELADA)
    public void listenReservationCanceled(@Payload List<HistoryDto> history) throws JsonProcessingException {

        Reserva reservation = repository.findById(history.get(0).getCodigo_reserva())
                .orElseThrow(
                        () -> new RuntimeException("reserva não encontrada no banco query!"));

        reservation.setEstado(ReservationStatusEnum.CANCELADA.name());

        String historicoJson = objectMapper.writeValueAsString(history);

        reservation.setHistorico(historicoJson);

        repository.save(reservation);
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_RESERVA_ATUALIZADA)
    public void listenReservationUpdated(@Payload ReservationUpdatedEventDto event) throws JsonProcessingException {
        Reserva reservation = repository.findById(event.codigo())
                .orElseThrow(
                        () -> new RuntimeException("reserva não encontrada no banco query!"));

        reservation.setEstado(event.estado().name());

        String historicoJson = objectMapper.writeValueAsString(event.historico());

        reservation.setHistorico(historicoJson);

        repository.save(reservation);
    }

}
