package com.dac.fly.reservationservice.entity.command;

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

    @Column(name = "codigo_voo")
    private String codigoVoo;

    @Column(name = "codigo_cliente")
    private Long codigoCliente;

    private String status;

    @Column(name = "data_reserva")
    private LocalDateTime dataReserva;

    @Column(name = "valor_pago")
    private Double valorPago;

    @Column(name = "milhas_utilizadas")
    private Integer milhasUtilizadas;
}
