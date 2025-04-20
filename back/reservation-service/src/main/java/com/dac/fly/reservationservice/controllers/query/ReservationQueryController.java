package com.dac.fly.reservationservice.controllers.query;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dac.fly.reservationservice.dto.response.ApiResponse;
import com.dac.fly.reservationservice.dto.response.ReservationResponseDto;
import com.dac.fly.reservationservice.entity.query.Reserva;
import com.dac.fly.reservationservice.repository.query.ReservaQueryRepository;
import com.dac.fly.reservationservice.dto.factory.ReservationResponseFactory;

@RestController
@RequestMapping("/reservas")
public class ReservationQueryController {

    private final ReservaQueryRepository reservaQueryRepository;
    private final ReservationResponseFactory responseFactory;

    public ReservationQueryController(ReservaQueryRepository reservaQueryRepository,
            ReservationResponseFactory responseFactory) {
        this.reservaQueryRepository = reservaQueryRepository;
        this.responseFactory = responseFactory;
    }

    @GetMapping("/{codigoReserva}")
    public ResponseEntity<ApiResponse<ReservationResponseDto>> getReservationByCode(
            @PathVariable String codigoReserva) {
        try {
            Reserva reserva = reservaQueryRepository.findById(codigoReserva)
                    .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));

            ReservationResponseDto dto = responseFactory.fromQueryReserva(reserva);
            return ResponseEntity.ok(ApiResponse.success(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), 404));
        }
    }

    @GetMapping("/clientes/{codigoCliente}")
    public ResponseEntity<ApiResponse<List<ReservationResponseDto>>> getReservationByClientCode(
            @PathVariable Long codigoCliente) {
        try {
            List<Reserva> reservas = reservaQueryRepository.findBycodigoCliente(codigoCliente);
            List<ReservationResponseDto> response = reservas.stream()
                    .map(responseFactory::fromQueryReserva)
                    .toList();

            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Erro ao buscar reservas do cliente", 400));
        }
    }
}
