package com.dac.fly.clientservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "endereco")
@Data
@NoArgsConstructor
@AllArgsConstructor
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