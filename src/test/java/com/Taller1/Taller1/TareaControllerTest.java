package com.Taller1.Taller1;

import com.Taller1.Taller1.Controller.TareaController;
import com.Taller1.Taller1.Entity.Tarea;
import com.Taller1.Taller1.Service.TareaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TareaControllerTest {

    @Mock
    private TareaService tareaService;

    @InjectMocks
    private TareaController tareaController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(tareaController).build();
    }

    @Test
    void listarTareas_debeRetornarVistaIndexConListaTareas() throws Exception {
        Tarea tarea = new Tarea(1L, "Tarea 1", "Desc 1", LocalDate.now(), "PENDIENTE");
        when(tareaService.obtenerTodas()).thenReturn(List.of(tarea));

        mockMvc.perform(get(""))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("tareas"))
                .andExpect(model().attribute("tareas", List.of(tarea)));

        verify(tareaService, times(1)).obtenerTodas();
    }

    @Test
    void listarTareas_conEstadoDebeFiltrarCorrectamente() throws Exception {
        Tarea tarea = new Tarea(2L, "Tarea Completada", "Desc", LocalDate.now(), "COMPLETADA");
        when(tareaService.filtrarPorEstado("COMPLETADA")).thenReturn(List.of(tarea));

        mockMvc.perform(get("").param("estado", "COMPLETADA"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("tareas"))
                .andExpect(model().attribute("tareas", List.of(tarea)));

        verify(tareaService, times(1)).filtrarPorEstado("COMPLETADA");
    }

    @Test
    void listarTareas_conSemanaDebeFiltrarCorrectamente() throws Exception {
        Tarea tarea = new Tarea(3L, "Tarea Semana 38", "Desc", LocalDate.of(2025, 9, 18), "PENDIENTE");
        when(tareaService.filtrarPorSemana(38)).thenReturn(List.of(tarea));

        mockMvc.perform(get("").param("semana", "38"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("tareas"))
                .andExpect(model().attribute("tareas", List.of(tarea)));

        verify(tareaService, times(1)).filtrarPorSemana(38);
    }
}
