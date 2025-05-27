package com.dac.fly.flyservice.dto.response;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import com.dac.fly.flyservice.entity.Aeroporto;
import com.dac.fly.flyservice.entity.Voo;
import com.fasterxml.jackson.annotation.JsonFormat;

public record FlightDetailsResponseDto(
        String codigo,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "America/Sao_Paulo") OffsetDateTime data,
        Double valor_passagem,
        Integer quantidade_poltronas_total,
        Integer quantidade_poltronas_ocupadas,
        String estado,
        AeroportoResponseDto aeroporto_origem,
        AeroportoResponseDto aeroporto_destino) {

    public static FlightDetailsResponseDto fromEntity(Voo voo) {
        Aeroporto origem = voo.getAeroportoOrigem();
        Aeroporto destino = voo.getAeroportoDestino();

        return new FlightDetailsResponseDto(
                voo.getCodigo(),
                voo.getData().withOffsetSameInstant(ZoneOffset.of("-03:00")),
                voo.getValorPassagem(),
                voo.getQuantidadePoltronasTotal(),
                voo.getQuantidadePoltronasOcupadas(),
                voo.getEstado().getNome().toString(),
                new AeroportoResponseDto(origem.getCodigo(), origem.getNome(), origem.getCidade(), origem.getUf()),
                new AeroportoResponseDto(destino.getCodigo(), destino.getNome(), destino.getCidade(), destino.getUf()));
    }

}
