package com.dac.fly.authservice.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "auth")
public class Auth {

    @Id
    private Long codigoUsuario;

    private String email;
    private String senha;
    private String nome;
    private String role;

    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
