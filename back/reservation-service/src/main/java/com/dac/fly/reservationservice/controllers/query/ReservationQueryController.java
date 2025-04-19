package com.dac.fly.reservationservice.controllers.query;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dac.fly.reservationservice.dto.response.ReservationDto;
import com.dac.fly.reservationservice.service.query.ReservationQueryService;

@RestController
@RequestMapping("/reservas")
public class ReservationQueryController {

    private final ReservationQueryService reservationService;

    public ReservationQueryController(ReservationQueryService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/{codigoReserva}")
    public ResponseEntity<ReservationDto> getReservationByCode(@PathVariable String codigoReserva) {
        try {
            ReservationDto reservation = reservationService.getReservationByCode(codigoReserva);
            return ResponseEntity.ok().body(reservation);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/clientes/{codigoCliente}")
    public ResponseEntity<List<ReservationDto>> getReservationByClientCode(@PathVariable Long codigoCliente) {
        try {
            List<ReservationDto> reservations = reservationService.getReservationByClientCode(codigoCliente);
            return ResponseEntity.ok().body(reservations);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
