package com.dac.fly.reservationservice.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dac.fly.reservationservice.dto.ReservationDto;
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
}
