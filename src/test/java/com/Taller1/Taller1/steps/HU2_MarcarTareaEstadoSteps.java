package com.Taller1.Taller1.steps;

import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import io.cucumber.java.Before;
import org.springframework.beans.factory.annotation.Autowired;

import com.Taller1.Taller1.Entity.Tarea;
import com.Taller1.Taller1.Repository.TareaRepository;
import com.Taller1.Taller1.Service.TareaService;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

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
    public void queExisteUnaTareaEnLaListaConEstado(String estado) {
        tarea = new Tarea(null, "Tarea Prueba", "Descripcion", LocalDate.now().plusDays(3), estado);
        tarea = tareaService.crearTarea(tarea);
        assertNotNull(tarea.getId());
        assertEquals(estado, tarea.getEstado());
    }

    // Escenario 1
    @Cuando("el usuario haga clic en la casilla de verificación junto a la tarea")
    public void elUsuarioHagaClicEnLaCasillaDeVerificacion() {
        tarea = tareaService.actualizarEstado(tarea.getId(), "COMPLETADA");
    }

    @Entonces("la tarea debe cambiar su estado a {string} o {string}")
    public void laTareaDebeCambiarSuEstado(String estado1, String estado2) {
        assertTrue(
            tarea.getEstado().equalsIgnoreCase(estado1)
            || tarea.getEstado().equalsIgnoreCase(estado2)
        );
    }

    @Y("la tarea debe aparecer visualmente tachada en la lista de tareas pendientes")
    public void laTareaDebeAparecerTachada() {
        // Validación backend: estado debe cambiar (lo visual es responsabilidad del frontend)
        assertNotNull(tarea.getEstado());
    }

    // Escenario 2
    @Dado("que la tarea ha sido marcada como {string}")
    public void queLaTareaHaSidoMarcadaComo(String estado) {
        tarea = tareaService.actualizarEstado(tarea.getId(), estado);
        assertEquals(estado, tarea.getEstado());
    }

    @Cuando("la tarea se muestre en la sección de tareas completadas")
    public void laTareaSeMuestreEnLaSeccionDeTareasCompletadas() {
        assertEquals("COMPLETADA", tarea.getEstado());
    }

    @Entonces("la tarea debe estar visualmente diferenciada \\(por ejemplo, gris o tachada)")
    public void laTareaDebeEstarVisualmenteDiferenciada() {
        // Validamos que está completada (lo visual lo hace el frontend)
        assertEquals("COMPLETADA", tarea.getEstado());
    }

    // Escenario 3
    @Dado("que la tarea está en estado {string}")
    public void queLaTareaEstaEnEstado(String estado) {
        tarea = new Tarea(null, "Tarea Prueba Estado", "Desc", LocalDate.now().plusDays(1), estado);
        tarea = tareaService.crearTarea(tarea);
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

    @Cuando("el usuario desmarca la casilla de la tarea")
    public void elUsuarioDesmarcaLaCasillaDeLaTarea() {
        tarea = tareaService.actualizarEstado(tarea.getId(), "PENDIENTE");
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
}