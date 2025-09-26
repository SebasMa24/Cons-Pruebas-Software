package com.Taller1.Taller1;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import com.Taller1.Taller1.Controller.TareaController;
import com.Taller1.Taller1.Entity.Tarea;
import com.Taller1.Taller1.Service.TareaService;

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

    @Test
void editarTarea_debeActualizarCorrectamente() {
    Tarea tareaEditada = new Tarea(1L, "Título Editado", "Desc Editada", LocalDate.now().plusDays(5), "PENDIENTE");

    when(tareaService.editarTarea(eq(1L), anyString(), anyString(), any(LocalDate.class)))
            .thenReturn(tareaEditada);

    ResponseEntity<?> response = tareaController.editarTarea(
            1L, "Título Editado", "Desc Editada", LocalDate.now().plusDays(5));

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Título Editado", ((Tarea) response.getBody()).getTitulo());
    assertEquals("Desc Editada", ((Tarea) response.getBody()).getDescripcion());
    verify(tareaService, times(1))
            .editarTarea(eq(1L), anyString(), anyString(), any(LocalDate.class));
}

@Test
void editarTarea_conTituloVacio_debeRetornarBadRequest() {
    when(tareaService.editarTarea(eq(1L), eq(""), anyString(), any(LocalDate.class)))
            .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El título de la tarea no puede estar vacío"));

    ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> tareaController.editarTarea(1L, "", "Desc", LocalDate.now()));

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    assertEquals("El título de la tarea no puede estar vacío", exception.getReason());
}

@Test
void editarTarea_conTituloMuyLargo_debeRetornarBadRequest() {
    String tituloLargo = "A".repeat(101);
    when(tareaService.editarTarea(eq(1L), eq(tituloLargo), anyString(), any(LocalDate.class)))
            .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El título de la tarea no puede exceder 100 caracteres"));

    ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> tareaController.editarTarea(1L, tituloLargo, "Desc", LocalDate.now()));

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    assertEquals("El título de la tarea no puede exceder 100 caracteres", exception.getReason());
}

@Test
void editarTarea_inexistente_debeRetornarNotFound() {
    when(tareaService.editarTarea(eq(99L), anyString(), anyString(), any(LocalDate.class)))
            .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarea no encontrada"));

    ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> tareaController.editarTarea(99L, "Título", "Desc", LocalDate.now()));

    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    assertEquals("Tarea no encontrada", exception.getReason());
}

@Test
void editarTarea_conCambioDeFechaVencimiento_debeActualizarFecha() {
    LocalDate nuevaFecha = LocalDate.now().plusDays(10);
    Tarea tareaEditada = new Tarea(1L, "Tarea", "Desc", nuevaFecha, "PENDIENTE");

    when(tareaService.editarTarea(eq(1L), anyString(), anyString(), eq(nuevaFecha)))
            .thenReturn(tareaEditada);

    ResponseEntity<?> response = tareaController.editarTarea(1L, "Tarea", "Desc", nuevaFecha);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(nuevaFecha, ((Tarea) response.getBody()).getFechaVencimiento());
}

}
