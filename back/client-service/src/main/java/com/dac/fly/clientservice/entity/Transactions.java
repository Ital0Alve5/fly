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

@Entity
@Table(name = "transacoes")
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

    public Transactions() {
    }

    public Transactions(Integer id, Client cliente, LocalDateTime data, BigDecimal valorReais, Integer quantidadeMilhas, String descricao, String codigoReserva, String tipo) {
        this.id = id;
        this.cliente = cliente;
        this.data = data;
        this.valorReais = valorReais;
        this.quantidadeMilhas = quantidadeMilhas;
        this.descricao = descricao;
        this.codigoReserva = codigoReserva;
        this.tipo = tipo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Client getCliente() {
        return cliente;
    }

    public void setCliente(Client cliente) {
        this.cliente = cliente;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public BigDecimal getValorReais() {
        return valorReais;
    }

    public void setValorReais(BigDecimal valorReais) {
        this.valorReais = valorReais;
    }

    public Integer getQuantidadeMilhas() {
        return quantidadeMilhas;
    }

    public void setQuantidadeMilhas(Integer quantidadeMilhas) {
        this.quantidadeMilhas = quantidadeMilhas;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCodigoReserva() {
        return codigoReserva;
    }

    public void setCodigoReserva(String codigoReserva) {
        this.codigoReserva = codigoReserva;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

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
