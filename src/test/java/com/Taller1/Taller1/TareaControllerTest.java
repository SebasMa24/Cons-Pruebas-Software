package com.Taller1.Taller1;

import com.Taller1.Taller1.Controller.TareaController;
import com.Taller1.Taller1.Entity.Tarea;
import com.Taller1.Taller1.Service.TareaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TareaControllerTest {

    @Mock
    private TareaService tareaService;

    @InjectMocks
    private TareaController tareaController;

    private MockMvc mockMvc;

    private Tarea tarea;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(tareaController).build();
        tarea = new Tarea();
        tarea.setId(1L);
        tarea.setTitulo("Test Tarea");
        tarea.setDescripcion("Descripción de prueba");
        tarea.setEstado("PENDIENTE");

    }

    @Test
    void crearTarea_debeRetornarTareaCreada() {
        when(tareaService.crearTarea(any(Tarea.class))).thenReturn(tarea);

        ResponseEntity<?> response = tareaController.crearTarea(tarea);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(tarea, response.getBody());
        verify(tareaService, times(1)).crearTarea(tarea);
    }

    @Test
    void crearTarea_conTituloVacio_debeRetornarBadRequest() {
        tarea.setTitulo("");

        when(tareaService.crearTarea(any(Tarea.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "El título de la tarea no puede estar vacío"));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> tareaController.crearTarea(tarea));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("El título de la tarea no puede estar vacío", exception.getReason());
    }

    @Test
    void crearTarea_conTituloNull_debeRetornarBadRequest() {
        tarea.setTitulo(null);

        when(tareaService.crearTarea(any(Tarea.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "El título de la tarea no puede estar vacío"));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> tareaController.crearTarea(tarea));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("El título de la tarea no puede estar vacío", exception.getReason());
    }

    @Test
    void crearTarea_conTituloMuyLargo_debeRetornarBadRequest() {
        tarea.setTitulo("A".repeat(101));
        when(tareaService.crearTarea(any(Tarea.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "El título de la tarea no puede exceder 100 caracteres"));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> tareaController.crearTarea(tarea));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("El título de la tarea no puede exceder 100 caracteres", exception.getReason());
    }

    @Test
    void crearTarea_conFechaVencimiento_debeGuardarCorrectamente() {
        tarea.setFechaVencimiento(LocalDate.now().plusDays(7));
        when(tareaService.crearTarea(any(Tarea.class))).thenReturn(tarea);

        ResponseEntity<?> response = tareaController.crearTarea(tarea);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(((Tarea) response.getBody()).getFechaVencimiento());
    }

    @Test
    void crearTarea_conEstadoNull_debeEstablecerEstadoPorDefecto() {
        tarea.setEstado(null);
        Tarea tareaConEstado = new Tarea();
        tareaConEstado.setEstado("PENDIENTE");

        when(tareaService.crearTarea(any(Tarea.class))).thenReturn(tareaConEstado);

        ResponseEntity<?> response = tareaController.crearTarea(tarea);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("PENDIENTE", ((Tarea) response.getBody()).getEstado());
    }

    @Test
    void crearTarea_conExcepcionInesperada_debePropagarExcepcion() {
        when(tareaService.crearTarea(any(Tarea.class)))
                .thenThrow(new RuntimeException("Error inesperado"));

        assertThrows(RuntimeException.class, () -> {
            tareaController.crearTarea(tarea);
        });
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
