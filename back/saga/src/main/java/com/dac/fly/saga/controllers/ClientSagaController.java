package com.dac.fly.saga.controllers;

import com.dac.fly.saga.service.ClientSagaOrchestrator;
import com.dac.fly.shared.dto.request.CreateClientRequestDto;
import com.dac.fly.shared.dto.response.ClientCreatedResponseDto;
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
    public ResponseEntity<ApiResponse<ClientCreatedResponseDto>> createClient(
            @RequestBody CreateClientRequestDto dto) {
        try {
            ClientCreatedResponseDto data = orchestrator.createClientSaga(dto);
            return ResponseEntity
                    .ok(ApiResponse.success(data));
        }catch (IllegalArgumentException e){
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(
                            e.getMessage(),
                            HttpStatus.CONFLICT.value()
                    ));
        }
    }
}
