package com.dac.fly.reservationservice.service.command;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.springframework.stereotype.Service;

import com.dac.fly.reservationservice.dto.factory.ReservationResponseFactory;
import com.dac.fly.reservationservice.dto.request.ReservationUpdateStatusDto;
import com.dac.fly.reservationservice.dto.response.ReservationResponseDto;
import com.dac.fly.reservationservice.entity.command.Estado;
import com.dac.fly.reservationservice.entity.command.Historico;
import com.dac.fly.reservationservice.entity.command.Reserva;
import com.dac.fly.reservationservice.enums.ReservationStatusEnum;
import com.dac.fly.reservationservice.repository.command.EstadoRepository;
import com.dac.fly.reservationservice.repository.command.HistoryRepository;
import com.dac.fly.reservationservice.repository.command.ReservaCommandRepository;
import com.dac.fly.reservationservice.repository.query.ReservaQueryRepository;
import com.dac.fly.shared.dto.command.CreateReservationCommand;
import com.dac.fly.shared.dto.events.CancelledReservationEventDto;
import com.dac.fly.shared.dto.response.CreatedReservationResponseDto;

@Service
public class ReservationCommandService {

        private final ReservaCommandRepository repository;
        private final EstadoRepository estadoRepository;
        private final HistoryRepository historyRepository;
        private final ReservationResponseFactory responseFactory;
        private final ReservaQueryRepository reservaQueryRepository;

        public ReservationCommandService(
                        ReservaCommandRepository repository,
                        EstadoRepository estadoRepository,
                        HistoryRepository historyRepository,
                        ReservationResponseFactory responseFactory,
                        ReservaQueryRepository reservaQueryRepository) {
                this.repository = repository;
                this.estadoRepository = estadoRepository;
                this.historyRepository = historyRepository;
                this.responseFactory = responseFactory;
                this.reservaQueryRepository = reservaQueryRepository;
        }

        public CreatedReservationResponseDto createReservation(CreateReservationCommand requestDto) {
                Reserva reserva = new Reserva();
                reserva.setCodigo(requestDto.codigo());

                OffsetDateTime now = OffsetDateTime.now(ZoneOffset.of("-03:00"));
                reserva.setDataReserva(now);
                reserva.setCodigoCliente(requestDto.codigo_cliente());
                reserva.setQuantidadePoltronas(requestDto.quantidade_poltronas());
                reserva.setCodigoVoo(requestDto.codigo_voo());
                reserva.setMilhasUtilizadas(requestDto.milhas_utilizadas());
                reserva.setValorPago(requestDto.valor());

                Long createdStatusId = estadoRepository.findByNome(ReservationStatusEnum.CRIADA.toString())
                                .orElseThrow(() -> new RuntimeException("Estado 'CRIADA' não encontrado"))
                                .getCodigo();
                reserva.setEstado(createdStatusId);
                repository.save(reserva);

                Historico historyEntity = new Historico(
                                reserva.getCodigo(),
                                OffsetDateTime.now(ZoneOffset.of("-03:00")),
                                null,
                                createdStatusId);
                historyRepository.save(historyEntity);

                CreatedReservationResponseDto responseDto = new CreatedReservationResponseDto(
                                requestDto.codigo(),
                                now,
                                requestDto.valor(),
                                requestDto.milhas_utilizadas(),
                                requestDto.quantidade_poltronas(),
                                requestDto.codigo_cliente(),
                                ReservationStatusEnum.CRIADA.toString(),
                                requestDto.codigo_voo(),
                                requestDto.codigo_aeroporto_origem(),
                                requestDto.codigo_aeroporto_destino());

                return responseDto;
        }

        public CancelledReservationEventDto cancelReservationByCode(String codigo_reserva) {
                return doCancel(codigo_reserva, ReservationStatusEnum.CANCELADA);
        }

        public CancelledReservationEventDto cancelReservationByFlight(String codigo_reserva) {
                return doCancel(codigo_reserva, ReservationStatusEnum.CANCELADA_VOO);
        }

        public void completeReservationByFlight(String codigo_reserva) {
                doComplete(codigo_reserva);
        }

        public CancelledReservationEventDto doCancel(String codigo_reserva,
                        ReservationStatusEnum targetStatus) {
                Reserva reservation = repository.findById(codigo_reserva)
                                .orElseThrow(() -> new RuntimeException("Reserva não encontrada: " + codigo_reserva));

                Long oldStatus = reservation.getEstado();
                String oldStateName = estadoRepository.findById(oldStatus)
                                .orElseThrow(() -> new RuntimeException(
                                                "Estado anterior não encontrado: " + oldStatus))
                                .getNome();

                Long newStatusId = estadoRepository.findByNome(targetStatus.toString())
                                .orElseThrow(() -> new RuntimeException("Estado '" + targetStatus + "' não encontrado"))
                                .getCodigo();

                reservation.setEstado(newStatusId);
                repository.save(reservation);

                Historico historyEntity = new Historico(
                                reservation.getCodigo(),
                                OffsetDateTime.now(ZoneOffset.of("-03:00")),
                                oldStatus,
                                newStatusId);
                historyRepository.save(historyEntity);

                com.dac.fly.reservationservice.entity.query.Reserva reservationQuery = reservaQueryRepository
                                .findById(codigo_reserva)
                                .orElseThrow(() -> new RuntimeException("Reserva não encontrada: " + codigo_reserva));

                CancelledReservationEventDto cancelDto = new CancelledReservationEventDto(
                                reservation.getCodigo(),
                                reservation.getDataReserva(),
                                reservation.getValorPago(),
                                reservation.getMilhasUtilizadas(),
                                reservation.getQuantidadePoltronas(),
                                reservation.getCodigoCliente(),
                                ReservationStatusEnum.CANCELADA.toString(),
                                oldStateName,
                                reservation.getCodigoVoo(),
                                reservationQuery.getAeroportoOrigem(),
                                reservationQuery.getAeroportoDestino());

                return cancelDto;
        }

        public void doComplete(String codigo_reserva) {
                Reserva reserva = repository.findById(codigo_reserva)
                                .orElseThrow(() -> new RuntimeException("Reserva não encontrada: " + codigo_reserva));

                String estadoAtual = resolveEstadoNome(reserva.getEstado());
                ReservationStatusEnum targetStatus;

                if ("CANCELADA VOO".equals(estadoAtual) || "CANCELADA".equals(estadoAtual)) {
                        return;
                }

                if ("EMBARCADA".equals(estadoAtual)) {
                        targetStatus = ReservationStatusEnum.REALIZADA;
                } else {
                        targetStatus = ReservationStatusEnum.NAO_REALIZADA;
                }

                Long oldStatus = reserva.getEstado();

                Long newStatusId = estadoRepository.findByNome(targetStatus.toString())
                                .orElseThrow(() -> new RuntimeException("Estado '" + targetStatus + "' não encontrado"))
                                .getCodigo();

                reserva.setEstado(newStatusId);
                repository.save(reserva);

                Historico historyEntity = new Historico(
                                reserva.getCodigo(),
                                OffsetDateTime.now(ZoneOffset.of("-03:00")),
                                oldStatus,
                                newStatusId);
                historyRepository.save(historyEntity);
        }

        public ReservationResponseDto updateReservationStatusByCode(String codigo_reserva,
                        ReservationUpdateStatusDto novoEstado) {
                Reserva reserva = repository.findById(codigo_reserva)
                                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));

                String estadoAtual = resolveEstadoNome(reserva.getEstado());
                if ("CANCELADA".equals(estadoAtual) || "CANCELADA VOO".equals(estadoAtual)) {
                        throw new RuntimeException("Não é permitido alterar o estado de uma reserva cancelada.");
                }

                if (!"CHECK-IN".equals(estadoAtual) && novoEstado.estado().equals(ReservationStatusEnum.EMBARCADA)) {
                        throw new RuntimeException(
                                        "Não é permitido alterar o estado da reserva para EMBARCADA sem estar no estado CHECK-IN.");
                }

                Long idNovoEstado = estadoRepository.findByNome(novoEstado.estado().toString())
                                .orElseThrow(() -> new RuntimeException("Estado não encontrado"))
                                .getCodigo();

                Long idEstadoAnterior = reserva.getEstado();
                reserva.setEstado(idNovoEstado);
                repository.save(reserva);

                Historico historico = new Historico(
                                reserva.getCodigo(),
                                OffsetDateTime.now(ZoneOffset.of("-03:00")),
                                idEstadoAnterior,
                                idNovoEstado);
                historyRepository.save(historico);

                String origem = null;
                String destino = null;
                var reservaOptional = reservaQueryRepository.findById(codigo_reserva);
                if (reservaOptional.isPresent()) {
                        var query = reservaOptional.get();
                        origem = query.getAeroportoOrigem();
                        destino = query.getAeroportoDestino();
                }

                return responseFactory.fromCommandReserva(reserva, novoEstado.estado(), origem, destino);
        }

        private String resolveEstadoNome(Long id) {
                if (id == null)
                        return null;
                return estadoRepository.findById(id)
                                .map(e -> e.getNome())
                                .orElse("DESCONHECIDO");
        }

        public boolean exists(String reservationId) {
                return repository.existsById(reservationId);
        }

        public void removeReservation(String reservationId) {
                repository.deleteById(reservationId);
        }

        public void revertReservationStatus(String codigo_reserva, String previousState) {
                Reserva reserva = repository.findById(codigo_reserva)
                                .orElseThrow(() -> new RuntimeException("Reserva não encontrada: " + codigo_reserva));

                Estado prevEstado = estadoRepository.findByNome(previousState)
                                .orElseThrow(() -> new RuntimeException(
                                                "Estado anterior não encontrado: " + previousState));

                Long prevId = prevEstado.getCodigo();
                Long currentId = reserva.getEstado();

                reserva.setEstado(prevId);
                repository.save(reserva);

                Historico historico = new Historico(
                                reserva.getCodigo(),
                                OffsetDateTime.now(ZoneOffset.of("-03:00")),
                                currentId,
                                prevId);
                historyRepository.save(historico);
        }

}
