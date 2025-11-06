package com.Taller1.Taller1.steps;

import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import com.Taller1.Taller1.Entity.Tarea;
import com.Taller1.Taller1.Repository.TareaRepository;
import com.Taller1.Taller1.Service.TareaService;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class TareaSteps {

    @LocalServerPort
    private int port;

    @Autowired
    private TareaService tareaService;

    @Autowired
    private TareaRepository tareaRepository;

    private ResponseEntity<?> response;
    private Tarea tareaCreada;

    @Dado("que el usuario está en la pantalla principal")
    public void que_el_usuario_esta_en_la_pantalla_principal() {
        // Limpiar base de datos para tests
        tareaRepository.deleteAll();
    }

    @Dado("que el usuario está creando una nueva tarea")
    public void que_el_usuario_esta_creando_una_nueva_tarea() {

    }

    @Dado("que el usuario ha agregado una tarea con título {string}")
    public void que_el_usuario_ha_agregado_una_tarea_con_titulo(String titulo) {
        
        Tarea tarea = new Tarea();
        tarea.setTitulo(titulo);
        tarea.setDescripcion("Descripción de prueba");
        tarea.setEstado("PENDIENTE");
        
        tareaService.crearTarea(tarea);
    }

    @Cuando("el usuario ingresa el título {string} y hace clic en {string}")
    public void el_usuario_ingresa_el_titulo_y_hace_clic_en(String titulo, String boton) {
        
        Tarea tareaNueva = new Tarea();
        tareaNueva.setTitulo(titulo);
        tareaNueva.setDescripcion("Descripción automática");
        tareaNueva.setEstado("PENDIENTE");
        
        try {
            tareaCreada = tareaService.crearTarea(tareaNueva);
            response = ResponseEntity.status(HttpStatus.CREATED).body(tareaCreada);
        } catch (Exception e) {
            response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Cuando("el usuario intenta agregar una tarea sin ingresar título")
    public void el_usuario_intenta_agregar_una_tarea_sin_ingresar_titulo() {
        Tarea tareaSinTitulo = new Tarea();
        tareaSinTitulo.setTitulo("");
        tareaSinTitulo.setDescripcion("Descripción sin título");
        
        try {
            tareaService.crearTarea(tareaSinTitulo);
            response = ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Cuando("el usuario recarga la página")
    public void el_usuario_recarga_la_pagina() {
        // Simular recarga obteniendo todas las tareas
        List<Tarea> tareas = tareaService.obtenerTodas();
        response = ResponseEntity.ok(tareas);
    }

    @Cuando("el usuario ingresa un título de más de {int} caracteres")
    public void el_usuario_ingresa_un_titulo_de_mas_de_caracteres(Integer longitud) {
        String tituloLargo = "A".repeat(longitud + 1); // Crear string más largo del límite
        
        Tarea tareaTituloLargo = new Tarea();
        tareaTituloLargo.setTitulo(tituloLargo);
        tareaTituloLargo.setDescripcion("Descripción con título largo");
        
        try {
            tareaService.crearTarea(tareaTituloLargo);
            response = ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Entonces("la tarea se agrega a la lista de tareas pendientes")
    public void la_tarea_se_agrega_a_la_lista_de_tareas_pendientes() {
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(tareaCreada);
        assertNotNull(tareaCreada.getId());
        assertEquals("PENDIENTE", tareaCreada.getEstado());
    }

    @Entonces("la tarea visible tiene el título {string}")
    public void la_tarea_visible_tiene_el_titulo(String tituloEsperado) {
        assertNotNull(tareaCreada);
        assertEquals(tituloEsperado, tareaCreada.getTitulo());
        
        // Verificar que también existe en la base de datos
        List<Tarea> tareas = tareaService.filtrarPorEstado("PENDIENTE");
        boolean tareaEncontrada = tareas.stream()
            .anyMatch(t -> t.getTitulo().equals(tituloEsperado));
        assertTrue(tareaEncontrada, "La tarea debería estar en la lista de pendientes");
    }

    @SuppressWarnings("null")
    @Entonces("el sistema muestra un mensaje de error {string}")
    public void el_sistema_muestra_un_mensaje_de_error(String mensajeEsperado) {
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains(mensajeEsperado));
    }

    @Entonces("la tarea {string} aparece en la lista de tareas pendientes")
    public void la_tarea_aparece_en_la_lista_de_tareas_pendientes(String titulo) {
        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        List<Tarea> tareas = tareaService.obtenerTodas();
        boolean tareaEncontrada = tareas.stream()
            .anyMatch(t -> t.getTitulo().equals(titulo));
        assertTrue(tareaEncontrada, "La tarea '" + titulo + "' debería persistir después de recargar");
    }

    @SuppressWarnings("null")
    @Entonces("el sistema muestra un mensaje de advertencia {string}")
    public void el_sistema_muestra_un_mensaje_de_advertencia(String mensajeEsperado) {
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains(mensajeEsperado));
    }
}