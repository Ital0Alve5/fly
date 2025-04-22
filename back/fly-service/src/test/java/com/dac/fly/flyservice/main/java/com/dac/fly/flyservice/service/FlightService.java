package com.dac.fly.flyservice.service;

import com.dac.fly.flyservice.dto.request.CreateNewFlightRequestDto;
import com.dac.fly.flyservice.dto.request.UpdateFlightStatusRequestDto;
import com.dac.fly.flyservice.dto.response.ApiResponse;
import com.dac.fly.flyservice.dto.response.FlightDetailsResponseDto;
import com.dac.fly.flyservice.dto.response.FlightResponseDto;
import com.dac.fly.flyservice.dto.response.FlightGroupedResponseDto;
import com.dac.fly.flyservice.entity.*;
import com.dac.fly.flyservice.enums.ReservationStatusEnum;
import com.dac.fly.flyservice.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.dac.fly.flyservice.util.FlightCodeGenerator;

@Service
public class FlightService {

    private final VooRepository vooRepository;
    private final EstadoRepository estadoRepository;
    private final AeroportoRepository aeroportoRepository;

    public FlightService(VooRepository vooRepository, EstadoRepository estadoRepository,
            AeroportoRepository aeroportoRepository) {
        this.vooRepository = vooRepository;
        this.estadoRepository = estadoRepository;
        this.aeroportoRepository = aeroportoRepository;
    }

    public ResponseEntity<ApiResponse<List<FlightDetailsResponseDto>>> findAll(
            OffsetDateTime data,
            OffsetDateTime dataFim,
            String origem,
            String destino) {

        List<Voo> voos;

        if (data != null && dataFim != null) {
            voos = vooRepository.findByDataBetween(data, dataFim);
        } else if (data != null && origem != null && destino != null) {
            OffsetDateTime inicio = data.toLocalDate().atStartOfDay().atOffset(ZoneOffset.UTC);
            OffsetDateTime fim = inicio.plusDays(1);

            voos = vooRepository.findByDataBetweenAndAeroportoOrigemCodigoAndAeroportoDestinoCodigo(
                    inicio, fim, origem, destino);
        } else {
            voos = vooRepository.findAll();
        }

        List<FlightDetailsResponseDto> dtos = voos.stream()
                .map(FlightDetailsResponseDto::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(dtos));
    }

    public ResponseEntity<ApiResponse<FlightDetailsResponseDto>> findByCode(String codigo) {
        return vooRepository.findById(codigo)
                .map(voo -> ResponseEntity.ok(ApiResponse.success(FlightDetailsResponseDto.fromEntity(voo))))
                .orElse(ResponseEntity.status(404).body(ApiResponse.error("Voo não encontrado", 404)));
    }

    public ResponseEntity<ApiResponse<FlightResponseDto>> create(CreateNewFlightRequestDto dto) {
        Optional<Aeroporto> origem = aeroportoRepository.findById(dto.codigo_aeroporto_origem());
        Optional<Aeroporto> destino = aeroportoRepository.findById(dto.codigo_aeroporto_destino());
        Optional<Estado> estado = estadoRepository.findByNome(ReservationStatusEnum.CONFIRMADO);

        if (origem.isEmpty() || destino.isEmpty() || estado.isEmpty()) {
            return ResponseEntity.status(400).body(ApiResponse.error("Dados inválidos para criar voo", 400));
        }

        Voo voo = new Voo();
        voo.setCodigo(FlightCodeGenerator.generateReservationCode());
        voo.setData(OffsetDateTime.parse(dto.data()));
        voo.setValorPassagem(dto.valor_passagem());
        voo.setQuantidadePoltronasTotal(dto.quantidade_poltronas_total());
        voo.setQuantidadePoltronasOcupadas(dto.quantidade_poltronas_ocupadas());
        voo.setAeroportoOrigem(origem.get());
        voo.setAeroportoDestino(destino.get());
        voo.setEstado(estado.get());

        Voo salvo = vooRepository.save(voo);
        return ResponseEntity.status(201).body(ApiResponse.success(toDto(salvo)));
    }

    public ResponseEntity<ApiResponse<FlightResponseDto>> updateStatus(String codigo,
            UpdateFlightStatusRequestDto estadoRequest) {
        Optional<Voo> vooOpt = vooRepository.findById(codigo);
        Optional<Estado> estado = estadoRepository.findByNome(estadoRequest.estado());

        if (vooOpt.isEmpty()) {
            return ResponseEntity.status(404).body(ApiResponse.error("Voo não encontrado", 404));
        }

        if (estado.isEmpty()) {
            return ResponseEntity.status(400).body(ApiResponse.error("Estado inválido", 400));
        }

        Voo voo = vooOpt.get();
        voo.setEstado(estado.get());
        vooRepository.save(voo);

        return ResponseEntity.ok(ApiResponse.success(toDto(voo)));
    }

    private FlightResponseDto toDto(Voo voo) {
        return new FlightResponseDto(
                voo.getCodigo(),
                voo.getData().toString(),
                voo.getValorPassagem(),
                voo.getQuantidadePoltronasTotal(),
                voo.getQuantidadePoltronasOcupadas(),
                voo.getEstado().getNome(),
                voo.getAeroportoOrigem().getCodigo(),
                voo.getAeroportoDestino().getCodigo());
    }

    public ResponseEntity<ApiResponse<FlightGroupedResponseDto>> findByAirport(OffsetDateTime data, String origem,
            String destino) {
        OffsetDateTime inicio = data.toLocalDate().atStartOfDay().atOffset(ZoneOffset.UTC);
        OffsetDateTime fim = inicio.plusDays(1);

        List<Voo> voos = vooRepository.findByDataBetweenAndAeroportoOrigemCodigoAndAeroportoDestinoCodigo(
                inicio, fim, origem, destino);
        List<FlightDetailsResponseDto> detalhes = voos.stream()
                .map(FlightDetailsResponseDto::fromEntity)
                .toList();

        FlightGroupedResponseDto resposta = FlightGroupedResponseDto.of(
                data.toString(),
                origem,
                destino,
                detalhes);

        return ResponseEntity.ok(ApiResponse.success(resposta));
    }

}
