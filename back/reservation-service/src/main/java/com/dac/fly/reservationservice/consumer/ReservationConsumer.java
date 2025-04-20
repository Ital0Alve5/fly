package com.dac.fly.reservationservice.consumer;

import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.dac.fly.reservationservice.config.RabbitMQConfig;
import com.dac.fly.reservationservice.dto.HistoryDto;
import com.dac.fly.reservationservice.dto.events.ReservationCreatedEventDto;
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
    public void listenReservationCreated(@Payload ReservationCreatedEventDto message) throws JsonProcessingException {
        if (message.codigo() == null || message.codigo_cliente() == null) {
            throw new IllegalArgumentException("Dados de reserva inválidos no evento de criação");
        }

        if (repository.existsById(message.codigo())) {
            return; 
        }

        Reserva reserva = new Reserva();
        reserva.setCodigo(message.codigo());
        reserva.setDataReserva(message.data_reserva());
        reserva.setMilhasUtilizadas(message.milhas_utilizadas());
        reserva.setQuantidadePoltronas(message.quantidade_poltronas());
        reserva.setCodigoCliente(message.codigo_cliente());
        reserva.setEstado(ReservationStatusEnum.CRIADA.toString());
        reserva.setCodigoVoo(message.codigo_voo());
        reserva.setAeroportoOrigem(message.codigo_aeroporto_origem());
        reserva.setAeroportoDestino(message.codigo_aeroporto_destino());
        reserva.setValor(message.valor());

        String historicoJson = objectMapper.writeValueAsString(message.historico());
        reserva.setHistorico(historicoJson);

        repository.save(reserva);
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_RESERVA_CANCELADA)
    public void listenReservationCanceled(@Payload List<HistoryDto> history) throws JsonProcessingException {
        if (history == null || history.isEmpty()) {
            throw new IllegalArgumentException("Histórico inválido no evento de cancelamento");
        }

        Reserva reservation = repository.findById(history.get(0).getCodigo_reserva())
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada no banco query!"));

        reservation.setEstado(ReservationStatusEnum.CANCELADA.toString());
        String historicoJson = objectMapper.writeValueAsString(history);
        reservation.setHistorico(historicoJson);

        repository.save(reservation);
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_RESERVA_ATUALIZADA)
    public void listenReservationUpdated(@Payload ReservationUpdatedEventDto event) throws JsonProcessingException {
        if (event.codigo() == null || event.estado() == null) {
            throw new IllegalArgumentException("Dados inválidos no evento de atualização");
        }

        Reserva reservation = repository.findById(event.codigo())
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada no banco query!"));

        reservation.setEstado(event.estado().toString());
        String historicoJson = objectMapper.writeValueAsString(event.historico());
        reservation.setHistorico(historicoJson);

        repository.save(reservation);
    }
}