package com.Taller1.Taller1.steps;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.springframework.beans.factory.annotation.Autowired;

import com.Taller1.Taller1.Entity.Tarea;
import com.Taller1.Taller1.Repository.TareaRepository;
import com.Taller1.Taller1.Service.TareaService;

import io.cucumber.java.Before;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;

public class HU2_MarcarTareaEstadoSteps {

    @Autowired
    private TareaService tareaService;

    @Autowired
    private TareaRepository tareaRepository;

    private Tarea tarea;

    @Before
    public void limpiarBaseDatos() {
        tareaRepository.deleteAll();
    }

    // Antecedentes
    @Dado("que existe una tarea en la lista con estado {string}")
    @Dado("que existe una tarea con estado {string}")
    public void queExisteUnaTareaEnLaListaConEstado(String estado) {
        // Crear tarea con estado por defecto (crearTarea siempre inicializa a PENDIENTE)
        tarea = new Tarea(null, "Tarea Prueba", "Descripcion", LocalDate.now().plusDays(3), null);
        tarea = tareaService.crearTarea(tarea);
        assertNotNull(tarea.getId());
        // Si se solicita un estado distinto a PENDIENTE, actualizarlo mediante el servicio
        if (!"PENDIENTE".equalsIgnoreCase(estado)) {
            tarea = tareaService.actualizarEstado(tarea.getId(), estado);
        }
        assertEquals(estado, tarea.getEstado());
    }

    // Escenario 1
    @Cuando("el sistema recibe una solicitud para cambiar el estado de la tarea a {string}")
    public void elSistemaRecibeSolicitudParaCambiarEstado(String nuevoEstado) {
        tarea = tareaService.actualizarEstado(tarea.getId(), nuevoEstado);
    }

    @Entonces("la tarea debe tener estado {string} en la base de datos")
    public void laTareaDebeTenerEstadoEnLaBaseDeDatos(String estado) {
        assertEquals(estado, tarea.getEstado());
    }

    @Y("la fecha de finalización debe estar registrada")
    public void laFechaDeFinalizacionDebeEstarRegistrada() {
        assertNotNull(tarea.getFechaFinalizacion());
    }

    // Escenario 2
    @Dado("que la tarea ha sido marcada como {string}")
    public void queLaTareaHaSidoMarcadaComo(String estado) {
        tarea = tareaService.actualizarEstado(tarea.getId(), estado);
        assertEquals(estado, tarea.getEstado());
    }

    @Cuando("la tarea se consulte en el sistema")
    public void laTareaSeConsulteEnElSistema() {
        // Comprobación sencilla: el estado ya está en la entidad 'tarea'
        assertNotNull(tarea.getEstado());
    }

    // Escenario 3
    @Dado("que la tarea está en estado {string}")
    public void queLaTareaEstaEnEstado(String estado) {
        tarea = new Tarea(null, "Tarea Prueba Estado", "Desc", LocalDate.now().plusDays(1), null);
        tarea = tareaService.crearTarea(tarea);
        if (!"PENDIENTE".equalsIgnoreCase(estado)) {
            tarea = tareaService.actualizarEstado(tarea.getId(), estado);
        }
        assertEquals(estado, tarea.getEstado());
    }

    @Cuando("el usuario la marca como {string}")
    public void elUsuarioLaMarcaComo(String estado) {
        tarea = tareaService.actualizarEstado(tarea.getId(), estado);
    }

    @Entonces("el sistema debe guardar la fecha de finalización de la tarea")
    public void elSistemaDebeGuardarLaFechaDeFinalizacion() {
        assertNotNull(tarea.getFechaFinalizacion());
    }

    // Escenario 4
    @Dado("que la tarea está marcada como {string}")
    public void queLaTareaEstaMarcadaComo(String estado) {
        tarea = tareaService.actualizarEstado(tarea.getId(), estado);
        assertEquals("COMPLETADA", tarea.getEstado());
        assertNotNull(tarea.getFechaFinalizacion());
    }

    

    @Entonces("la tarea debe regresar a la sección de tareas pendientes")
    public void laTareaDebeRegresarASeccionPendientes() {
        assertEquals("PENDIENTE", tarea.getEstado());
    }

    @Y("la tarea debe mostrarse sin tachar ni diferenciación visual")
    public void laTareaDebeMostrarseSinTachar() {
        assertEquals("PENDIENTE", tarea.getEstado());
    }

    @Y("la fecha de finalización debe eliminarse")
    public void laFechaDeFinalizacionDebeEliminarse() {
        assertNull(tarea.getFechaFinalizacion());
    }

    @Entonces("la fecha de finalización debe ser nula")
    public void laFechaDeFinalizacionDebeSerNula() {
        assertNull(tarea.getFechaFinalizacion());
    }
}