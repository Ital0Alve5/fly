package com.dac.fly.reservationservice.controllers.query;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dac.fly.reservationservice.dto.factory.ReservationResponseFactory;
import com.dac.fly.reservationservice.dto.response.ReservationResponseDto;
import com.dac.fly.reservationservice.entity.query.Reserva;
import com.dac.fly.reservationservice.repository.query.ReservaQueryRepository;
import com.dac.fly.shared.dto.response.ApiResponse;

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

    @GetMapping("/{codigo_reserva}")
    public ResponseEntity<ApiResponse<ReservationResponseDto>> getReservationByCode(
            @PathVariable String codigo_reserva) {
        try {
            Reserva reserva = reservaQueryRepository.findById(codigo_reserva)
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

    @GetMapping("/{codigo}/exists")
    public ResponseEntity<Boolean> existsByCode(@PathVariable("codigo") String codigo) {
        boolean exists = reservaQueryRepository.existsById(codigo);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/{codigo}/historico")
    public ResponseEntity<ApiResponse<String>> historico(@PathVariable("codigo") String codigo) {
        Reserva reserva = reservaQueryRepository.findById(codigo)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada: " + codigo));
        return ResponseEntity.ok(ApiResponse.success(reserva.getHistorico()));
    }
}
