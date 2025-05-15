package com.dac.fly.flyservice.service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.dac.fly.flyservice.enums.FlightStatusEnum;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.dac.fly.flyservice.dto.request.CreateNewFlightRequestDto;
import com.dac.fly.flyservice.dto.response.FlightDetailsResponseDto;
import com.dac.fly.flyservice.dto.response.FlightGroupedResponseDto;
import com.dac.fly.flyservice.dto.response.FlightResponseDto;
import com.dac.fly.flyservice.entity.Aeroporto;
import com.dac.fly.flyservice.entity.Estado;
import com.dac.fly.flyservice.entity.Voo;
import com.dac.fly.flyservice.repository.AeroportoRepository;
import com.dac.fly.flyservice.repository.EstadoRepository;
import com.dac.fly.flyservice.repository.VooRepository;
import com.dac.fly.flyservice.util.FlightCodeGenerator;
import com.dac.fly.shared.dto.events.CancelledFlightEventDto;
import com.dac.fly.shared.dto.response.ApiResponse;

import jakarta.transaction.Transactional;

@Service
public class FlightService {

        private final VooRepository vooRepository;
        private final EstadoRepository estadoRepository;
        private final AeroportoRepository aeroportoRepository;

        public FlightService(VooRepository vooRepository,
                        EstadoRepository estadoRepository,
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
                                .map(v -> ResponseEntity
                                                .ok(ApiResponse.success(FlightDetailsResponseDto.fromEntity(v))))
                                .orElseGet(() -> ResponseEntity.status(404)
                                                .body(ApiResponse.error("Voo não encontrado", 404)));
        }

        public ResponseEntity<ApiResponse<String>> findEstadoByCode(String codigo) {
                return vooRepository.findById(codigo)
                        .map(v -> ResponseEntity
                                .ok(ApiResponse.success(v.getEstado().getNome().toString())))
                        .orElseGet(() -> ResponseEntity.status(404)
                                .body(ApiResponse.error("Voo não encontrado", 404)));
        }

        public ResponseEntity<ApiResponse<FlightResponseDto>> create(CreateNewFlightRequestDto dto) {
                Optional<Aeroporto> origem = aeroportoRepository.findById(dto.codigo_aeroporto_origem());
                Optional<Aeroporto> destino = aeroportoRepository.findById(dto.codigo_aeroporto_destino());
                Optional<Estado> estado = estadoRepository.findByNome(FlightStatusEnum.CONFIRMADO);

                if (origem.isEmpty() || destino.isEmpty() || estado.isEmpty()) {
                        return ResponseEntity.badRequest()
                                        .body(ApiResponse.error("Dados inválidos para criar voo", 400));
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
                return ResponseEntity.status(201)
                                .body(ApiResponse.success(toDto(salvo)));
        }

        @Transactional
        public CancelledFlightEventDto updateStatus(
                        String codigo,
                        FlightStatusEnum novoStatus) {

                System.err.println(1);
                Voo voo = vooRepository.findById(codigo)
                                .orElseThrow(() -> new RuntimeException("Voo não encontrado: " + codigo));
                System.err.println(2);

                var oldState = voo.getEstado().getNome().toString();
                System.err.println(3);

                Estado newEstado = estadoRepository.findByNome(novoStatus)
                                .orElseThrow(() -> new RuntimeException("Estado não encontrado: " + novoStatus));
                System.err.println(4);

                voo.setEstado(newEstado);
                vooRepository.save(voo);
                System.err.println(5);

                return new CancelledFlightEventDto(
                                voo.getCodigo(),
                                voo.getData().toString(),
                                voo.getValorPassagem(),
                                voo.getQuantidadePoltronasTotal(),
                                voo.getQuantidadePoltronasOcupadas(),
                                oldState,
                                newEstado.getNome().name(),
                                voo.getAeroportoOrigem().getCodigo(),
                                voo.getAeroportoDestino().getCodigo());
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

        public boolean updateSeats(String flightCode, int delta) {
                Voo voo = vooRepository.findById(flightCode)
                                .orElseThrow(() -> new RuntimeException("Voo não encontrado: " + flightCode));

                int ocupadas = voo.getQuantidadePoltronasOcupadas() + delta;
                if (ocupadas < 0 || ocupadas > voo.getQuantidadePoltronasTotal()) {
                        return false;
                }

                voo.setQuantidadePoltronasOcupadas(ocupadas);
                vooRepository.save(voo);
                return true;
        }

        public ResponseEntity<ApiResponse<FlightGroupedResponseDto>> findByAirport(
                        OffsetDateTime data, String origem, String destino) {
                OffsetDateTime inicio = data.toLocalDate().atStartOfDay().atOffset(ZoneOffset.UTC);
                OffsetDateTime fim = inicio.plusYears(10);

                List<Voo> voos;

                if (origem != null && destino != null) {
                    voos = vooRepository.findByDataBetweenAndAeroportoOrigemCodigoAndAeroportoDestinoCodigo(
                            inicio, fim, origem, destino);
                } else if (origem != null) {
                    voos = vooRepository.findByDataBetweenAndAeroportoOrigemCodigo(
                            inicio, fim, origem);
                } else if (destino != null) {
                    voos = vooRepository.findByDataBetweenAndAeroportoDestinoCodigo(
                            inicio, fim, destino);
                } else {
                    voos = vooRepository.findByDataBetween(inicio, fim);
                }

                List<FlightDetailsResponseDto> detalhes = voos.stream()
                                .map(FlightDetailsResponseDto::fromEntity)
                                .collect(Collectors.toList());

                FlightGroupedResponseDto resposta = FlightGroupedResponseDto.of(
                                data.toString(), origem, destino, detalhes);
                return ResponseEntity.ok(ApiResponse.success(resposta));
        }

        public void revertFlightStatus(String codigo, String estadoAnterior) {
                Voo voo = vooRepository.findById(codigo)
                                .orElseThrow(() -> new RuntimeException("Voo não encontrado: " + codigo));

                FlightStatusEnum prevEnum = FlightStatusEnum.valueOf(estadoAnterior);
                Estado prevEstado = estadoRepository.findByNome(prevEnum)
                                .orElseThrow(() -> new RuntimeException(
                                                "Estado anterior não encontrado: " + estadoAnterior));

                voo.setEstado(prevEstado);
                vooRepository.save(voo);
        }

        public boolean existsByCodigo(String codigo) {
                return vooRepository.existsById(codigo);
        }
}
