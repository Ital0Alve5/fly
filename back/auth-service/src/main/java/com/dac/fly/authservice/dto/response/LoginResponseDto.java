package com.dac.fly.authservice.dto.response;

public record LoginResponseDto(Long codigoExterno, String access_token, String token_type, String tipo) {
}
