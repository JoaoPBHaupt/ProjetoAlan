package com.infrastructure.repository;



import com.domain.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findBySalaIdAndDataHoraInicioBetween(Long salaId, LocalDateTime inicio, LocalDateTime fim);
    List<Reserva> findByUsuarioId(Long usuarioId);
} 