package com.Taller1.Taller1.Controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import com.Taller1.Taller1.Entity.Tarea;
import com.Taller1.Taller1.Service.TareaService;

@Controller
public class TareaController {
    private final TareaService tareaService;

    public TareaController(TareaService tareaService) {
        this.tareaService = tareaService;
    }

    @GetMapping
    public String listarTareas(Model model,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Integer semana) {

        List<Tarea> tareas;
        if (estado != null && !estado.isEmpty()) {
            tareas = tareaService.filtrarPorEstado(estado);
        } else if (semana != null && semana > 0) {
            tareas = tareaService.filtrarPorSemana(semana);
        } else {
            tareas = tareaService.obtenerTodas();
        }

        // calculos para el view (evitar lógica en Thymeleaf)
        Map<Long, Boolean> recordatorioProximo = tareaService.calcularRecordatorioProximoMap(tareas);
        Map<Long, String> recordatorioFormat = tareaService.calcularRecordatorioFormateado(tareas);
        Map<Long, String> estadoVisual = tareaService.calcularEstadoVisual(tareas);

        model.addAttribute("tareaEditar", new Tarea());
        model.addAttribute("tareas", tareas);
        model.addAttribute("tareaNueva", new Tarea());

        model.addAttribute("recordatorioProximo", recordatorioProximo);
        model.addAttribute("recordatorioFormat", recordatorioFormat);
        model.addAttribute("estadoVisual", estadoVisual);
        model.addAttribute("estado", estado != null ? estado : ""); // 🔹 para mantener filtro
        return "index";
    }

    @PostMapping("/tarea")
    public ResponseEntity<Tarea> crearTarea(@RequestBody Tarea tareaNueva) {
        Tarea creada = tareaService.crearTarea(tareaNueva);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/tarea/{id}")
    public ResponseEntity<Tarea> editarTarea(
            @PathVariable long id,
            @RequestBody Map<String, Object> body) {
        String titulo = (String) body.get("titulo");
        String descripcion = (String) body.get("descripcion");
        LocalDate fechaVencimiento = body.get("fechaVencimiento") != null
                ? LocalDate.parse((String) body.get("fechaVencimiento"))
                : null;
        LocalDateTime recordatorio = body.get("recordatorio") != null
                ? LocalDateTime.parse((String) body.get("recordatorio"))
                : null;
        Tarea tareaEditada = tareaService.editarTarea(id, titulo, descripcion, fechaVencimiento, recordatorio);
        return ResponseEntity.ok(tareaEditada);
    }

    @PostMapping("/eliminar")
    public Object eliminarTarea(@RequestParam Long id) {
        try {
            tareaService.eliminarTarea(id);
            return new RedirectView("/");
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/tareas/{id}/estado")
    public String cambiarEstado(@PathVariable Long id,
            @RequestParam String estado) {
        tareaService.actualizarEstado(id, estado);
        return "redirect:/"; // redirige a la lista principal
    }

    // Buscar tarea
    @GetMapping("/tareas/buscar")
    @ResponseBody
    public Map<String, Object> buscarTareas(@RequestParam(required = false, defaultValue = "") String texto) {

        List<Tarea> tareas = (texto == null || texto.isBlank())
                ? tareaService.obtenerTodas()
                : tareaService.buscarPorTitulo(texto.trim());

        Map<String, Object> response = new HashMap<>();
        response.put("tareas", tareas);
        response.put("recordatorioProximo", tareaService.calcularRecordatorioProximoMap(tareas));
        response.put("recordatorioFormat", tareaService.calcularRecordatorioFormateado(tareas));
        response.put("estadoVisual", tareaService.calcularEstadoVisual(tareas));

        return response;
    }
}
