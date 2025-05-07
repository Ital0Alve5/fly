package com.dac.fly.saga.controllers;

import com.dac.fly.saga.service.ClientSagaOrchestrator;
import com.dac.fly.shared.dto.request.CreateClientRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dac.fly.shared.dto.response.ApiResponse;

@RestController
@RequestMapping("/sagas/clientes")
public class ClientSagaController {

    private final ClientSagaOrchestrator orchestrator;

    public ClientSagaController(ClientSagaOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<Boolean>> createClient(
            @RequestBody CreateClientRequestDto dto) {
        try {
            orchestrator.createClientSaga(dto);
            return ResponseEntity
                    .ok(ApiResponse.success(true));
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), HttpStatus.NOT_FOUND.value()));
        }
    }
}
