package com.Taller1.Taller1.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Taller1.Taller1.Entity.Tarea;

public interface TareaRepository extends JpaRepository<Tarea, Long> {
    List<Tarea> findByEstado(String estado);

    List<Tarea> findByFechaVencimientoBetween(LocalDate inicio, LocalDate fin);

    Optional<Tarea> findByTitulo(String titulo);
}