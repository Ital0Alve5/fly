package com.dac.fly.reservationservice.entity.command;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "historico")
public class Historico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_reserva", nullable = false)
    private String codigoReserva;

    @Column(name = "data", nullable = false)
    private LocalDateTime data;

    @Column(name = "estado_origem")
    private Long estadoOrigem;

    @Column(name = "estado_destino", nullable = false)
    private Long estadoDestino;

    public Historico() {
    }

    public Historico(String codigoReserva, LocalDateTime data, Long estadoOrigem, Long estadoDestino) {
        this.codigoReserva = codigoReserva;
        this.data = data;
        this.estadoOrigem = estadoOrigem;
        this.estadoDestino = estadoDestino;
    }

    public Long getId() {
        return id;
    }

    public String getCodigoReserva() {
        return codigoReserva;
    }

    public void setCodigoReserva(String codigoReserva) {
        this.codigoReserva = codigoReserva;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public Long getEstadoOrigem() {
        return estadoOrigem;
    }

    public void setEstadoOrigem(Long estadoOrigem) {
        this.estadoOrigem = estadoOrigem;
    }

    public Long getEstadoDestino() {
        return estadoDestino;
    }

    public void setEstadoDestino(Long estadoDestino) {
        this.estadoDestino = estadoDestino;
    }
}
