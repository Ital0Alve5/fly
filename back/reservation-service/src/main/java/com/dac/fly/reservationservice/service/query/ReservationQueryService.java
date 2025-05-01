package com.dac.fly.reservationservice.service.query;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dac.fly.reservationservice.dto.HistoryDto;
import com.dac.fly.reservationservice.dto.factory.ReservationResponseFactory;
import com.dac.fly.reservationservice.dto.response.ReservationResponseDto;
import com.dac.fly.reservationservice.entity.command.Historico;
import com.dac.fly.reservationservice.entity.query.Reserva;
import com.dac.fly.reservationservice.repository.command.EstadoRepository;
import com.dac.fly.reservationservice.repository.command.HistoryRepository;
import com.dac.fly.reservationservice.repository.query.ReservaQueryRepository;
import com.dac.fly.shared.dto.response.CanceledReservationResponseDto;
import com.dac.fly.shared.dto.response.CreatedReservationResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ReservationQueryService {

    private final ReservaQueryRepository reservaQueryRepository;
    private final ReservationResponseFactory responseFactory;
    private final HistoryRepository historyRepository;
    private final EstadoRepository estadoRepository;
    private final ObjectMapper objectMapper;

    public ReservationQueryService(ReservaQueryRepository reservaQueryRepository,
            ReservationResponseFactory responseFactory, HistoryRepository historyRepository,
            EstadoRepository estadoRepository, ObjectMapper objectMapper) {
        this.reservaQueryRepository = reservaQueryRepository;
        this.responseFactory = responseFactory;
        this.historyRepository = historyRepository;
        this.estadoRepository = estadoRepository;
        this.objectMapper = objectMapper;
    }

    public List<ReservationResponseDto> getReservationByClientCode(Long clientCode) {
        List<Reserva> reservations = reservaQueryRepository.findBycodigoCliente(clientCode);
        return reservations.stream().map(responseFactory::fromQueryReserva).toList();
    }

    public ReservationResponseDto getReservationByCode(String code) {
        Reserva reservation = reservaQueryRepository.findById(code)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada: " + code));
        return responseFactory.fromQueryReserva(reservation);
    }

    public boolean saveCreatedReservation(CreatedReservationResponseDto evt) {
        try {
            if (reservaQueryRepository.existsById(evt.codigo())) {
                return updateHistory(evt.codigo());
            }

            Reserva view = new Reserva();
            view.setCodigo(evt.codigo());
            view.setDataReserva(evt.data());
            view.setValor(evt.valor());
            view.setMilhasUtilizadas(evt.milhas_utilizadas());
            view.setQuantidadePoltronas(evt.quantidade_poltronas());
            view.setCodigoCliente(evt.codigo_cliente());
            view.setEstado(evt.estado());
            view.setCodigoVoo(evt.codigo_voo());
            view.setAeroportoOrigem(evt.codigo_aeroporto_origem());
            view.setAeroportoDestino(evt.codigo_aeroporto_destino());

            view.setHistorico(buildHistoryJson(evt.codigo()));

            reservaQueryRepository.save(view);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean saveCanceledReservation(CanceledReservationResponseDto evt) {
        try {
            Reserva view = reservaQueryRepository.findById(evt.codigo())
                    .orElseThrow(() -> new RuntimeException(
                            "Projeção de reserva não encontrada: " + evt.codigo()));

            view.setEstado(evt.estado());

            view.setHistorico(buildHistoryJson(evt.codigo()));

            reservaQueryRepository.save(view);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String buildHistoryJson(String reservaCodigo) {
        List<Historico> historicos = historyRepository.findByCodigoReserva(reservaCodigo);
        List<HistoryDto> dtos = historicos.stream()
                .map(h -> new HistoryDto(
                        h.getId(),
                        h.getCodigoReserva(),
                        h.getData(),
                        resolveEstadoNome(h.getEstadoOrigem()),
                        resolveEstadoNome(h.getEstadoDestino())))
                .toList();
        try {
            return objectMapper.writeValueAsString(dtos);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao serializar histórico para JSON", e);
        }
    }

    private boolean updateHistory(String reservaCodigo) {
        try {
            Reserva view = reservaQueryRepository.findById(reservaCodigo).get();
            view.setHistorico(buildHistoryJson(reservaCodigo));
            reservaQueryRepository.save(view);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String resolveEstadoNome(Long id) {
        if (id == null)
            return null;
        return estadoRepository.findById(id)
                .map(e -> e.getNome())
                .orElse("DESCONHECIDO");
    }
}
