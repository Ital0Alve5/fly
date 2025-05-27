package com.dac.fly.flyservice.dto.request;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record CreateNewFlightRequestDto(
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "America/Sao_Paulo") OffsetDateTime data,
        Double valor_passagem,
        Integer quantidade_poltronas_total,
        Integer quantidade_poltronas_ocupadas,
        String codigo_aeroporto_origem,
        String codigo_aeroporto_destino) {
}
