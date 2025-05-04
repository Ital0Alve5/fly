package com.dac.fly.clientservice.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dac.fly.clientservice.dto.request.AddMilesRequestDTO;
import com.dac.fly.clientservice.dto.response.ApiResponse;
import com.dac.fly.clientservice.dto.response.ClientResponseDTO;
import com.dac.fly.clientservice.dto.response.MilesResponseDTO;
import com.dac.fly.clientservice.dto.response.MilesStatementResponseDTO;
import com.dac.fly.clientservice.service.ClientService;

@RestController
@RequestMapping("/clientes")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/{codigoCliente}")
    public ResponseEntity<ApiResponse<ClientResponseDTO>> getClientById(@PathVariable Long codigoCliente) {
        try {
            ClientResponseDTO client = clientService.findByCodigo(codigoCliente);
            return ResponseEntity.ok(ApiResponse.success(client));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage(), 400));
        }
    }

    @PutMapping("/{codigoCliente}/milhas")
    public ResponseEntity<ApiResponse<MilesResponseDTO>> addMiles(@PathVariable Long codigoCliente,
                                                                   @RequestBody AddMilesRequestDTO request) {
        try {
            MilesResponseDTO response = clientService.addMiles(codigoCliente, request);
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage(), 400));
        }
    }

    @GetMapping("/{codigoCliente}/milhas")
    public ResponseEntity<ApiResponse<MilesStatementResponseDTO>> getMilesStatement(@PathVariable Long codigoCliente) {
        try {
            MilesStatementResponseDTO statement = clientService.getMilesStatement(codigoCliente);
            return ResponseEntity.ok(ApiResponse.success(statement));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage(), 400));
        }
    }
}
