package com.Taller1.Taller1.steps;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.beans.factory.annotation.Autowired;

import com.Taller1.Taller1.Entity.Tarea;
import com.Taller1.Taller1.Repository.TareaRepository;
import com.Taller1.Taller1.Service.TareaService;

import io.cucumber.java.Before;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;

public class HU3_EditarTareaSteps {

    @Autowired
    private TareaService tareaService;

    @Autowired
    private TareaRepository tareaRepository;

    private Tarea tareaActual;
    private Exception excepcionCapturada;

    @Before
    public void limpiarBaseDatos() {
        tareaRepository.deleteAll();
        excepcionCapturada = null;
    }

    // Escenario 1: Actualización del contenido
    @Dado("que existe una tarea registrada con título {string}")
    public void queExisteUnaTareaRegistradaConTitulo(String titulo) {
        Tarea tarea = new Tarea();
        tarea.setTitulo(titulo);
        tarea.setEstado("PENDIENTE");
        tareaActual = tareaService.crearTarea(tarea);
        assertNotNull(tareaActual.getId(), "La tarea no fue creada correctamente");
    }

    @Cuando("el sistema recibe una solicitud de edición con título {string}")
    public void elSistemaRecibeSolicitudDeEdicionConTitulo(String nuevoTitulo) {
        tareaActual = tareaService.editarTarea(
                tareaActual.getId(),
                nuevoTitulo,
                tareaActual.getDescripcion(),
                tareaActual.getFechaVencimiento(),
                tareaActual.getRecordatorio()
        );
    }

    @Entonces("debe actualizar la tarea y confirmar que los cambios fueron aplicados")
    public void debeActualizarLaTareaYConfirmarCambios() {
        assertNotNull(tareaActual, "La tarea actualizada no debe ser nula");
        Tarea tareaBD = tareaService.obtenerPorId(tareaActual.getId()).get();
        assertEquals(tareaActual.getTitulo(), tareaBD.getTitulo(), 
                "El título debe coincidir con el actualizado");
    }

    // Escenario 2: Título inválido (vacío)
    @Cuando("se intenta editar la tarea con un título vacío")
    public void seIntentaEditarLaTareaConTituloVacio() {
        try {
            tareaService.editarTarea(
                    tareaActual.getId(),
                    "",
                    tareaActual.getDescripcion(),
                    tareaActual.getFechaVencimiento(),
                    tareaActual.getRecordatorio()
            );
        } catch (Exception e) {
            excepcionCapturada = e;
        }
    }

    @Entonces("el sistema debe devolver un error indicando que el título no puede estar vacío")
    public void elSistemaDebeRetornarErrorTituloVacio() {
        assertNotNull(excepcionCapturada, "Debe lanzarse una excepción");
        assertTrue(
                excepcionCapturada.getMessage().toLowerCase().contains("título") 
                || excepcionCapturada.getMessage().toLowerCase().contains("vacío")
                || excepcionCapturada.getMessage().toLowerCase().contains("requerido"),
                "El mensaje de error debe indicar que el título no puede estar vacío"
        );
    }

    // Escenario 3: Título nulo
    @Cuando("se intenta editar la tarea con un título nulo")
    public void seIntentaEditarLaTareaConTituloNulo() {
        try {
            tareaService.editarTarea(
                    tareaActual.getId(),
                    null,
                    tareaActual.getDescripcion(),
                    tareaActual.getFechaVencimiento(),
                    tareaActual.getRecordatorio()
            );
        } catch (Exception e) {
            excepcionCapturada = e;
        }
    }

    // Escenario 4: Actualización de fecha límite
    @Dado("que existe una tarea registrada con título {string} y fecha de vencimiento {string}")
    public void queExisteUnaTareaConTituloYFecha(String titulo, String fechaVencimiento) {
        Tarea tarea = new Tarea();
        tarea.setTitulo(titulo);
        tarea.setEstado("PENDIENTE");
        tarea.setFechaVencimiento(LocalDate.parse(fechaVencimiento));
        tareaActual = tareaService.crearTarea(tarea);
        assertNotNull(tareaActual.getId(), "La tarea no fue creada correctamente");
    }

    @Cuando("se recibe una solicitud de edición con fecha de vencimiento {string}")
    public void seRecibeSolicitudConFechaVencimiento(String nuevaFecha) {
        tareaActual = tareaService.editarTarea(
                tareaActual.getId(),
                tareaActual.getTitulo(),
                tareaActual.getDescripcion(),
                LocalDate.parse(nuevaFecha),
                tareaActual.getRecordatorio()
        );
    }

    @Entonces("el sistema debe almacenar esa fecha correctamente")
    public void elSistemaDebeAlmacenarFechaCorrectamente() {
        assertNotNull(tareaActual, "La tarea actualizada no debe ser nula");
        Tarea tareaBD = tareaService.obtenerPorId(tareaActual.getId()).get();
        assertNotNull(tareaBD.getFechaVencimiento(), 
                "La fecha de vencimiento debe estar almacenada");
        assertEquals(tareaActual.getFechaVencimiento(), tareaBD.getFechaVencimiento(),
                "La fecha almacenada debe coincidir con la actualizada");
    }

    // Escenario 5: Persistencia de cambios
    @Dado("que una tarea con título {string} fue editada a {string}")
    public void queUnaTareaFueEditada(String tituloOriginal, String tituloEditado) {
        Tarea tarea = new Tarea();
        tarea.setTitulo(tituloOriginal);
        tarea.setEstado("PENDIENTE");
        tareaActual = tareaService.crearTarea(tarea);

        tareaActual = tareaService.editarTarea(
                tareaActual.getId(),
                tituloEditado,
                tareaActual.getDescripcion(),
                tareaActual.getFechaVencimiento(),
                tareaActual.getRecordatorio()
        );
    }

    @Cuando("se consulta nuevamente la lista de tareas")
    public void seConsultaNuevamenteLaLista() {
        tareaActual = tareaService.obtenerPorId(tareaActual.getId()).get();
    }

    @Entonces("la tarea debe reflejar los datos actualizados")
    public void laTareaDebeReflejarDatosActualizados() {
        assertNotNull(tareaActual, "La tarea debe existir");
        assertTrue(tareaActual.getTitulo().contains("Final") 
                || tareaActual.getTitulo().contains("Examen"),
                "La tarea debe contener los datos actualizados");
    }
}