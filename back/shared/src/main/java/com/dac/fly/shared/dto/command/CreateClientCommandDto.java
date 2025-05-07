package com.dac.fly.shared.dto.command;

public record CreateClientCommandDto(
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
    ) {
    }
}
