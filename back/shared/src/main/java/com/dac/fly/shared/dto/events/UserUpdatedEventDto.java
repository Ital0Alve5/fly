package com.dac.fly.shared.dto.events;

public record UserUpdatedEventDto(Long codigoFuncionario, String email, Boolean success) {
}
