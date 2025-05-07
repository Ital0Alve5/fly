package com.dac.fly.authservice.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "auth")
public class Auth {

    @Id
    private String id;
    private String email;
    private String senha;
    private String nome;
    private String role;
    private Long codigoExterno;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public Auth() {
    }

    public Auth(String email, String senha, String nome, String role, Long codigoExterno, LocalDateTime criadoEm,
            LocalDateTime atualizadoEm) {
        this.email = email;
        this.senha = senha;
        this.nome = nome;
        this.role = role;
        this.codigoExterno = codigoExterno;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }
}
