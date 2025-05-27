package com.dac.fly.reservationservice.dto;

import java.time.OffsetDateTime;

public class HistoryDto {
    private Long id;
    private String codigo_reserva;
    private OffsetDateTime data;
    private String estado_origem;
    private String estado_destino;

    public HistoryDto() {}

    public HistoryDto(Long id, String codigo_reserva, OffsetDateTime data, String estado_origem, String estado_destino) {
        this.id = id;
        this.codigo_reserva = codigo_reserva;
        this.data = data;
        this.estado_origem = estado_origem;
        this.estado_destino = estado_destino;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo_reserva() {
        return codigo_reserva;
    }

    public void setCodigo_reserva(String codigo_reserva) {
        this.codigo_reserva = codigo_reserva;
    }

    public OffsetDateTime getData() {
        return data;
    }

    public void setData(OffsetDateTime data) {
        this.data = data;
    }

    public String getEstado_origem() {
        return estado_origem;
    }

    public void setEstado_origem(String estado_origem) {
        this.estado_origem = estado_origem;
    }

    public String getEstado_destino() {
        return estado_destino;
    }

    public void setEstado_destino(String estado_destino) {
        this.estado_destino = estado_destino;
    }
}
