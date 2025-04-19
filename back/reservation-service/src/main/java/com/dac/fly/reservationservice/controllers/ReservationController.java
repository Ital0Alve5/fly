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
import com.dac.fly.reservationservice.dto.response.ClientReservationsDto;
import com.dac.fly.reservationservice.service.ReservationService;

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

    @GetMapping("/clientes/{codigoCliente}")
    public ResponseEntity<List<ClientReservationsDto>> getReservationByClientCode(@PathVariable Long codigoCliente) {
        try {
            List<ClientReservationsDto> reservations = reservationService.getReservationByClientCode(codigoCliente);
            return ResponseEntity.ok().body(reservations);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
