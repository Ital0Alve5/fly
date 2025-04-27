package com.dac.fly.flyservice.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "aeroportos")
public class Aeroporto {

    @Id
    @Column(length = 4)
    private String codigo;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, length = 100)
    private String cidade;

    @Column(nullable = false, length = 2)
    private String uf;

    public Aeroporto() {
    }

    public Aeroporto(String codigo, String nome, String cidade, String uf) {
        this.codigo = codigo;
        this.nome = nome;
        this.cidade = cidade;
        this.uf = uf;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }
}
