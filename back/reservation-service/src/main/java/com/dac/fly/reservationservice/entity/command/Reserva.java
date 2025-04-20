package com.dac.fly.reservationservice.entity.command;

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

    @Column(name = "codigo_voo")
    private String codigoVoo;

    @Column(name = "codigo_cliente")
    private Long codigoCliente;

    private Long estado;

    @Column(name = "data_reserva")
    private LocalDateTime dataReserva;

    @Column(name = "quantidade_poltronas")
    private Integer quantidadePoltronas;

    @Column(name = "valor_pago")
    private Double valorPago;

    @Column(name = "milhas_utilizadas")
    private Integer milhasUtilizadas;

    public Reserva() {
    }

    public Reserva(String codigo, String codigoVoo, Long codigoCliente, Long estado,
            LocalDateTime dataReserva, Double valorPago, Integer milhasUtilizadas) {
        this.codigo = codigo;
        this.codigoVoo = codigoVoo;
        this.codigoCliente = codigoCliente;
        this.estado = estado;
        this.dataReserva = dataReserva;
        this.valorPago = valorPago;
        this.milhasUtilizadas = milhasUtilizadas;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigoVoo() {
        return codigoVoo;
    }

    public void setCodigoVoo(String codigoVoo) {
        this.codigoVoo = codigoVoo;
    }

    public Long getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(Long codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public Long getEstado() {
        return estado;
    }

    public void setEstado(Long estado) {
        this.estado = estado;
    }

    public LocalDateTime getDataReserva() {
        return dataReserva;
    }

    public void setDataReserva(LocalDateTime dataReserva) {
        this.dataReserva = dataReserva;
    }

    public Double getValorPago() {
        return valorPago;
    }

    public void setValorPago(Double valorPago) {
        this.valorPago = valorPago;
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
}
