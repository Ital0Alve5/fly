package com.dac.fly.clientservice.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "endereco")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10)
    private String cep;
    
    @Column(length = 2)
    private String uf;
    
    @Column(length = 100)
    private String cidade;
    
    @Column(length = 100)
    private String bairro;
    
    @Column(length = 100)
    private String rua;
    
    @Column(length = 10)
    private String numero;
    
    @Column(length = 100)
    private String complemento;
}