package com.Taller1.Taller1.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public void eliminarTarea(Long id) {
        if (tareaRepository.existsById(id)) {
            tareaRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "La tarea con ID " + id + " no existe");
        }
    }

    // Editar una tarea existente (solo título, descripción y fecha de vencimiento)
    public Tarea editarTarea(Long id, String titulo, String descripcion, LocalDate fechaVencimiento,
            LocalDateTime recordatorio) {
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
        existente.setRecordatorio(recordatorio);
        System.out.println("Recordatorio recibido: " + recordatorio);
        System.out.println("Tarea antes de guardar: " + existente);

        return tareaRepository.save(existente);
    }

    public Tarea crearTarea(Tarea tarea) {
        if (tarea.getId() != null) {
            if (tareaRepository.existsById(tarea.getId())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "La tarea con ID " + tarea.getId() + " ya existe");
            }
        }

        if (tarea.getTitulo() == null || tarea.getTitulo().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El título de la tarea no puede estar vacío");
        }
        if (tarea.getTitulo().length() > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El título de la tarea no puede exceder 100 caracteres");
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

    // Tareas con recordatorio próximo
    public List<Tarea> tareasConRecordatorioProximo() {
        LocalDateTime ahora = LocalDateTime.now();
        return tareaRepository.findAll().stream()
                .filter(t -> t.getRecordatorio() != null)
                .filter(t -> !t.getRecordatorio().isAfter(ahora))
                .toList();
    }

    public Map<Long, Boolean> calcularRecordatorioProximoMap(List<Tarea> tareas) {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime dentroDe24h = ahora.plusHours(24);
        return tareas.stream()
                .collect(Collectors.toMap(
                        Tarea::getId,
                        t -> t.getRecordatorio() != null &&
                                !t.getRecordatorio().isBefore(ahora) &&
                                t.getRecordatorio().isBefore(dentroDe24h)));
    }

    public Map<Long, String> calcularRecordatorioFormateado(List<Tarea> tareas) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return tareas.stream()
                .filter(t -> t.getRecordatorio() != null)
                .collect(Collectors.toMap(
                        Tarea::getId,
                        t -> t.getRecordatorio().format(fmt)));
    }

    // utility para determinar si la tarea vence pronto o está vencida
    public Map<Long, String> calcularEstadoVisual(List<Tarea> tareas) {
        LocalDate hoy = LocalDate.now();

        return tareas.stream().collect(Collectors.toMap(
                Tarea::getId,
                t -> {
                    if (t.getFechaVencimiento() == null) {
                        return "normal";
                    }

                    LocalDate fecha = t.getFechaVencimiento();

                    if (fecha.isBefore(hoy)) {
                        return "vencida";
                    } else if (fecha.isEqual(hoy)) {
                        return "proxima";
                    } else {
                        return "normal";
                    }
                }));
    }
}
