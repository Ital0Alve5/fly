package com.dac.fly.reservationservice.controllers.command;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dac.fly.reservationservice.dto.ReservationDto;
import com.dac.fly.reservationservice.service.command.ReservationCommandService;

@RestController
@RequestMapping("/reservas")
public class ReservationCommandController {

    private final ReservationCommandService reservationService;

    public ReservationCommandController(ReservationCommandService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<Void> criarReserva(@RequestBody ReservationDto dto) {
        reservationService.createReservation(dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{codigoReserva}")
    public ResponseEntity<ReservationDto> getReservationByCode(@PathVariable String codigoReserva) {
        try {
            reservationService.cancelReservationByCode(codigoReserva);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

}
