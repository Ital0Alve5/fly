package com.dac.fly.clientservice.dto.request;

public record AddMilesRequestDTO(
    Integer quantidade
) {
    public boolean isValid() {
        return quantidade != null && quantidade > 0;
    }
}