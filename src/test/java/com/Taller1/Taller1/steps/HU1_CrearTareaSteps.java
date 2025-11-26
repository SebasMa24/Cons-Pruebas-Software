package com.Taller1.Taller1.steps;

import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import com.Taller1.Taller1.Entity.Tarea;
import com.Taller1.Taller1.Repository.TareaRepository;
import com.Taller1.Taller1.Service.TareaService;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class HU1_CrearTareaSteps {

    @Autowired
    private TareaService tareaService;

    @Autowired
    private TareaRepository tareaRepository;

    private ResponseEntity<?> response;
    private Tarea tareaCreada;

    // Reutilizable para todos los escenarios de creación
    @Dado("que el usuario intenta crear una nueva tarea")
    public void que_el_usuario_intenta_crear_una_nueva_tarea() {
        tareaRepository.deleteAll(); // limpiar base de datos antes de cada escenario
    }

    @Dado("que el usuario ha creado una tarea previamente con el título {string}")
    public void que_el_usuario_ha_creado_una_tarea_previamente(String titulo) {
        Tarea tarea = new Tarea();
        tarea.setTitulo(titulo);
        tarea.setDescripcion("Descripción de prueba");
        tarea.setEstado("PENDIENTE");
        tareaService.crearTarea(tarea);
    }

    @Cuando("el usuario envía una solicitud con el título {string}")
    public void el_usuario_envia_una_solicitud_con_el_titulo(String titulo) {
        Tarea tarea = new Tarea();
        tarea.setTitulo(titulo);
        tarea.setDescripcion("Descripción automática");

        try {
            tareaCreada = tareaService.crearTarea(tarea);
            response = ResponseEntity.status(HttpStatus.CREATED).body(tareaCreada);
        } catch (Exception e) {
            response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Cuando("el usuario envía una solicitud sin título")
    public void el_usuario_envia_una_solicitud_sin_titulo() {
        Tarea tarea = new Tarea();
        tarea.setTitulo("");
        tarea.setDescripcion("Descripción sin título");

        try {
            tareaService.crearTarea(tarea);
            response = ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Cuando("el usuario envía un título que excede los 100 caracteres")
    public void el_usuario_envia_un_titulo_que_excede_los_100_caracteres() {
        String tituloLargo = "A".repeat(101); // 101 caracteres
        Tarea tarea = new Tarea();
        tarea.setTitulo(tituloLargo);
        tarea.setDescripcion("Descripción con título largo");

        try {
            tareaService.crearTarea(tarea);
            response = ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Cuando("el usuario consulta la lista de tareas mediante el endpoint correspondiente")
    public void el_usuario_consulta_la_lista_de_tareas() {
        List<Tarea> tareas = tareaService.obtenerTodas();
        response = ResponseEntity.ok(tareas);
    }

    @Entonces("el sistema debe crear la tarea y devolver una respuesta con el identificador y el título {string}")
    public void el_sistema_crea_la_tarea_y_devuelve_identificador_y_titulo(String tituloEsperado) {
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(tareaCreada);
        assertNotNull(tareaCreada.getId());
        assertEquals(tituloEsperado, tareaCreada.getTitulo());
        assertEquals("PENDIENTE", tareaCreada.getEstado());
    }

    @SuppressWarnings("null")
    @Entonces("el sistema debe devolver un error indicando {string}")
    public void el_sistema_devuelve_un_error_indicando(String mensajeEsperado) {
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains(mensajeEsperado));
    }

    @SuppressWarnings("unchecked")
    @Entonces("el sistema debe retornar la tarea con el título {string}, garantizando su persistencia")
    public void el_sistema_retorna_la_tarea_garantizando_persistencia(String titulo) {
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Tarea> tareas = (List<Tarea>) response.getBody();
        @SuppressWarnings("null")
        boolean encontrada = tareas.stream().anyMatch(t -> t.getTitulo().equals(titulo));
        assertTrue(encontrada, "La tarea '" + titulo + "' debería persistir");
    }

    @SuppressWarnings("null")
    @Entonces("el sistema debe devolver un mensaje de advertencia indicando que el título es demasiado largo")
    public void el_sistema_devuelve_mensaje_advertencia_titulo_largo() {
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("no puede exceder 100 caracteres"));
    }
}
