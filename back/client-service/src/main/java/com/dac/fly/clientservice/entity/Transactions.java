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
    private BigDecimal valor_reais;

    private Integer quantidade_milhas;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    private String codigo_reserva;

    private String tipo;

    public Transactions() {
    }

    public Transactions(Integer id, Client cliente, LocalDateTime data, BigDecimal valor_reais, Integer quantidade_milhas, String descricao, String codigo_reserva, String tipo) {
        this.id = id;
        this.cliente = cliente;
        this.data = data;
        this.valor_reais = valor_reais;
        this.quantidade_milhas = quantidade_milhas;
        this.descricao = descricao;
        this.codigo_reserva = codigo_reserva;
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

    public BigDecimal getvalor_reais() {
        return valor_reais;
    }

    public void setvalor_reais(BigDecimal valor_reais) {
        this.valor_reais = valor_reais;
    }

    public Integer getquantidade_milhas() {
        return quantidade_milhas;
    }

    public void setquantidade_milhas(Integer quantidade_milhas) {
        this.quantidade_milhas = quantidade_milhas;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getcodigo_reserva() {
        return codigo_reserva;
    }

    public void setcodigo_reserva(String codigo_reserva) {
        this.codigo_reserva = codigo_reserva;
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
        transaction.setquantidade_milhas(miles);
        transaction.setvalor_reais(calculateRealValue(miles));
        transaction.setDescricao(description);
        transaction.setTipo("ENTRADA");
        return transaction;
    }

    public static Transactions createDebitTransaction(Client client, Integer miles, String reservationCode, String description) {
        Transactions transaction = new Transactions();
        transaction.setCliente(client);
        transaction.setquantidade_milhas(miles);
        transaction.setDescricao(description);
        transaction.setcodigo_reserva(reservationCode);
        transaction.setTipo("SAIDA");
        return transaction;
    }

    private static BigDecimal calculateRealValue(Integer miles) {
        return BigDecimal.valueOf(miles * 5);
    }
}
