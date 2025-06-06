package com.dac.fly.clientservice.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    @Transactional(readOnly = true)
    public List<ClientResponseDTO> findAll() {
        return clientRepository.findAll()
                .stream()
                .map(ClientResponseDTO::new)
                .toList();
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

        return new MilesResponseDTO(client.getCodigo(), client.getsaldo_milhas());
    }

    public MilesStatementResponseDTO getMilesStatement(Long codigoCliente) {
        Client client = clientRepository.findByCodigo(codigoCliente)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        List<MilesStatementResponseDTO.TransactionDTO> transactions = getClientTransactions(codigoCliente);

        return new MilesStatementResponseDTO(
                client.getCodigo(),
                client.getsaldo_milhas(),
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
        client.setCpf(request.cpf());
        client.setEmail(request.email());
        client.setNome(request.nome());
        Integer initialSaldo = request.saldo_milhas();
        client.setsaldo_milhas(initialSaldo != null ? initialSaldo : 0);
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
        transaction.setquantidade_milhas(miles);
        transaction.setvalor_reais(calculateRealValue(miles));
        transaction.setDescricao("COMPRA DE MILHAS");
        transaction.setTipo("ENTRADA");
        return transaction;
    }

    private void updateClientMilesBalance(Client client, Integer miles) {
        client.setsaldo_milhas(client.getsaldo_milhas() + miles);
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
                transaction.getvalor_reais(),
                transaction.getquantidade_milhas(),
                transaction.getDescricao(),
                transaction.getcodigo_reserva(),
                transaction.getTipo());
    }

    public boolean updateMiles(Long clientId, Integer miles, String reservationCode, String description) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado: " + clientId));

        if (miles < 0) {
            if (client.getsaldo_milhas() < Math.abs(miles)) {
                return false;
            }
            client.setsaldo_milhas(client.getsaldo_milhas() + miles);
            clientRepository.save(client);

            Transactions tx = Transactions.createDebitTransaction(
                    client,
                    Math.abs(miles),
                    reservationCode,
                    Objects.nonNull(description) ? description : "Uso de milhas");
            tx.setvalor_reais(calculateRealValue(Math.abs(miles)));
            transactionsRepository.save(tx);
        } else {
            client.setsaldo_milhas(client.getsaldo_milhas() + miles);
            clientRepository.save(client);

            Transactions tx = createCreditTransaction(client, miles);
            tx.setcodigo_reserva(reservationCode);
            tx.setDescricao("EXTORNO DE MILHAS");
            transactionsRepository.save(tx);
        }

        return true;
    }

    public void deleteClient(Long clientCode) {
        Optional<Client> optionalClient = clientRepository.findById(clientCode);

        optionalClient.ifPresent(clientRepository::delete);
    }

    private BigDecimal calculateRealValue(Integer miles) {
        return BigDecimal.valueOf(miles * 5);
    }

    public boolean existsByCodigo(Long codigo) {
        return clientRepository.existsById(codigo);
    }

    public boolean existsByCpf(String cpf) {
        return clientRepository.existsByCpf(cpf);
    }
}