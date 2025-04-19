package com.dac.fly.reservationservice.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dac.fly.reservationservice.dto.HistoryDto;
import com.dac.fly.reservationservice.dto.ReservationDto;
import com.dac.fly.reservationservice.entity.command.Historico;
import com.dac.fly.reservationservice.enums.ReservationStatusEnum;
import com.dac.fly.reservationservice.publisher.ReservationPublisher;
import com.dac.fly.reservationservice.repository.command.EstadoRepository;
import com.dac.fly.reservationservice.repository.command.HistoryRepository;
import com.dac.fly.reservationservice.repository.command.ReservaCommandRepository;
import com.dac.fly.reservationservice.repository.query.ReservaQueryRepository;

@Service
public class ReservationService {

    private final ReservaCommandRepository repository;
    private final EstadoRepository estadoRepository;
    private final ReservationPublisher publisher;
    private final HistoryRepository historyRepository;
    private final ReservaQueryRepository reservaQueryRepository;

    public ReservationService(
            ReservaCommandRepository repository, EstadoRepository estadoRepository, ReservationPublisher publisher,
            com.dac.fly.reservationservice.repository.command.HistoryRepository historyRepository,
            ReservaQueryRepository reservaQueryRepository) {
        this.repository = repository;
        this.estadoRepository = estadoRepository;
        this.publisher = publisher;
        this.historyRepository = historyRepository;
        this.reservaQueryRepository = reservaQueryRepository;
    }

    public List<com.dac.fly.reservationservice.dto.response.ReservationDto> getReservationByClientCode(
            Long clientCode) {
        List<com.dac.fly.reservationservice.entity.query.Reserva> reservations = reservaQueryRepository
                .findBycodigoCliente(clientCode);

        return reservations.stream().map(r -> new com.dac.fly.reservationservice.dto.response.ReservationDto(
                r.getCodigo(),
                r.getDataReserva(),
                r.getValor(),
                r.getMilhasUtilizadas(),
                r.getQuantidadePoltronas(),
                r.getCodigoCliente(),
                ReservationStatusEnum.valueOf(r.getEstado()))).toList();
    }

    public com.dac.fly.reservationservice.dto.response.ReservationDto getReservationByCode(
            String code) {
        com.dac.fly.reservationservice.entity.query.Reserva reservation = reservaQueryRepository
                .findBycodigo(code);

        return com.dac.fly.reservationservice.dto.response.ReservationDto.from(reservation);
    }

    public void createReservation(ReservationDto reservationDto) {
        com.dac.fly.reservationservice.entity.command.Reserva reserva = new com.dac.fly.reservationservice.entity.command.Reserva();
        reserva.setCodigo(generateCode());
        reserva.setCodigoCliente(reservationDto.getCodigo_cliente());
        reserva.setQuantidadePoltronas(reservationDto.getQuantidade_poltronas());
        reserva.setCodigoVoo(reservationDto.getCodigo_voo());
        reserva.setDataReserva(LocalDateTime.now());
        reserva.setMilhasUtilizadas(reservationDto.getMilhas_utilizadas());
        reserva.setValorPago(reservationDto.getValor());

        Long createdStatusId = estadoRepository.findByNome(ReservationStatusEnum.CRIADA.name())
                .orElseThrow(() -> new RuntimeException("Estado 'CRIADA' não encontrado"))
                .getCodigo();

        reserva.setEstado(createdStatusId);

        repository.save(reserva);

        Historico history = new Historico(
                reserva.getCodigo(),
                LocalDateTime.now(),
                null,
                createdStatusId);

        historyRepository.save(history);

        List<HistoryDto> historyDto = getReservationHistory(reserva.getCodigo());

        reservationDto.setCodigo(reserva.getCodigo());
        reservationDto.setHistorico(historyDto);
        reservationDto.setDataReserva(reserva.getDataReserva());

        try {
            publisher.publishCreatedReservation(reservationDto);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao publicar evento de reserva", e);
        }
    }

    private List<HistoryDto> getReservationHistory(String reservationCode) {
        List<Historico> history = historyRepository.findByCodigoReserva(reservationCode);

        List<HistoryDto> historyDto = history.stream()
                .map(h -> new HistoryDto(
                        h.getId(),
                        h.getCodigoReserva(),
                        h.getData(),
                        resolveEstadoNome(h.getEstadoOrigem()),
                        resolveEstadoNome(h.getEstadoDestino())))
                .toList();

        return historyDto;
    }

    private String resolveEstadoNome(Long id) {
        if (id == null)
            return null;
        return estadoRepository.findById(id)
                .map(estado -> estado.getNome())
                .orElse("DESCONHECIDO");
    }

    private String generateCode() {
        String letters = generateRandomLetters(3);
        String numbers = String.format("%03d", (int) (Math.random() * 1000));
        return letters + numbers;
    }

    private String generateRandomLetters(int tamanho) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tamanho; i++) {
            char letter = (char) ('A' + (int) (Math.random() * 26));
            sb.append(letter);
        }
        return sb.toString();
    }
}
