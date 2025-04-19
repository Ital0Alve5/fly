package com.dac.fly.reservationservice.controllers.command;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dac.fly.reservationservice.dto.ReservationDto;
import com.dac.fly.reservationservice.dto.request.ReservationUpdateStatusDto;
import com.dac.fly.reservationservice.service.command.ReservationCommandService;

@RestController
@RequestMapping("/reservas")
public class ReservationCommandController {

    private final ReservationCommandService reservationService;

    public ReservationCommandController(ReservationCommandService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<Void> createReservation(@RequestBody ReservationDto dto) {
        reservationService.createReservation(dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{codigoReserva}")
    public ResponseEntity<ReservationDto> cancelReservationByCode(@PathVariable String codigoReserva) {
        try {
            reservationService.cancelReservationByCode(codigoReserva);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PatchMapping("/{codigoReserva}/estado")
    public ResponseEntity<ReservationDto> updateReservationStatus(@PathVariable String codigoReserva,
            @RequestBody ReservationUpdateStatusDto newStatus) {
        try {
            reservationService.updateReservationStatusByCode(codigoReserva, newStatus);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
