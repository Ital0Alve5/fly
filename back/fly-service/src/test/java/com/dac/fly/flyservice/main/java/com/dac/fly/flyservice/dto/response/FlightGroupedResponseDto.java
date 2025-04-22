package com.dac.fly.flyservice.dto.response;

import java.util.List;

public record FlightGroupedResponseDto(
    String data,
    String origem,
    String destino,
    List<FlightDetailsResponseDto> voos
) {
    public static FlightGroupedResponseDto of(
        String data,
        String origem,
        String destino,
        List<FlightDetailsResponseDto> voos
    ) {
        return new FlightGroupedResponseDto(data, origem, destino, voos);
    }
}
