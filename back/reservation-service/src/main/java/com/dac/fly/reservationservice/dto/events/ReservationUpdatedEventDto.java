package com.dac.fly.reservationservice.dto.events;

import java.util.List;

import com.dac.fly.reservationservice.dto.HistoryDto;
import com.dac.fly.reservationservice.entity.command.Reserva;
import com.dac.fly.reservationservice.enums.ReservationStatusEnum;

public record ReservationUpdatedEventDto(
        String codigo,
        Long codigo_cliente,
        ReservationStatusEnum estado,
        List<HistoryDto> historico) {

    public static ReservationUpdatedEventDto fromCommand(Reserva r,
            ReservationStatusEnum estado, List<HistoryDto> history) {
        return new ReservationUpdatedEventDto(
                r.getCodigo(),
                r.getCodigoCliente(),
                estado,
                history);
    }

}
