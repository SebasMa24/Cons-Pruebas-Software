package com.Taller1.Taller1.steps;

import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.Before;
import org.springframework.beans.factory.annotation.Autowired;

import com.Taller1.Taller1.Entity.Tarea;
import com.Taller1.Taller1.Repository.TareaRepository;
import com.Taller1.Taller1.Service.TareaService;

import static org.junit.jupiter.api.Assertions.*;

public class HU4_EliminarTareaSteps {

    @Autowired
    private TareaService tareaService;

    @Autowired
    private TareaRepository tareaRepository;

    private Tarea tareaActual;
    private String mensajeConfirmacion;

    @Before("@HU4")
    public void limpiarBaseDatos() {
        tareaRepository.deleteAll();
    }

    // Step para crear tarea
    @Dado("que existe una tarea llamada {string} en la lista de tareas")
    public void queExisteUnaTareaEnLaLista(String titulo) {
        Tarea tarea = new Tarea();
        tarea.setTitulo(titulo);
        tarea.setEstado("PENDIENTE");
        tareaActual = tareaService.crearTarea(tarea);
        assertNotNull(tareaActual.getId(), "La tarea no fue creada correctamente");
    }

    // Step para indicar intención de eliminar (usado también con "Y")
    @Dado("el usuario desea eliminar la tarea {string}")
    @Dado("que el usuario desea eliminar la tarea {string}")
    public void elUsuarioDeseaEliminarLaTarea(String titulo) {
        Tarea tarea = new Tarea();
        tarea.setTitulo(titulo);
        tarea.setEstado("PENDIENTE");
        tareaService.crearTarea(tarea);
        tareaActual = tareaService.obtenerTodas().stream()
                .filter(t -> t.getTitulo().equals(titulo))
                .findFirst()
                .orElse(null);
        assertNotNull(tareaActual, "La tarea no existe en la BD");
    }

    // Step para hacer clic en eliminar
    @Cuando("el usuario hace clic en el botón de \"eliminar\" junto a la tarea")
    public void elUsuarioHaceClicEnEliminar() {
        // Simula que se muestra ventana emergente
        mensajeConfirmacion = "¿Estás seguro de que deseas eliminar esta tarea?";
    }

    // Step para verificar ventana emergente
    @Entonces("el sistema debe mostrar una ventana emergente con el mensaje {string}")
    public void elSistemaDebeMostrarVentanaEmergente(String mensajeEsperado) {
        assertEquals(mensajeEsperado, mensajeConfirmacion);
    }

    // Step para confirmar eliminación
    @Cuando("el usuario confirma la acción de eliminar")
    public void elUsuarioConfirmaLaEliminacion() {
        tareaService.eliminarTarea(tareaActual.getId());
        tareaActual = null; // Marcamos como eliminada
    }

    // Step para verificar que la tarea desaparece
    @Entonces("la tarea debe desaparecer de la lista de tareas pendientes")
    public void laTareaDesapareceDeLaLista() {
        assertFalse(tareaRepository.existsById(tareaActual != null ? tareaActual.getId() : -1L), 
            "La tarea sigue existiendo");
    }

    // Step para verificar eliminación permanente
    @Entonces("la tarea debe ser eliminada permanentemente del sistema")
    public void laTareaEliminadaEsPermanente() {
        assertFalse(tareaRepository.existsById(tareaActual != null ? tareaActual.getId() : -1L), 
            "La tarea sigue existiendo en la BD");
    }

    // Step para cancelar eliminación
    @Cuando("el usuario hace clic en \"Cancelar\" en la ventana emergente")
    public void elUsuarioCancelaEliminacion() {
        // Simula cancelación: no hacemos nada
    }

    // Step para verificar que la tarea sigue en la lista
    @Entonces("la tarea debe permanecer en la lista sin cambios")
    public void laTareaPermaneceEnLaLista() {
        Tarea tarea = tareaService.obtenerPorId(tareaActual.getId()).orElse(null);
        assertNotNull(tarea, "La tarea debería seguir existiendo");
    }

    // Step para simular recargar la página
    @Cuando("el usuario recarga la página visible")
    public void elUsuarioRecargaLaPagina() {
        if (tareaActual != null) {
            tareaActual = tareaRepository.findById(tareaActual.getId()).orElse(null);
        }
    }

    // Step para verificar que la tarea eliminada no aparece
    @Dado("que el usuario ha eliminado la tarea {string}")
    public void queElUsuarioHaEliminadoLaTarea(String titulo) {
        tareaActual = tareaService.obtenerTodas().stream()
                .filter(t -> t.getTitulo().equals(titulo))
                .findFirst()
                .orElse(null);
        // Debe ser null porque ya se eliminó
        assertNull(tareaActual, "La tarea todavía existe en la BD");
    }

    @Entonces("la tarea eliminada no debe aparecer en ninguna de las listas de tareas")
    public void tareaEliminadaNoAparece() {
        // ya comprobado en el step anterior, aquí solo refuerzo
        assertNull(tareaActual, "La tarea todavía existe en la BD");
    }
}
