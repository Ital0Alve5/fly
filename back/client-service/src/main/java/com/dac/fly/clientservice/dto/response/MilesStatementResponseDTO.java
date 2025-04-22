package com.dac.fly.clientservice.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record MilesStatementResponseDTO(
    Long codigo,
    Integer saldoMilhas,
    List<TransactionDTO> transacoes
) {
    public record TransactionDTO(
        LocalDateTime data,
        BigDecimal valorReais,
        Integer quantidadeMilhas,
        String descricao,
        String codigoReserva,
        String tipo
    ) {}
}