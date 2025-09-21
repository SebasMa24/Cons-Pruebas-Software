package com.Taller1.Taller1.Controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

        model.addAttribute("tareas", tareas);
        return "index";
    }

    
}
