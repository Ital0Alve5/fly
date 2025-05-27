package com.dac.fly.shared.dto.command;

public record CreateEmployeeCommandDto(String cpf, String email, String nome, String telefone) {
}
