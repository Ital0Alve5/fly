package com.dac.fly.flyservice.entity;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "voos")
public class Voo {

    @Id
    @Column(length = 6)
    private String codigo;

    @Column(nullable = false)
    private OffsetDateTime data;

    @Column(name = "valor_passagem")
    private Double valorPassagem;

    @Column(name = "quantidade_poltronas_total")
    private Integer quantidadePoltronasTotal;

    @Column(name = "quantidade_poltronas_ocupadas")
    private Integer quantidadePoltronasOcupadas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado", referencedColumnName = "codigo")
    private Estado estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aeroporto_origem", referencedColumnName = "codigo")
    private Aeroporto aeroportoOrigem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aeroporto_destino", referencedColumnName = "codigo")
    private Aeroporto aeroportoDestino;

    public Voo() {
    }

    public Voo(String codigo, OffsetDateTime data, Double valorPassagem, Integer quantidadePoltronasTotal,
            Integer quantidadePoltronasOcupadas, Estado estado, Aeroporto aeroportoOrigem, Aeroporto aeroportoDestino) {
        this.codigo = codigo;
        this.data = data;
        this.valorPassagem = valorPassagem;
        this.quantidadePoltronasTotal = quantidadePoltronasTotal;
        this.quantidadePoltronasOcupadas = quantidadePoltronasOcupadas;
        this.estado = estado;
        this.aeroportoOrigem = aeroportoOrigem;
        this.aeroportoDestino = aeroportoDestino;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public OffsetDateTime getData() {
        return data;
    }

    public void setData(OffsetDateTime data) {
        this.data = data;
    }

    public Double getValorPassagem() {
        return valorPassagem;
    }

    public void setValorPassagem(Double valorPassagem) {
        this.valorPassagem = valorPassagem;
    }

    public Integer getQuantidadePoltronasTotal() {
        return quantidadePoltronasTotal;
    }

    public void setQuantidadePoltronasTotal(Integer quantidadePoltronasTotal) {
        this.quantidadePoltronasTotal = quantidadePoltronasTotal;
    }

    public Integer getQuantidadePoltronasOcupadas() {
        return quantidadePoltronasOcupadas;
    }

    public void setQuantidadePoltronasOcupadas(Integer quantidadePoltronasOcupadas) {
        this.quantidadePoltronasOcupadas = quantidadePoltronasOcupadas;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Aeroporto getAeroportoOrigem() {
        return aeroportoOrigem;
    }

    public void setAeroportoOrigem(Aeroporto aeroportoOrigem) {
        this.aeroportoOrigem = aeroportoOrigem;
    }

    public Aeroporto getAeroportoDestino() {
        return aeroportoDestino;
    }

    public void setAeroportoDestino(Aeroporto aeroportoDestino) {
        this.aeroportoDestino = aeroportoDestino;
    }
}
