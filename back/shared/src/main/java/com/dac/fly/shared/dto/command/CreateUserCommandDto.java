package com.dac.fly.shared.dto.command;

public record CreateUserCommandDto(String nome, String email, String role, String password) {
}
