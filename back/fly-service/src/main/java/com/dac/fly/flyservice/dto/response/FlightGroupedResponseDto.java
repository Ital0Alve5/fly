package com.dac.fly.flyservice.dto.response;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public record FlightGroupedResponseDto(
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "America/Sao_Paulo") OffsetDateTime data,
        String origem,
        String destino,
        List<FlightDetailsResponseDto> voos) {
    public static FlightGroupedResponseDto of(
            @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "America/Sao_Paulo") OffsetDateTime data,
            String origem,
            String destino,
            List<FlightDetailsResponseDto> voos) {
        return new FlightGroupedResponseDto(data, origem, destino, voos);
    }
}
