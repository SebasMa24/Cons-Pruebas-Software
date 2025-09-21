package com.Taller1.Taller1;

import com.Taller1.Taller1.Entity.Tarea;
import com.Taller1.Taller1.Repository.TareaRepository;
import com.Taller1.Taller1.Service.TareaService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TareaServiceTest {

    @Mock
    private TareaRepository tareaRepository;

    @InjectMocks
    private TareaService tareaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
}
