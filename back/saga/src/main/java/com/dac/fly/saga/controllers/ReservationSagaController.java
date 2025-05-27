package com.dac.fly.saga.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dac.fly.saga.service.ReservationSagaOrchestrator;
import com.dac.fly.shared.dto.request.CreateReservationDto;
import com.dac.fly.shared.dto.response.ApiResponse;
import com.dac.fly.shared.dto.response.CancelledReservationResponseDto;
import com.dac.fly.shared.dto.response.CreatedReservationResponseDto;

@RestController
@RequestMapping("/sagas/reservations")
public class ReservationSagaController {
    private final ReservationSagaOrchestrator orchestrator;

    public ReservationSagaController(ReservationSagaOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CreatedReservationResponseDto>> startSaga(
            @RequestBody CreateReservationDto dto) {
        try {
            CreatedReservationResponseDto response = orchestrator.createReservation(dto);
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }

    @DeleteMapping("/cancela/{codigo}")
    public ResponseEntity<ApiResponse<CancelledReservationResponseDto>> cancelSaga(
        @PathVariable("codigo") String codigo) {
        try {
            CancelledReservationResponseDto dto = orchestrator.cancelReservationSaga(codigo);
            return ResponseEntity
                    .ok(ApiResponse.success(dto));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), 404));
        }
    }

}
