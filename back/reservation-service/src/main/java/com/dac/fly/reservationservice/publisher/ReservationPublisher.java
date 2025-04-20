package com.dac.fly.reservationservice.publisher;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.dac.fly.reservationservice.config.RabbitMQConfig;
import com.dac.fly.reservationservice.dto.HistoryDto;
import com.dac.fly.reservationservice.dto.events.ReservationCreatedEventDto;
import com.dac.fly.reservationservice.dto.events.ReservationUpdatedEventDto;

@Component
public class ReservationPublisher {

    private static final Logger logger = LoggerFactory.getLogger(ReservationPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public ReservationPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishCreatedReservation(ReservationCreatedEventDto event) {
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE_RESERVA,
                    RabbitMQConfig.ROUTING_KEY_RESERVA_CRIADA,
                    event);
            logger.info("Evento de criação de reserva publicado: {}", event.codigo());
        } catch (Exception e) {
            logger.error("Erro ao publicar evento de criação de reserva", e);
            throw new RuntimeException("Falha ao publicar evento de reserva criada", e);
        }
    }

    public void publishReservationCancelled(List<HistoryDto> history) {
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE_RESERVA,
                    RabbitMQConfig.ROUTING_KEY_RESERVA_CANCELADA,
                    history);
            logger.info("Evento de cancelamento de reserva publicado: {}", history.get(0).getCodigo_reserva());
        } catch (Exception e) {
            logger.error("Erro ao publicar evento de cancelamento de reserva", e);
            throw new RuntimeException("Falha ao publicar evento de reserva cancelada", e);
        }
    }

    public void publishReservationUpdated(ReservationUpdatedEventDto event) {
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE_RESERVA,
                    RabbitMQConfig.ROUTING_KEY_RESERVA_ATUALIZADA,
                    event);
            logger.info("Evento de atualização de reserva publicado: {}", event.codigo());
        } catch (Exception e) {
            logger.error("Erro ao publicar evento de atualização de reserva", e);
            throw new RuntimeException("Falha ao publicar evento de reserva atualizada", e);
        }
    }
}
