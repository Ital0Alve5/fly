package com.dac.fly.reservationservice.dto.response;

import java.time.LocalDateTime;

import com.dac.fly.reservationservice.enums.ReservationStatusEnum;
import com.dac.fly.reservationservice.repository.command.EstadoRepository;

public record ReservationDto(
                String codigo,
                LocalDateTime data,
                Double valor,
                Integer milhas_utilizadas,
                Integer quantidade_poltronas,
                Long codigo_cliente,
                ReservationStatusEnum estado) {

        public static ReservationDto fromQuery(com.dac.fly.reservationservice.entity.query.Reserva r) {
                return new ReservationDto(
                                r.getCodigo(),
                                r.getDataReserva(),
                                r.getValor(),
                                r.getMilhasUtilizadas(),
                                r.getQuantidadePoltronas(),
                                r.getCodigoCliente(),
                                ReservationStatusEnum.valueOf(r.getEstado()));
        }

        public static ReservationDto fromCommand(com.dac.fly.reservationservice.entity.command.Reserva r,
                        ReservationStatusEnum estado) {
                return new ReservationDto(
                                r.getCodigo(),
                                r.getDataReserva(),
                                r.getValorPago(),
                                r.getMilhasUtilizadas(),
                                r.getQuantidadePoltronas(),
                                r.getCodigoCliente(),
                                estado);
        }
}
