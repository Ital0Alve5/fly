package com.dac.fly.clientservice.dto.response;

import com.dac.fly.clientservice.entity.Address;
import com.dac.fly.clientservice.entity.Client;

public class ClientResponseDTO {

    private final Long codigo;
    private final String cpf;
    private final String email;
    private final String nome;
    private final Integer saldo_milhas;
    private final AddressDTO endereco;

    public ClientResponseDTO(Client client) {
        this.codigo = client.getCodigo();
        this.cpf = client.getCpf();
        this.email = client.getEmail();
        this.nome = client.getNome();
        this.saldo_milhas = client.getsaldo_milhas();
        this.endereco = client.getEndereco() != null ? new AddressDTO(client.getEndereco()) : null;
    }

    public Long getCodigo() {
        return codigo;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }

    public String getNome() {
        return nome;
    }

    public Integer getsaldo_milhas() {
        return saldo_milhas;
    }

    public AddressDTO getEndereco() {
        return endereco;
    }

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

        public String getCep() {
            return cep;
        }

        public String getUf() {
            return uf;
        }

        public String getCidade() {
            return cidade;
        }

        public String getBairro() {
            return bairro;
        }

        public String getRua() {
            return rua;
        }

        public String getNumero() {
            return numero;
        }

        public String getComplemento() {
            return complemento;
        }
    }
}
