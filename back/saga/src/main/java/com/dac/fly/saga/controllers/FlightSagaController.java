package com.dac.fly.saga.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dac.fly.saga.service.FlightSagaOrchestrator;
import com.dac.fly.shared.dto.events.CancelledFlightEventDto;
import com.dac.fly.shared.dto.response.ApiResponse;
import com.dac.fly.shared.dto.response.CancelledFlightResponseDto;

@RestController
@RequestMapping("/sagas/voos")
public class FlightSagaController {

    private final FlightSagaOrchestrator orchestrator;

    public FlightSagaController(FlightSagaOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    @PatchMapping("cancela/{flightCode}")
    public ResponseEntity<ApiResponse<CancelledFlightResponseDto>> cancelFlight(
            @PathVariable String flightCode) {
        try {
            CancelledFlightResponseDto dto = orchestrator.cancelFlightSaga(flightCode);
            return ResponseEntity
                    .ok(ApiResponse.success(dto));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), HttpStatus.NOT_FOUND.value()));
        }
    }
}
