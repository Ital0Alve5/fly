package com.dac.fly.clientservice.controllers;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dac.fly.clientservice.dto.response.ClientResponseDTO;
import com.dac.fly.clientservice.entity.Client;
import com.dac.fly.clientservice.repository.ClientRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/clientes")
public class ClientController {

    private final ClientRepository clientRepository;


    @GetMapping("/{codigoCliente}")
    public ResponseEntity<ClientResponseDTO> findByCodigo(@PathVariable("codigoCliente") Long codigo) {
        Optional<Client> clientOpt = clientRepository.findByCodigo(codigo);

        if (clientOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Client client = clientOpt.get();
        
        return ResponseEntity.ok(new ClientResponseDTO(client));
    }
}
