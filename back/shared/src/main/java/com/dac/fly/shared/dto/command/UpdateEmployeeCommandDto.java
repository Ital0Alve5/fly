package com.dac.fly.shared.dto.command;

public record UpdateEmployeeCommandDto(Long codigo, String cpf, String email, String nome, String telefone, String senha) {
}
