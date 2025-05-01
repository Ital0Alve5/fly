package com.dac.fly.flyservice.entity;

import com.dac.fly.flyservice.enums.FlightStatusEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "estado")
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30, unique = true)
    private FlightStatusEnum nome;

    public Estado() {
    }

    public Estado(Long codigo, FlightStatusEnum nome) {
        this.codigo = codigo;
        this.nome = nome;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public FlightStatusEnum getNome() {
        return nome;
    }

    public void setNome(FlightStatusEnum nome) {
        this.nome = nome;
    }
}
