package com.dac.fly.shared.dto.command;

public record CreateUserCommandDto(String nome, String email, Long codigoExterno, String role, String password) {
}
