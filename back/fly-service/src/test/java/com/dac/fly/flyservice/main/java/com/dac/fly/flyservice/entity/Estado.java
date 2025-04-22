package com.dac.fly.flyservice.entity;

import com.dac.fly.flyservice.enums.ReservationStatusEnum;

import jakarta.persistence.*;

@Entity
@Table(name = "estado")
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30, unique = true)
    private ReservationStatusEnum nome;

    public Estado() {
    }

    public Estado(Long codigo, ReservationStatusEnum nome) {
        this.codigo = codigo;
        this.nome = nome;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public ReservationStatusEnum getNome() {
        return nome;
    }

    public void setNome(ReservationStatusEnum nome) {
        this.nome = nome;
    }
}
