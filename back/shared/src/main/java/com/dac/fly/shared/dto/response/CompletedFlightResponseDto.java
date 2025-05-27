package com.dac.fly.shared.dto.response;

public record CompletedFlightResponseDto(
        String codigo,
        String data,
        Double valor_passagem,
        Integer quantidade_poltronas_total,
        Integer quantidade_poltronas_ocupadas,
        String estado,
        String codigo_aeroporto_origem,
        String codigo_aeroporto_destino
) {
}
