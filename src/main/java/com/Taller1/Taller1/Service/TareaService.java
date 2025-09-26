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

    // Crear una nueva tarea
    public Tarea crearTarea(Tarea tarea) {
        if(tarea.getId() != null){
            if (tareaRepository.existsById(tarea.getId())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "La tarea con ID " + tarea.getId() + " ya existe");
            }
        }
        
        if (tarea.getTitulo() == null || tarea.getTitulo().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El título de la tarea no puede estar vacío");
        }
        if (tarea.getTitulo().length() > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El título de la tarea no puede exceder 100 caracteres");
        }
        tarea.setEstado("PENDIENTE");
        return tareaRepository.save(tarea);
    }
    // Actualizar estado tarea
    public Tarea actualizarEstado(Long id, String nuevoEstado) {
    Tarea tarea = tareaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Tarea no encontrada: " + id));

    tarea.setEstado(nuevoEstado);

    if ("COMPLETADA".equalsIgnoreCase(nuevoEstado)) {
        tarea.setFechaFinalizacion(LocalDate.now());
    } else {
        tarea.setFechaFinalizacion(null); // si se desmarca
    }

    return tareaRepository.save(tarea);
    }
}
