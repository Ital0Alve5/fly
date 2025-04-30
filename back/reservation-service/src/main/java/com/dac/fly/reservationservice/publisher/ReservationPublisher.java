package com.dac.fly.reservationservice.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.dac.fly.shared.config.RabbitConstants;
import com.dac.fly.shared.dto.response.CanceledReservationResponseDto;
import com.dac.fly.shared.dto.response.CreatedReservationResponseDto;

@Component
public class ReservationPublisher {

    private static final Logger logger = LoggerFactory.getLogger(ReservationPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public ReservationPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishCreatedReservationToSaga(CreatedReservationResponseDto dto) {
        try {
            rabbitTemplate.convertAndSend(
                RabbitConstants.EXCHANGE,
                RabbitConstants.CREATED_QUEUE,
                dto);
            logger.info("Evento de criação de reserva (saga) publicado: {}", dto.codigo());
        } catch (AmqpException e) {
            logger.error("Erro ao publicar evento de criação de reserva (saga)", e);
            throw new RuntimeException("Falha ao publicar evento de reserva criada (saga)", e);
        }
    }

    public void publishCancelledReservationToSaga(CanceledReservationResponseDto dto) {
        try {
            rabbitTemplate.convertAndSend(
                RabbitConstants.EXCHANGE,
                RabbitConstants.CANCELED_QUEUE,
                dto);
            logger.info("Evento de cancelamento de reserva (saga) publicado: {}", dto.codigo());
        } catch (AmqpException e) {
            logger.error("Erro ao publicar evento de cancelamento de reserva (saga)", e);
            throw new RuntimeException("Falha ao publicar evento de reserva cancelada (saga)", e);
        }
    }
}
