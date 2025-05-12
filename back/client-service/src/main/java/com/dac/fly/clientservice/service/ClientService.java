package com.dac.fly.clientservice.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dac.fly.clientservice.dto.request.AddMilesRequestDTO;
import com.dac.fly.clientservice.dto.response.ClientResponseDTO;
import com.dac.fly.clientservice.dto.response.MilesResponseDTO;
import com.dac.fly.clientservice.dto.response.MilesStatementResponseDTO;
import com.dac.fly.clientservice.entity.Address;
import com.dac.fly.clientservice.entity.Client;
import com.dac.fly.clientservice.entity.Transactions;
import com.dac.fly.clientservice.repository.ClientRepository;
import com.dac.fly.clientservice.repository.TransactionsRepository;
import com.dac.fly.clientservice.util.DocumentUtils;
import com.dac.fly.shared.dto.command.CreateClientCommandDto;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final TransactionsRepository transactionsRepository;

    public ClientService(ClientRepository clientRepository,
            TransactionsRepository transactionsRepository) {
        this.clientRepository = clientRepository;
        this.transactionsRepository = transactionsRepository;
    }

    @Transactional
    public ClientResponseDTO createClient(CreateClientCommandDto request) {
        validateClientData(request);

        Address address = createAddress(request);
        Client client = createClientEntity(request, address);

        Client savedClient = clientRepository.save(client);
        return new ClientResponseDTO(savedClient);
    }

    public ClientResponseDTO findByCodigo(Long codigo) {
        Client client = clientRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com código: " + codigo));

        return new ClientResponseDTO(client);
    }

    @Transactional
    public MilesResponseDTO addMiles(Long codigoCliente, AddMilesRequestDTO request) {
        validateMilesRequest(request);

        Client client = clientRepository.findByCodigo(codigoCliente)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        Transactions transaction = createCreditTransaction(client, request.quantidade());
        transactionsRepository.save(transaction);

        updateClientMilesBalance(client, request.quantidade());

        return new MilesResponseDTO(client.getCodigo(), client.getSaldoMilhas());
    }

    public MilesStatementResponseDTO getMilesStatement(Long codigoCliente) {
        Client client = clientRepository.findByCodigo(codigoCliente)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        List<MilesStatementResponseDTO.TransactionDTO> transactions = getClientTransactions(codigoCliente);

        return new MilesStatementResponseDTO(
                client.getCodigo(),
                client.getSaldoMilhas(),
                transactions);
    }

    private void validateClientData(CreateClientCommandDto request) {
        if (!DocumentUtils.isValidCpf(request.cpf())) {
            throw new RuntimeException("CPF inválido");
        }

        if (clientRepository.existsByCpf(DocumentUtils.unformat(request.cpf()))) {
            throw new RuntimeException("CPF já cadastrado");
        }

        if (clientRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email já cadastrado");
        }
    }

    private Address createAddress(CreateClientCommandDto request) {
        Address address = new Address();
        address.setCep(DocumentUtils.formatCep(request.endereco().cep()));
        address.setUf(request.endereco().uf());
        address.setCidade(request.endereco().cidade());
        address.setBairro(request.endereco().bairro());
        address.setRua(request.endereco().rua());
        address.setNumero(request.endereco().numero());
        address.setComplemento(request.endereco().complemento());
        return address;
    }

    private Client createClientEntity(CreateClientCommandDto request, Address address) {
        Client client = new Client();
        client.setCpf(DocumentUtils.formatCpf(request.cpf()));
        client.setEmail(request.email());
        client.setNome(request.nome());
        Integer initialSaldo = request.saldoMilhas();
        client.setSaldoMilhas(initialSaldo != null ? initialSaldo : 0);
        client.setEndereco(address);
        return client;
    }

    private void validateMilesRequest(AddMilesRequestDTO request) {
        if (request.quantidade() == null || request.quantidade() <= 0) {
            throw new RuntimeException("Quantidade de milhas deve ser positiva");
        }
    }

    private Transactions createCreditTransaction(Client client, Integer miles) {
        Transactions transaction = new Transactions();
        transaction.setCliente(client);
        transaction.setQuantidadeMilhas(miles);
        transaction.setValorReais(calculateRealValue(miles));
        transaction.setDescricao("COMPRA DE MILHAS");
        transaction.setTipo("ENTRADA");
        return transaction;
    }

    private void updateClientMilesBalance(Client client, Integer miles) {
        client.setSaldoMilhas(client.getSaldoMilhas() + miles);
        clientRepository.save(client);
    }

    private List<MilesStatementResponseDTO.TransactionDTO> getClientTransactions(Long codigoCliente) {
        return transactionsRepository.findByClienteCodigo(codigoCliente)
                .stream()
                .map(this::convertToTransactionDTO)
                .toList();
    }

    private MilesStatementResponseDTO.TransactionDTO convertToTransactionDTO(Transactions transaction) {
        return new MilesStatementResponseDTO.TransactionDTO(
                transaction.getData(),
                transaction.getValorReais(),
                transaction.getQuantidadeMilhas(),
                transaction.getDescricao(),
                transaction.getCodigoReserva(),
                transaction.getTipo());
    }

    public boolean updateMiles(Long clientId, Integer miles) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado: " + clientId));

        if (miles < 0) {
            if (client.getSaldoMilhas() < Math.abs(miles)) {
                return false;
            }
            client.setSaldoMilhas(client.getSaldoMilhas() + miles);
            clientRepository.save(client);

            Transactions tx = Transactions.createDebitTransaction(
                    client,
                    Math.abs(miles),
                    "Reserva",
                    "Uso de milhas");
            tx.setValorReais(calculateRealValue(Math.abs(miles)));
            transactionsRepository.save(tx);
        } else {
            client.setSaldoMilhas(client.getSaldoMilhas() + miles);
            clientRepository.save(client);

            Transactions tx = createCreditTransaction(client, miles);
            tx.setDescricao("ESTORNO DE MILHAS POR CANCELAMENTO DE VOO");
            transactionsRepository.save(tx);
        }

        return true;
    }

    private BigDecimal calculateRealValue(Integer miles) {
        return BigDecimal.valueOf(miles * 5);
    }

    public boolean existsByCodigo(Long codigo) {
        return clientRepository.existsById(codigo);
    }

}