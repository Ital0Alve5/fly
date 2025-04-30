package com.dac.fly.reservationservice.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.dac.fly.reservationservice.service.command.ReservationCommandService;
import com.dac.fly.shared.config.RabbitConstants;
import com.dac.fly.shared.dto.command.CancelReservationCommand;
import com.dac.fly.shared.dto.response.CanceledReservationResponseDto;

@Component
public class CancelReservationConsumer {

    private final RabbitTemplate rabbit;
    private final ReservationCommandService reservationCommandService;

    public CancelReservationConsumer(
            RabbitTemplate rabbit,
            ReservationCommandService reservationCommandService) {
        this.rabbit = rabbit;
        this.reservationCommandService = reservationCommandService;
    }

    @RabbitListener(queues = RabbitConstants.CANCEL_QUEUE)
    public void handle(CancelReservationCommand cmd) {
        var resp = reservationCommandService.cancelReservationByCode(cmd.codigo());

        var canceledDto = new CanceledReservationResponseDto(
                resp.codigo(),
                resp.data(),
                resp.valor(),
                resp.milhas_utilizadas(),
                resp.quantidade_poltronas(),
                resp.codigo_cliente(),
                resp.estado(),
                resp.codigo_voo(),
                resp.codigo_aeroporto_origem(),
                resp.codigo_aeroporto_destino());

        rabbit.convertAndSend(
                RabbitConstants.EXCHANGE,
                RabbitConstants.CANCELED_QUEUE,
                canceledDto);
    }
}
