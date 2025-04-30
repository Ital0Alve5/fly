package com.dac.fly.reservationservice.controllers.command;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dac.fly.reservationservice.dto.request.ReservationUpdateStatusDto;
import com.dac.fly.reservationservice.dto.response.ReservationResponseDto;
import com.dac.fly.reservationservice.service.command.ReservationCommandService;
import com.dac.fly.shared.dto.response.ApiResponse;


@RestController
@RequestMapping("/reservas")
public class ReservationCommandController {

    private final ReservationCommandService reservationService;

    public ReservationCommandController(ReservationCommandService reservationService) {
        this.reservationService = reservationService;
    }

    @PatchMapping("/{codigoReserva}/estado")
    public ResponseEntity<ApiResponse<ReservationResponseDto>> updateReservationStatus(
            @PathVariable String codigoReserva,
            @RequestBody ReservationUpdateStatusDto newStatus) {
        try {
            ReservationResponseDto response = reservationService.updateReservationStatusByCode(codigoReserva,
                    newStatus);
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), 404));
        }
    }
}