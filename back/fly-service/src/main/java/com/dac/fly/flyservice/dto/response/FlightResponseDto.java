package com.dac.fly.flyservice.dto.response;

import java.time.OffsetDateTime;

import com.dac.fly.flyservice.enums.FlightStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;

public record FlightResponseDto(
                String codigo,
                @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "America/Sao_Paulo") OffsetDateTime data,
                Double valor_passagem,
                Integer quantidade_poltronas_total,
                Integer quantidade_poltronas_ocupadas,
                FlightStatusEnum estado,
                String codigo_aeroporto_origem,
                String codigo_aeroporto_destino) {
}
