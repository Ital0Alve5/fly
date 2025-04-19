package com.dac.fly.reservationservice.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dac.fly.reservationservice.dto.ReservationDto;
import com.dac.fly.reservationservice.service.ReservationService;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/reservas")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<Void> criarReserva(@RequestBody ReservationDto dto) {
        reservationService.createReservation(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{codigoReserva}")
    public ResponseEntity<com.dac.fly.reservationservice.dto.response.ReservationDto> getReservationByCode(
            @PathVariable String codigoReserva) {
        try {
            com.dac.fly.reservationservice.dto.response.ReservationDto reservation = reservationService
                    .getReservationByCode(codigoReserva);

            return ResponseEntity.ok().body(reservation);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/clientes/{codigoCliente}")
    public ResponseEntity<List<com.dac.fly.reservationservice.dto.response.ReservationDto>> getReservationByClientCode(
            @PathVariable Long codigoCliente) {
        try {
            List<com.dac.fly.reservationservice.dto.response.ReservationDto> reservations = reservationService
                    .getReservationByClientCode(codigoCliente);
            return ResponseEntity.ok().body(reservations);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
