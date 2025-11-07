package com.Taller1.Taller1.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Taller1.Taller1.Entity.Tarea;

public interface TareaRepository extends JpaRepository<Tarea, Long> {
    List<Tarea> findByEstado(String estado);

    List<Tarea> findByFechaVencimientoBetween(LocalDate inicio, LocalDate fin);

    Optional<Tarea> findByTitulo(String titulo);
    // HU6: Búsqueda de tareas por título (case insensitive)
    @Query("SELECT t FROM Tarea t WHERE LOWER(t.titulo) LIKE LOWER(CONCAT('%', :texto, '%'))")
    List<Tarea> buscarPorTitulo(@Param("texto") String texto);
}