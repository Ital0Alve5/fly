package com.dac.fly.reservationservice.service.command;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dac.fly.reservationservice.dto.HistoryDto;
import com.dac.fly.reservationservice.dto.events.ReservationCreatedEventDto;
import com.dac.fly.reservationservice.dto.events.ReservationUpdatedEventDto;
import com.dac.fly.reservationservice.dto.factory.ReservationResponseFactory;
import com.dac.fly.reservationservice.dto.request.ReservationCreateRequestDto;
import com.dac.fly.reservationservice.dto.request.ReservationUpdateStatusDto;
import com.dac.fly.reservationservice.dto.response.ReservationResponseDto;
import com.dac.fly.reservationservice.entity.command.Historico;
import com.dac.fly.reservationservice.entity.command.Reserva;
import com.dac.fly.reservationservice.enums.ReservationStatusEnum;
import com.dac.fly.reservationservice.publisher.ReservationPublisher;
import com.dac.fly.reservationservice.repository.command.EstadoRepository;
import com.dac.fly.reservationservice.repository.command.HistoryRepository;
import com.dac.fly.reservationservice.repository.command.ReservaCommandRepository;
import com.dac.fly.reservationservice.repository.query.ReservaQueryRepository;
import com.dac.fly.reservationservice.util.ReservationCodeGenerator;

@Service
public class ReservationCommandService {

        private final ReservaCommandRepository repository;
        private final EstadoRepository estadoRepository;
        private final ReservationPublisher publisher;
        private final HistoryRepository historyRepository;
        private final ReservationResponseFactory responseFactory;
        private final ReservaQueryRepository reservaQueryRepository;

        public ReservationCommandService(
                        ReservaCommandRepository repository,
                        EstadoRepository estadoRepository,
                        ReservationPublisher publisher,
                        HistoryRepository historyRepository,
                        ReservationResponseFactory responseFactory,
                        ReservaQueryRepository reservaQueryRepository) {
                this.repository = repository;
                this.estadoRepository = estadoRepository;
                this.publisher = publisher;
                this.historyRepository = historyRepository;
                this.responseFactory = responseFactory;
                this.reservaQueryRepository = reservaQueryRepository;
        }

        public ReservationResponseDto createReservation(ReservationCreateRequestDto requestDto) {
                Reserva reserva = new Reserva();
                reserva.setCodigo(ReservationCodeGenerator.generateReservationCode());
                reserva.setCodigoCliente(requestDto.codigo_cliente());
                reserva.setQuantidadePoltronas(requestDto.quantidade_poltronas());
                reserva.setCodigoVoo(requestDto.codigo_voo());
                reserva.setDataReserva(LocalDateTime.now());
                reserva.setMilhasUtilizadas(requestDto.milhas_utilizadas());
                reserva.setValorPago(requestDto.valor());

                Long createdStatusId = estadoRepository.findByNome(ReservationStatusEnum.CRIADA.toString())
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

                List<HistoryDto> historico = getReservationHistory(reserva.getCodigo());

                ReservationCreatedEventDto event = ReservationCreatedEventDto.from(
                                reserva.getCodigo(),
                                reserva.getCodigoCliente(),
                                reserva.getDataReserva(),
                                reserva.getValorPago(),
                                reserva.getMilhasUtilizadas(),
                                reserva.getQuantidadePoltronas(),
                                reserva.getCodigoVoo(),
                                requestDto.codigo_aeroporto_origem(),
                                requestDto.codigo_aeroporto_destino(),
                                historico);

                try {
                        publisher.publishCreatedReservation(event);
                } catch (Exception e) {
                        throw new RuntimeException("Erro ao publicar evento de reserva", e);
                }

                return responseFactory.fromCommandReserva(reserva, ReservationStatusEnum.CRIADA,
                                requestDto.codigo_aeroporto_origem(), requestDto.codigo_aeroporto_destino());
        }

        public ReservationResponseDto cancelReservationByCode(String codigoReserva) {
                Reserva reservation = repository.findById(codigoReserva)
                                .orElseThrow(() -> new RuntimeException("Reserva não encontrada: " + codigoReserva));

                Long currentStatus = reservation.getEstado();
                Long canceledStatusId = estadoRepository.findByNome(ReservationStatusEnum.CANCELADA.toString())
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

                String origem = null;
                String destino = null;
                var reservaOptional = reservaQueryRepository.findById(codigoReserva);
                if (reservaOptional.isPresent()) {
                        var query = reservaOptional.get();
                        origem = query.getAeroportoOrigem();
                        destino = query.getAeroportoDestino();
                }

                return responseFactory.fromCommandReserva(reservation, ReservationStatusEnum.CANCELADA, origem,
                                destino);
        }

        public ReservationResponseDto updateReservationStatusByCode(String codigoReserva,
                        ReservationUpdateStatusDto novoEstado) {
                Reserva reserva = repository.findById(codigoReserva)
                                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));

                String estadoAtual = resolveEstadoNome(reserva.getEstado());
                if (estadoAtual.equals("CANCELADA") || estadoAtual.equals("CANCELADA-VOO")) {
                        throw new RuntimeException("Não é permitido alterar o estado de uma reserva cancelada.");
                }

                Long idNovoEstado = estadoRepository.findByNome(novoEstado.estado().toString())
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

                String origem = null;
                String destino = null;
                var reservaOptional = reservaQueryRepository.findById(codigoReserva);
                if (reservaOptional.isPresent()) {
                        var query = reservaOptional.get();
                        origem = query.getAeroportoOrigem();
                        destino = query.getAeroportoDestino();
                }

                return responseFactory.fromCommandReserva(reserva, novoEstado.estado(), origem, destino);
        }

        private List<HistoryDto> getReservationHistory(String reservationCode) {
                return historyRepository.findByCodigoReserva(reservationCode).stream()
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
}
