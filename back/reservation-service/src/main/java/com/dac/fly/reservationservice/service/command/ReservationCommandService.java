package com.dac.fly.reservationservice.service.command;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dac.fly.reservationservice.dto.HistoryDto;
import com.dac.fly.reservationservice.dto.ReservationDto;
import com.dac.fly.reservationservice.dto.events.ReservationUpdatedEventDto;
import com.dac.fly.reservationservice.dto.request.ReservationUpdateStatusDto;
import com.dac.fly.reservationservice.entity.command.Historico;
import com.dac.fly.reservationservice.entity.command.Reserva;
import com.dac.fly.reservationservice.enums.ReservationStatusEnum;
import com.dac.fly.reservationservice.publisher.ReservationPublisher;
import com.dac.fly.reservationservice.repository.command.EstadoRepository;
import com.dac.fly.reservationservice.repository.command.HistoryRepository;
import com.dac.fly.reservationservice.repository.command.ReservaCommandRepository;

@Service
public class ReservationCommandService {

    private final ReservaCommandRepository repository;
    private final EstadoRepository estadoRepository;
    private final ReservationPublisher publisher;
    private final HistoryRepository historyRepository;

    public ReservationCommandService(
            ReservaCommandRepository repository, EstadoRepository estadoRepository,
            ReservationPublisher publisher, HistoryRepository historyRepository) {
        this.repository = repository;
        this.estadoRepository = estadoRepository;
        this.publisher = publisher;
        this.historyRepository = historyRepository;
    }

    public void createReservation(ReservationDto reservationDto) {
        Reserva reserva = new Reserva();
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

    public void cancelReservationByCode(String codigoReserva) {
        Reserva reservation = repository.findById(codigoReserva)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada: " + codigoReserva));

        Long currentStatus = reservation.getEstado();
        Long canceledStatusId = estadoRepository.findByNome(ReservationStatusEnum.CANCELADA.name())
                .orElseThrow(() -> new RuntimeException("Estado 'CANCELADA' não encontrado"))
                .getCodigo();

        reservation.setEstado(canceledStatusId);

        repository.save(reservation);

        Historico history = new Historico(
                reservation.getCodigo(),
                LocalDateTime.now(),
                currentStatus,
                canceledStatusId);

        historyRepository.save(history);

        List<HistoryDto> historyDto = getReservationHistory(codigoReserva);

        try {
            publisher.publishReservationCancelled(historyDto);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao publicar evento de reserva", e);
        }
    }

    public void updateReservationStatusByCode(String codigoReserva, ReservationUpdateStatusDto novoEstado) {
        Reserva reserva = repository.findById(codigoReserva)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));

        Long idNovoEstado = estadoRepository.findByNome(novoEstado.estado().name())
                .orElseThrow(() -> new RuntimeException("Estado não encontrado"))
                .getCodigo();

        Long idEstadoAnterior = reserva.getEstado();
        reserva.setEstado(idNovoEstado);
        repository.save(reserva);

        Historico historico = new Historico(
                reserva.getCodigo(),
                LocalDateTime.now(),
                idEstadoAnterior,
                idNovoEstado);
        historyRepository.save(historico);

        List<HistoryDto> historicoAtualizado = getReservationHistory(reserva.getCodigo());

        ReservationUpdatedEventDto reservationUpdatedEventDto = ReservationUpdatedEventDto.fromCommand(reserva,
                novoEstado.estado(), historicoAtualizado);

        try {
            publisher.publishReservationUpdated(reservationUpdatedEventDto);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao publicar evento de reserva", e);
        }

    }

    private List<HistoryDto> getReservationHistory(String reservationCode) {
        List<Historico> history = historyRepository.findByCodigoReserva(reservationCode);

        return history.stream()
                .map(h -> new HistoryDto(
                        h.getId(),
                        h.getCodigoReserva(),
                        h.getData(),
                        resolveEstadoNome(h.getEstadoOrigem()),
                        resolveEstadoNome(h.getEstadoDestino())))
                .toList();
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
