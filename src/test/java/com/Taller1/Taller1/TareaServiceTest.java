package com.Taller1.Taller1;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

// JUnit Assertions
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Mockito
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// Proyecto
import com.Taller1.Taller1.Entity.Tarea;
import com.Taller1.Taller1.Repository.TareaRepository;
import com.Taller1.Taller1.Service.TareaService;

class TareaServiceTest {

    @Mock
    private TareaRepository tareaRepository;

    @InjectMocks
    private TareaService tareaService;

    private Tarea tarea;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tarea = new Tarea();
        tarea.setId(1L);
        tarea.setTitulo("Tarea de prueba");
        tarea.setDescripcion("Descripción de prueba");
        tarea.setEstado("PENDIENTE");
    }

    @Test
    void crearTarea_conTituloValido_debeGuardarTarea() {
        when(tareaRepository.save(any(Tarea.class))).thenReturn(tarea);

        Tarea resultado = tareaService.crearTarea(tarea);

        assertNotNull(resultado);
        assertEquals("Tarea de prueba", resultado.getTitulo());
        verify(tareaRepository, times(1)).save(tarea);
    }

    @Test
    void crearTarea_conIdExistente_debeLanzarExcepcion() {
        when(tareaRepository.existsById(tarea.getId())).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            tareaService.crearTarea(tarea);
        });

        assertEquals("La tarea con ID " + tarea.getId() + " ya existe", exception.getReason());
        verify(tareaRepository, never()).save(any());
    }

    @Test
    void crearTarea_conTituloVacio_debeLanzarExcepcion() {
        tarea.setTitulo("");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            tareaService.crearTarea(tarea);
        });

        assertEquals("El título de la tarea no puede estar vacío", exception.getReason());
        verify(tareaRepository, never()).save(any());
    }

    @Test
    void crearTarea_conTituloNull_debeLanzarExcepcion() {
        tarea.setTitulo(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            tareaService.crearTarea(tarea);
        });

        assertEquals("El título de la tarea no puede estar vacío", exception.getReason());
    }

    @Test
    void crearTarea_conTituloMuyLargo_debeLanzarExcepcion() {
        tarea.setTitulo("A".repeat(101));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            tareaService.crearTarea(tarea);
        });

        assertEquals("El título de la tarea no puede exceder 100 caracteres", exception.getReason());
    }

    @Test
    void crearTarea_conTituloEnLimite_debeGuardarCorrectamente() {
        tarea.setTitulo("A".repeat(100));
        when(tareaRepository.save(any(Tarea.class))).thenReturn(tarea);

        Tarea resultado = tareaService.crearTarea(tarea);

        assertNotNull(resultado);
        assertEquals(100, resultado.getTitulo().length());
        verify(tareaRepository, times(1)).save(tarea);
    }

    @Test
    void crearTarea_conDescripcionNull_debeGuardarCorrectamente() {
        tarea.setDescripcion(null);
        when(tareaRepository.save(any(Tarea.class))).thenReturn(tarea);

        Tarea resultado = tareaService.crearTarea(tarea);

        assertNotNull(resultado);
        assertNull(resultado.getDescripcion());
    }

    @Test
    void crearTarea_conFechaVencimiento_debeGuardarCorrectamente() {
        LocalDate fecha = LocalDate.now().plusDays(5);
        tarea.setFechaVencimiento(fecha);
        when(tareaRepository.save(any(Tarea.class))).thenReturn(tarea);

        Tarea resultado = tareaService.crearTarea(tarea);

        assertNotNull(resultado);
        assertEquals(fecha, resultado.getFechaVencimiento());
    }

    @Test
    void obtenerTodas_debeRetornarListaDeTareas() {
        Tarea tarea1 = new Tarea(1L, "Tarea 1", "Desc 1", LocalDate.now(), "PENDIENTE");
        Tarea tarea2 = new Tarea(2L, "Tarea 2", "Desc 2", LocalDate.now().plusDays(1), "COMPLETADA");
        when(tareaRepository.findAll()).thenReturn(Arrays.asList(tarea1, tarea2));

        List<Tarea> resultado = tareaService.obtenerTodas();

        assertEquals(2, resultado.size());
        verify(tareaRepository, times(1)).findAll();
    }

    @Test
    void obtenerPorId_debeRetornarTareaSiExiste() {
        Tarea tarea = new Tarea(1L, "Tarea 1", "Desc 1", LocalDate.now(), "PENDIENTE");
        when(tareaRepository.findById(1L)).thenReturn(Optional.of(tarea));

        Optional<Tarea> resultado = tareaService.obtenerPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Tarea 1", resultado.get().getTitulo());
    }

    @Test
    void filtrarPorEstado_debeRetornarSoloLasTareasConEseEstado() {
        Tarea tarea = new Tarea(1L, "Tarea 1", "Desc 1", LocalDate.now(), "PENDIENTE");
        when(tareaRepository.findByEstado("PENDIENTE")).thenReturn(List.of(tarea));

        List<Tarea> resultado = tareaService.filtrarPorEstado("PENDIENTE");

        assertEquals(1, resultado.size());
        assertEquals("PENDIENTE", resultado.get(0).getEstado());
    }

    @Test
    void filtrarPorRangoFechas_debeRetornarTareasEnEseRango() {
        LocalDate inicio = LocalDate.of(2025, 9, 1);
        LocalDate fin = LocalDate.of(2025, 9, 30);

        Tarea tarea = new Tarea(1L, "Tarea Rango", "Desc", LocalDate.of(2025, 9, 15), "PENDIENTE");

        when(tareaRepository.findByFechaVencimientoBetween(inicio, fin)).thenReturn(List.of(tarea));

        List<Tarea> resultado = tareaService.filtrarPorRangoFechas(inicio, fin);

        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getFechaVencimiento().isAfter(inicio.minusDays(1)));
    }

    @Test
    void filtrarPorSemana_debeRetornarTareasDeLaSemanaIndicada() {
        LocalDate fechaSemana38 = LocalDate.of(2025, 9, 18); // Semana 38

        Tarea tarea = new Tarea(1L, "Tarea Semana 38", "Desc", fechaSemana38, "PENDIENTE");
        when(tareaRepository.findAll()).thenReturn(List.of(tarea));

        List<Tarea> resultado = tareaService.filtrarPorSemana(38);

        assertEquals(1, resultado.size());
        assertEquals("Tarea Semana 38", resultado.get(0).getTitulo());
    }

    @Test
    void editarTarea_existente_debeActualizarCampos() {
        Tarea tarea = new Tarea(1L, "Original", "Desc", LocalDate.now(), "PENDIENTE");
        LocalDate nuevaFecha = LocalDate.now().plusDays(3);
        when(tareaRepository.findById(1L)).thenReturn(Optional.of(tarea));
        when(tareaRepository.save(any(Tarea.class))).thenReturn(tarea);

        Tarea resultado = tareaService.editarTarea(1L, "Título Editado", "Desc Editada", nuevaFecha);

        assertEquals("Título Editado", resultado.getTitulo());
        assertEquals("Desc Editada", resultado.getDescripcion());
        assertEquals(nuevaFecha, resultado.getFechaVencimiento());
        verify(tareaRepository, times(1)).save(tarea);
    }

    @Test
    void editarTarea_inexistente_debeLanzarNotFound() {
        when(tareaRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> tareaService.editarTarea(99L, "Título", "Desc", LocalDate.now()));

        assertEquals("Tarea no encontrada", ex.getReason());
        verify(tareaRepository, never()).save(any());
    }

    @Test
    void editarTarea_conTituloVacio_debeLanzarBadRequest() {
        Tarea tarea = new Tarea(1L, "Original", "Desc", LocalDate.now(), "PENDIENTE");
        when(tareaRepository.findById(1L)).thenReturn(Optional.of(tarea));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> tareaService.editarTarea(1L, "", "Desc", LocalDate.now()));

        assertEquals("El título de la tarea no puede estar vacío", ex.getReason());
        verify(tareaRepository, never()).save(any());
    }

    @Test
    void editarTarea_conTituloNull_debeLanzarBadRequest() {
        Tarea tarea = new Tarea(1L, "Original", "Desc", LocalDate.now(), "PENDIENTE");
        when(tareaRepository.findById(1L)).thenReturn(Optional.of(tarea));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> tareaService.editarTarea(1L, null, "Desc", LocalDate.now()));

        assertEquals("El título de la tarea no puede estar vacío", ex.getReason());
        verify(tareaRepository, never()).save(any());
    }

    @Test
    void editarTarea_conTituloMuyLargo_debeLanzarBadRequest() {
        Tarea tarea = new Tarea(1L, "Original", "Desc", LocalDate.now(), "PENDIENTE");
        when(tareaRepository.findById(1L)).thenReturn(Optional.of(tarea));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> tareaService.editarTarea(1L, "A".repeat(101), "Desc", LocalDate.now()));

        assertEquals("El título de la tarea no puede exceder 100 caracteres", ex.getReason());
        verify(tareaRepository, never()).save(any());
    }

    @Test
    void editarTarea_sinDescripcion_debeActualizarCorrectamente() {
        Tarea tarea = new Tarea(1L, "Original", "Desc", LocalDate.now(), "PENDIENTE");
        LocalDate nuevaFecha = LocalDate.now().plusDays(2);
        when(tareaRepository.findById(1L)).thenReturn(Optional.of(tarea));
        when(tareaRepository.save(any(Tarea.class))).thenReturn(tarea);

        Tarea resultado = tareaService.editarTarea(1L, "Título Editado", null, nuevaFecha);

        assertEquals("Título Editado", resultado.getTitulo());
        assertNull(resultado.getDescripcion());
        assertEquals(nuevaFecha, resultado.getFechaVencimiento());
    }
    void actualizarEstado_aCompletadaDebeRegistrarFecha() {
        Tarea tarea = new Tarea(1L, "Tarea 1", "Desc", LocalDate.now(), "PENDIENTE");
        when(tareaRepository.findById(1L)).thenReturn(Optional.of(tarea));
        when(tareaRepository.save(any(Tarea.class))).thenAnswer(inv -> inv.getArgument(0));

        Tarea resultado = tareaService.actualizarEstado(1L, "COMPLETADA");

        assertEquals("COMPLETADA", resultado.getEstado());
        assertNotNull(resultado.getFechaFinalizacion());
        verify(tareaRepository).save(tarea);
    }

    @Test
    void actualizarEstado_aPendienteDebeLimpiarFechaFinalizacion() {
        Tarea tarea = new Tarea(2L, "Tarea 2", "Desc", LocalDate.now(), "COMPLETADA");
        tarea.setFechaFinalizacion(LocalDate.now().minusDays(1));

        when(tareaRepository.findById(2L)).thenReturn(Optional.of(tarea));
        when(tareaRepository.save(any(Tarea.class))).thenAnswer(inv -> inv.getArgument(0));

        Tarea resultado = tareaService.actualizarEstado(2L, "PENDIENTE");

        assertEquals("PENDIENTE", resultado.getEstado());
        assertNull(resultado.getFechaFinalizacion());
    }

    @Test
    void actualizarEstado_siNoExisteDebeLanzarExcepcion() {
        when(tareaRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            tareaService.actualizarEstado(99L, "COMPLETADA");
        });
        assertNotNull(thrown);
    }
}
