package com.Taller1.Taller1.Service;

import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.Taller1.Taller1.Entity.Tarea;
import com.Taller1.Taller1.Repository.TareaRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TareaService {
    private final TareaRepository tareaRepository;

    // Obtener todas las tareas
    public List<Tarea> obtenerTodas() {
        return tareaRepository.findAll();
    }

    // Obtener una tarea por ID
    public Optional<Tarea> obtenerPorId(Long id) {
        return tareaRepository.findById(id);
    }

    // Filtrar por estado
    public List<Tarea> filtrarPorEstado(String estado) {
        return tareaRepository.findByEstado(estado);
    }

    // Filtrar por rango de fechas
    public List<Tarea> filtrarPorRangoFechas(LocalDate inicio, LocalDate fin) {
        return tareaRepository.findByFechaVencimientoBetween(inicio, fin);
    }

    // Filtrar por semana del año
    public List<Tarea> filtrarPorSemana(int numeroSemana) {
        return tareaRepository.findAll().stream()
                .filter(t -> t.getFechaVencimiento() != null &&
                        t.getFechaVencimiento().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR) == numeroSemana)
                .toList();
    }

    // Editar una tarea existente (solo título, descripción y fecha de vencimiento)
    public Tarea editarTarea(Long id, String titulo, String descripcion, LocalDate fechaVencimiento) {
        System.out.println("Editando tarea con ID: " + id);
        Tarea existente = tareaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarea no encontrada"));

        if (titulo == null || titulo.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El título de la tarea no puede estar vacío");
        }
        if (titulo.length() > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El título de la tarea no puede exceder 100 caracteres");
        }

        existente.setTitulo(titulo);
        existente.setDescripcion(descripcion);
        existente.setFechaVencimiento(fechaVencimiento);

        return tareaRepository.save(existente);
    }

}
