package com.dac.fly.clientservice.dto.response;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record MilesStatementResponseDTO(
    Long codigo,
    Integer saldo_milhas,
    List<TransactionDTO> transacoes
) {
    public record TransactionDTO(
        OffsetDateTime data,
        BigDecimal valor_reais,
        Integer quantidade_milhas,
        String descricao,
        String codigo_reserva,
        String tipo
    ) {}
}