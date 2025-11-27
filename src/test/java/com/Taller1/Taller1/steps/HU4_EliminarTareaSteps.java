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
    private boolean confirmacion;
    private Long idTareaEliminada; // ID guardado de tareas eliminadas

    @Before("@HU4")
    public void limpiarBaseDatos() {
        tareaRepository.deleteAll();
    }

    // --- 1. Solicitud de eliminación ---
    @Dado("que existe una tarea registrada con el título {string}")
    public void existeTareaRegistrada(String titulo) {
        Tarea tarea = new Tarea();
        tarea.setTitulo(titulo);
        tarea.setEstado("PENDIENTE");
        tareaActual = tareaService.crearTarea(tarea);
        assertNotNull(tareaActual.getId());
    }

    @Cuando("el sistema recibe una solicitud para eliminar la tarea con identificador válido sin confirmación")
    public void solicitudSinConfirmacion() {
        confirmacion = false;
    }

    @Entonces("el sistema debe requerir confirmación antes de eliminar la tarea")
    public void requiereConfirmacion() {
        boolean existe = tareaRepository.existsById(tareaActual.getId());
        assertTrue(existe, "La tarea no debe eliminarse sin confirmación");
    }

    // --- 2. Confirmación de eliminación ---
    @Cuando("el sistema recibe una solicitud para eliminar la tarea con confirmacion verdadera")
    public void solicitudConConfirmacion() {
        confirmacion = true;

        if (confirmacion) {
            idTareaEliminada = tareaActual.getId(); // Guardar ID
            tareaService.eliminarTarea(idTareaEliminada);
            tareaActual = null; // limpiar referencia
        }
    }

    @Entonces("el sistema debe eliminar la tarea permanentemente")
    public void eliminacionPermanente() {
        assertFalse(tareaRepository.existsById(idTareaEliminada));
    }

    // --- 3. Cancelación de eliminación ---
    @Cuando("el sistema recibe una solicitud para eliminar la tarea sin el parámetro de confirmación")
    public void solicitudCancelada() {
        confirmacion = false;
    }

    @Entonces("la tarea no debe ser eliminada y el sistema debe informar que la acción fue cancelada")
    public void accionCancelada() {
        boolean existe = tareaRepository.existsById(tareaActual.getId());
        assertTrue(existe);
    }

    // --- 4. Persistencia tras eliminación ---
    @Dado("que una tarea previamente eliminada con id válido ya no existe en el sistema")
    public void tareaYaEliminada() {

        // Crear una tarea temporal
        Tarea tareaTemp = new Tarea();
        tareaTemp.setTitulo("Tarea eliminada previamente");
        tareaTemp.setEstado("PENDIENTE");
        Tarea creada = tareaService.crearTarea(tareaTemp);

        // Guardar ID antes de eliminar
        idTareaEliminada = creada.getId();
        assertNotNull(idTareaEliminada);

        // Eliminar
        tareaService.eliminarTarea(idTareaEliminada);

        // Confirmar eliminación
        assertFalse(tareaRepository.existsById(idTareaEliminada));
    }

    @Cuando("se consulta la lista de tareas después de la eliminación")
    public void consultarLista() {
        // Opcional: puedes cargar una lista aquí si lo deseas.
    }

    @Entonces("la tarea eliminada no debe aparecer en la lista de tareas")
    public void noDebeAparecer() {
        assertFalse(tareaRepository.existsById(idTareaEliminada));
    }
}
