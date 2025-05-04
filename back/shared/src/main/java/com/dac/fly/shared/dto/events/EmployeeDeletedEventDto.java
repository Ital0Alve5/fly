package com.dac.fly.shared.dto.events;

public record EmployeeDeletedEventDto( Long codigo, String email, String cpf, String nome, String telefone, Boolean success) {
}
