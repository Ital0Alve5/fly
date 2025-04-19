package com.dac.fly.reservationservice.dto.response;

import java.time.LocalDateTime;

import com.dac.fly.reservationservice.entity.query.Reserva;
import com.dac.fly.reservationservice.enums.ReservationStatusEnum;

public record ReservationDto(
                String codigo,
                LocalDateTime data,
                Double valor,
                Integer milhas_utilizadas,
                Integer quantidade_poltronas,
                Long codigo_cliente,
                ReservationStatusEnum estado) {

        public static ReservationDto from(Reserva r) {
                return new ReservationDto(
                                r.getCodigo(),
                                r.getDataReserva(),
                                r.getValor(),
                                r.getMilhasUtilizadas(),
                                r.getQuantidadePoltronas(),
                                r.getCodigoCliente(),
                                ReservationStatusEnum.valueOf(r.getEstado()));
        }
}
