package com.dac.fly.reservationservice.dto.events;

import java.time.LocalDateTime;
import java.util.List;

import com.dac.fly.reservationservice.dto.HistoryDto;

public record ReservationCreatedEventDto(
        String codigo,
        Long codigo_cliente,
        LocalDateTime data_reserva,
        Double valor,
        Integer milhas_utilizadas,
        Integer quantidade_poltronas,
        String codigo_voo,
        String codigo_aeroporto_origem,
        String codigo_aeroporto_destino,
        List<HistoryDto> historico) {
    public static ReservationCreatedEventDto from(
            String codigo,
            Long codigo_cliente,
            LocalDateTime data_reserva,
            Double valor,
            Integer milhas_utilizadas,
            Integer quantidade_poltronas,
            String codigo_voo,
            String codigo_aeroporto_origem,
            String codigo_aeroporto_destino,
            List<HistoryDto> historico) {
        return new ReservationCreatedEventDto(
                codigo,
                codigo_cliente,
                data_reserva,
                valor,
                milhas_utilizadas,
                quantidade_poltronas,
                codigo_voo,
                codigo_aeroporto_origem,
                codigo_aeroporto_destino,
                historico);
    }
}
