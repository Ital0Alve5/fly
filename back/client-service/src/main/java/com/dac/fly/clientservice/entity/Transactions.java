package com.dac.fly.clientservice.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transacoes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "codigo_cliente", nullable = false, foreignKey = @ForeignKey(name = "fk_cliente"))
    private Client cliente;

    @Column(nullable = false)
    private LocalDateTime data = LocalDateTime.now();

    @Column(name = "valor_reais")
    private BigDecimal valorReais;

    private Integer quantidadeMilhas;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    private String codigoReserva;

    private String tipo;

public static Transactions createCreditTransaction(Client client, Integer miles, String description) {
    Transactions transaction = new Transactions();
    transaction.setCliente(client);
    transaction.setQuantidadeMilhas(miles);
    transaction.setValorReais(calculateRealValue(miles));
    transaction.setDescricao(description);
    transaction.setTipo("ENTRADA");
    return transaction;
}

public static Transactions createDebitTransaction(Client client, Integer miles, String reservationCode, String description) {
    Transactions transaction = new Transactions();
    transaction.setCliente(client);
    transaction.setQuantidadeMilhas(miles);
    transaction.setDescricao(description);
    transaction.setCodigoReserva(reservationCode);
    transaction.setTipo("SAIDA");
    return transaction;
}

private static BigDecimal calculateRealValue(Integer miles) {
    return BigDecimal.valueOf(miles * 5);
}
}