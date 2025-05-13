package com.dac.fly.shared.dto.response;

public record ClientCreatedResponseDto(
        Long codigo,
        String cpf,
        String email,
        String nome,
        Integer saldoMilhas,
        AddressDTO endereco
) {
    public record AddressDTO(
            String cep,
            String uf,
            String cidade,
            String bairro,
            String rua,
            String numero,
            String complemento
    ) {}
}
