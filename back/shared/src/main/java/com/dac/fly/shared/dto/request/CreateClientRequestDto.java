package com.dac.fly.shared.dto.request;

public record CreateClientRequestDto(
        String cpf,
        String email,
        String nome,
        Integer saldo_milhas,
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