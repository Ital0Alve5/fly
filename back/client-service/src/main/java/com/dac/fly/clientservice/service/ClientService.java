package com.dac.fly.clientservice.service;

import com.dac.fly.clientservice.dto.request.AddMilesRequestDTO;
import com.dac.fly.clientservice.dto.response.ClientResponseDTO;
import com.dac.fly.clientservice.dto.response.MilesResponseDTO;
import com.dac.fly.clientservice.dto.response.MilesStatementResponseDTO;
import com.dac.fly.clientservice.entity.Client;
import com.dac.fly.clientservice.entity.Transactions;
import com.dac.fly.clientservice.repository.ClientRepository;
import com.dac.fly.clientservice.repository.TransactionsRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final TransactionsRepository transactionsRepository;

    public ClientService(ClientRepository clientRepository, 
                       TransactionsRepository transactionsRepository) {
        this.clientRepository = clientRepository;
        this.transactionsRepository = transactionsRepository;
    }

    public ClientResponseDTO findByCodigo(Long codigo) {
        Client client = clientRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com código: " + codigo));
        
        return new ClientResponseDTO(client);
    }

    public MilesResponseDTO addMiles(Long codigoCliente, AddMilesRequestDTO request) {
        if (request.quantidade() == null || request.quantidade() <= 0) {
            throw new RuntimeException("Quantidade de milhas deve ser positiva");
        }

        Client client = clientRepository.findByCodigo(codigoCliente)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        Transactions transaction = new Transactions();
        transaction.setCliente(client);
        transaction.setQuantidadeMilhas(request.quantidade());
        transaction.setValorReais(calculateRealValue(request.quantidade()));
        transaction.setDescricao("COMPRA DE MILHAS");
        transaction.setTipo("ENTRADA");
        transactionsRepository.save(transaction);

        int novoSaldo = client.getSaldoMilhas() + request.quantidade();
        client.setSaldoMilhas(novoSaldo);
        clientRepository.save(client);

        return new MilesResponseDTO(client.getCodigo(), novoSaldo);
    }

    public MilesStatementResponseDTO getMilesStatement(Long codigoCliente) {
        Client client = clientRepository.findByCodigo(codigoCliente)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        List<MilesStatementResponseDTO.TransactionDTO> transactions = transactionsRepository
                .findByClienteCodigo(codigoCliente.intValue())
                .stream()
                .map(this::convertToTransactionDTO)
                .toList();

        return new MilesStatementResponseDTO(
                client.getCodigo(),
                client.getSaldoMilhas(),
                transactions
        );
    }

    private BigDecimal calculateRealValue(int miles) {
        return BigDecimal.valueOf(miles * 5);
    }

    private MilesStatementResponseDTO.TransactionDTO convertToTransactionDTO(Transactions transaction) {
        return new MilesStatementResponseDTO.TransactionDTO(
                transaction.getData(),
                transaction.getValorReais(),
                transaction.getQuantidadeMilhas(),
                transaction.getDescricao(),
                transaction.getCodigoReserva(),
                transaction.getTipo()
        );
    }
}