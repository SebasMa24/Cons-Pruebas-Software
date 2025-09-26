package com.Taller1.Taller1;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

        @SuppressWarnings("null")
        @Test
        void editarTarea_debeActualizarCorrectamente() {
                // Arrange
                LocalDate fechaVencimiento = LocalDate.now().plusDays(5);

                Map<String, Object> body = new HashMap<>();
                body.put("titulo", "Título Editado");
                body.put("descripcion", "Desc Editada");
                body.put("fechaVencimiento", fechaVencimiento.toString());

                Tarea tareaEditada = new Tarea(1L, "Título Editado", "Desc Editada", fechaVencimiento, "PENDIENTE");

                when(tareaService.editarTarea(eq(1L), eq("Título Editado"), eq("Desc Editada"), eq(fechaVencimiento)))
                                .thenReturn(tareaEditada);

                // Act
                ResponseEntity<?> response = tareaController.editarTarea(1L, body);

                // Assert
                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertEquals("Título Editado", ((Tarea) response.getBody()).getTitulo());
                assertEquals("Desc Editada", ((Tarea) response.getBody()).getDescripcion());

                verify(tareaService, times(1))
                                .editarTarea(eq(1L), eq("Título Editado"), eq("Desc Editada"), eq(fechaVencimiento));
        }

        @Test
        void editarTarea_conTituloVacio_debeRetornarBadRequest() {
                // Arrange
                LocalDate fechaVencimiento = LocalDate.now();

                Map<String, Object> body = new HashMap<>();
                body.put("titulo", ""); // Título vacío
                body.put("descripcion", "Desc");
                body.put("fechaVencimiento", fechaVencimiento.toString());

                when(tareaService.editarTarea(eq(1L), eq(""), eq("Desc"), eq(fechaVencimiento)))
                                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                                "El título de la tarea no puede estar vacío"));

                // Act
                ResponseStatusException exception = assertThrows(
                                ResponseStatusException.class,
                                () -> tareaController.editarTarea(1L, body));

                // Assert
                assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
                assertEquals("El título de la tarea no puede estar vacío", exception.getReason());
        }

        @Test
        void editarTarea_conTituloMuyLargo_debeRetornarBadRequest() {
                // Arrange
                String tituloLargo = "A".repeat(101);
                LocalDate fechaVencimiento = LocalDate.now();

                Map<String, Object> body = new HashMap<>();
                body.put("titulo", tituloLargo);
                body.put("descripcion", "Desc");
                body.put("fechaVencimiento", fechaVencimiento.toString());

                when(tareaService.editarTarea(eq(1L), eq(tituloLargo), eq("Desc"), eq(fechaVencimiento)))
                                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                                "El título de la tarea no puede exceder 100 caracteres"));

                // Act
                ResponseStatusException exception = assertThrows(
                                ResponseStatusException.class,
                                () -> tareaController.editarTarea(1L, body));

                // Assert
                assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
                assertEquals("El título de la tarea no puede exceder 100 caracteres", exception.getReason());
        }

        @Test
        void editarTarea_inexistente_debeRetornarNotFound() {
                // Arrange
                LocalDate fechaVencimiento = LocalDate.now();

                Map<String, Object> body = new HashMap<>();
                body.put("titulo", "Título");
                body.put("descripcion", "Desc");
                body.put("fechaVencimiento", fechaVencimiento.toString());

                when(tareaService.editarTarea(eq(99L), eq("Título"), eq("Desc"), eq(fechaVencimiento)))
                                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarea no encontrada"));

                // Act
                ResponseStatusException exception = assertThrows(
                                ResponseStatusException.class,
                                () -> tareaController.editarTarea(99L, body));

                // Assert
                assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
                assertEquals("Tarea no encontrada", exception.getReason());
        }

        @SuppressWarnings("null")
        @Test
        void editarTarea_conCambioDeFechaVencimiento_debeActualizarFecha() {
                // Arrange
                LocalDate nuevaFecha = LocalDate.now().plusDays(10);

                Map<String, Object> body = new HashMap<>();
                body.put("titulo", "Tarea");
                body.put("descripcion", "Desc");
                body.put("fechaVencimiento", nuevaFecha.toString());

                Tarea tareaEditada = new Tarea(1L, "Tarea", "Desc", nuevaFecha, "PENDIENTE");

                when(tareaService.editarTarea(eq(1L), eq("Tarea"), eq("Desc"), eq(nuevaFecha)))
                                .thenReturn(tareaEditada);

                // Act
                ResponseEntity<?> response = tareaController.editarTarea(1L, body);

                // Assert
                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertEquals(nuevaFecha, ((Tarea) response.getBody()).getFechaVencimiento());
        }

}
