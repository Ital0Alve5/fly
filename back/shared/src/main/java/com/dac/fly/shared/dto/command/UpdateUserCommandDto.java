package com.dac.fly.shared.dto.command;

public record UpdateUserCommandDto(Long codigoExterno, String nome, String email, String role, String senha) {
}
