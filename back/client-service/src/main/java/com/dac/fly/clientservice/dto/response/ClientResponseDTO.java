package com.dac.fly.clientservice.dto.response;

import com.dac.fly.clientservice.entity.Address;
import com.dac.fly.clientservice.entity.Client;
import lombok.Getter;

@Getter
public class ClientResponseDTO {
    private final Long codigo;
    private final String cpf;
    private final String email;
    private final String nome;
    private final Integer saldoMilhas;
    private final AddressDTO endereco;

    public ClientResponseDTO(Client client) {
        this.codigo = client.getCodigo();
        this.cpf = client.getCpf();
        this.email = client.getEmail();
        this.nome = client.getNome();
        this.saldoMilhas = client.getSaldoMilhas();
        this.endereco = client.getEndereco() != null ? new AddressDTO(client.getEndereco()) : null;
    }

    @Getter
    public static class AddressDTO {
        private final String cep;
        private final String uf;
        private final String cidade;
        private final String bairro;
        private final String rua;
        private final String numero;
        private final String complemento;

        public AddressDTO(Address address) {
            this.cep = address.getCep();
            this.uf = address.getUf();
            this.cidade = address.getCidade();
            this.bairro = address.getBairro();
            this.rua = address.getRua();
            this.numero = address.getNumero();
            this.complemento = address.getComplemento();
        }
    }
}