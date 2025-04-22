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

    public ClientResponseDTO(Client cliente) {
        this.codigo = cliente.getCodigo();
        this.cpf = cliente.getCpf();
        this.email = cliente.getEmail();
        this.nome = cliente.getNome();
        this.saldoMilhas = cliente.getSaldoMilhas();

        Address address = cliente.getEndereco();
        this.endereco = address != null ? new AddressDTO(
            address.getCep(),
            address.getUf(),
            address.getCidade(),
            address.getBairro(),
            address.getRua(),
            address.getNumero(),
            address.getComplemento()
        ) : null;
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

        public AddressDTO(String cep, String uf, String cidade, String bairro, String rua, String numero, String complemento) {
            this.cep = cep;
            this.uf = uf;
            this.cidade = cidade;
            this.bairro = bairro;
            this.rua = rua;
            this.numero = numero;
            this.complemento = complemento;
        }
    }
}