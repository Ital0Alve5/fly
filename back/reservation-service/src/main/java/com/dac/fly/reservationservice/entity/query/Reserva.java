package com.dac.fly.reservationservice.entity.query;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "reserva")
public class Reserva {

    @Id
    private String codigo;

    @Column(name = "data_reserva")
    private LocalDateTime dataReserva;

    @Column(name = "milhas_utilizadas")
    private Integer milhasUtilizadas;

    @Column(name = "quantidade_poltronas")
    private Integer quantidadePoltronas;

    @Column(name = "codigo_cliente")
    private Long codigoCliente;

    private String estado;

    @Column(name = "codigo_voo")
    private String codigoVoo;

    @Column(name = "aeroporto_origem")
    private String aeroportoOrigem;

    @Column(name = "aeroporto_destino")
    private String aeroportoDestino;

    private Double valor;

    @Column(name = "historico")
    private String historico;

    public Reserva() {
    }

    public Reserva(String codigo, LocalDateTime dataReserva, LocalDateTime dataVoo,
            Integer milhasUtilizadas, Integer quantidadePoltronas, Long codigoCliente,
            String estado, String aeroportoOrigem, String aeroportoDestino, Double valor) {
        this.codigo = codigo;
        this.dataReserva = dataReserva;
        this.milhasUtilizadas = milhasUtilizadas;
        this.quantidadePoltronas = quantidadePoltronas;
        this.codigoCliente = codigoCliente;
        this.estado = estado;
        this.aeroportoOrigem = aeroportoOrigem;
        this.aeroportoDestino = aeroportoDestino;
        this.valor = valor;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public LocalDateTime getDataReserva() {
        return dataReserva;
    }

    public void setDataReserva(LocalDateTime dataReserva) {
        this.dataReserva = dataReserva;
    }

    public Integer getMilhasUtilizadas() {
        return milhasUtilizadas;
    }

    public void setMilhasUtilizadas(Integer milhasUtilizadas) {
        this.milhasUtilizadas = milhasUtilizadas;
    }

    public Integer getQuantidadePoltronas() {
        return quantidadePoltronas;
    }

    public void setQuantidadePoltronas(Integer quantidadePoltronas) {
        this.quantidadePoltronas = quantidadePoltronas;
    }

    public Long getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(Long codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getAeroportoOrigem() {
        return aeroportoOrigem;
    }

    public void setAeroportoOrigem(String aeroportoOrigem) {
        this.aeroportoOrigem = aeroportoOrigem;
    }

    public String getAeroportoDestino() {
        return aeroportoDestino;
    }

    public void setAeroportoDestino(String aeroportoDestino) {
        this.aeroportoDestino = aeroportoDestino;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getHistorico() {
        return historico;
    }

    public void setHistorico(String historico) {
        this.historico = historico;
    }
    
    public String getCodigoVoo() {
        return codigoVoo;
    }

    public void setCodigoVoo(String codigoVoo) {
        this.codigoVoo = codigoVoo;
    }
}
