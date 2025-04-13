package com.dac.fly.reservationservice.entity.query;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reserva")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reserva {
    @Id
    private String codigo;

    @Column(name = "data_reserva")
    private LocalDateTime dataReserva;

    @Column(name = "data_voo")
    private LocalDateTime dataVoo;

    @Column(name = "milhas_utilizadas")
    private Integer milhasUtilizadas;

    @Column(name = "quantidade_poltronas")
    private Integer quantidadePoltronas;

    @Column(name = "codigo_cliente")
    private Long codigoCliente;

    private String estado;

    @Column(name = "aeroporto_origem")
    private String aeroportoOrigem;

    @Column(name = "aeroporto_destino")
    private String aeroportoDestino;

    private Double valor;
}
