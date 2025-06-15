package com.application.service;


import com.domain.model.Reserva;
import com.domain.model.StatusReserva;
import com.infrastructure.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private UsuarioService usuarioService;

    public ReservaService(ReservaRepository reservaRepository, UsuarioService usuarioService) {
        this.reservaRepository = reservaRepository;
        this.usuarioService = usuarioService;
    }


    public List<Reserva> findAll() {
        return reservaRepository.findAll();
    }

    public Optional<Reserva> findById(Long id) {
        return reservaRepository.findById(id);
    }

    public List<Reserva> findByUsuarioId(Long usuarioId) {
        return reservaRepository.findByUsuarioId(usuarioId);
    }

    public Reserva save(Reserva reserva) {
        try {
            validacoesBasicas(reserva);
            validarSeExisteSalaAlocadaNesseIntervalo(reserva);

            boolean usuarioValido = usuarioService.validarUsuario(reserva.getUsuarioId())
                            .get(5, TimeUnit.SECONDS);

            if (!usuarioValido) {
                throw new RuntimeException("Usuário inválido ou não encontrado");
            }

            reserva.setStatus(StatusReserva.PENDENTE);
            return reservaRepository.save(reserva);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException("Erro ao salvar reserva: " + e.getMessage());
        }
    }

    public void deleteById(Long id) {
        Optional<Reserva> reserva = reservaRepository.findById(id);
        if (reserva.isEmpty()) {
            throw new RuntimeException("Reserva não encontrada");
        }

        reservaRepository.deleteById(id);
    }

    public Reserva updateStatus(Long id, StatusReserva status) {
        if (status == null) {
            throw new RuntimeException("Status não pode ser nulo");
        }
        if (id == null) {
            throw new RuntimeException("ID da reserva não pode ser nulo");
        }

        Optional<Reserva> reservaOpt = reservaRepository.findById(id);
        if (reservaOpt.isEmpty()) {
            throw new RuntimeException("Reserva não encontrada");
        }


        Reserva reserva = reservaOpt.get();
        reserva.setStatus(status);
        return reservaRepository.save(reserva);
    }

    private static void validacoesBasicas(Reserva reserva) {
        if (reserva.getUsuarioId() == null) {
            throw new RuntimeException("Usuário não informado");
        }

        if (reserva.getSalaId() == null) {
            throw new RuntimeException("Sala não informada");
        }

        if (reserva.getDataHoraInicio().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("A data e hora de início não podem ser no passado");
        }
    }

    private void validarSeExisteSalaAlocadaNesseIntervalo(Reserva reserva) {
        List<Reserva> reservasExistentes = reservaRepository.findBySalaIdAndDataHoraInicioBetween(
                reserva.getSalaId(),
                reserva.getDataHoraInicio(),
                reserva.getDataHoraFim()
        );

        if (!reservasExistentes.isEmpty()) {
            throw new IllegalArgumentException("Já existe uma reserva para este horário");
        }
    }
}