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

    @Before
    public void limpiarBaseDatos() {
        tareaRepository.deleteAll();
    }

    @Dado("que existe una tarea llamada {string} en la lista")
    public void queExisteUnaTareaLlamadaEnLaLista(String titulo) {
        Tarea tarea = new Tarea();
        tarea.setTitulo(titulo);
        tarea.setEstado("PENDIENTE");
        tareaActual = tareaService.crearTarea(tarea);

        assertNotNull(tareaActual.getId(), "La tarea no fue creada correctamente");
    }

    @Cuando("el usuario hace clic en el icono de editar junto a la tarea")
    public void elUsuarioHaceClicEnEditar() {
        assertNotNull(tareaActual, "No hay tarea para editar");
    }

    @Entonces("debe mostrarse un formulario con los datos actuales de la tarea")
    public void debeMostrarseFormularioConDatos() {
        assertNotNull(tareaActual.getTitulo(), "El título debe estar presente en el formulario");
    }

    @Dado("que el usuario está editando la tarea {string}")
    public void queElUsuarioEstaEditandoLaTarea(String titulo) {
        Tarea tarea = new Tarea();
        tarea.setTitulo(titulo);
        tarea.setEstado("PENDIENTE");
        tareaService.crearTarea(tarea);
        tareaActual = tareaRepository.findByTitulo(titulo).orElse(null);
        assertNotNull(tareaActual, "La tarea no existe en la BD");
    }

    @Cuando("el usuario cambia el título por {string} y guarda los cambios")
    public void elUsuarioCambiaElTituloYGuarda(String nuevoTitulo) {
        tareaActual = tareaService.editarTarea(
                tareaActual.getId(),
                nuevoTitulo,
                tareaActual.getDescripcion(),
                tareaActual.getFechaVencimiento(),
                tareaActual.getRecordatorio()
        );
    }

    @Entonces("la lista de tareas debe mostrar la tarea con el título {string}")
    public void laListaDebeMostrarLaTareaConElNuevoTitulo(String nuevoTitulo) {
        Tarea tareaBD = tareaService.obtenerPorId(tareaActual.getId()).get();
        assertEquals(nuevoTitulo, tareaBD.getTitulo());
    }

    @Dado("que el usuario está editando la tarea {string} con fecha de vencimiento {string}")
    public void usuarioEditandoConFecha(String titulo, String fechaVencimiento) {
        Tarea tarea = new Tarea();
        tarea.setTitulo(titulo);
        tarea.setEstado("PENDIENTE");
        tarea.setFechaVencimiento(LocalDate.parse(fechaVencimiento));
        tareaActual = tareaService.crearTarea(tarea);
    }

    @Cuando("el usuario cambia la fecha de vencimiento a {string} y guarda los cambios")
    public void cambiaFechaDeVencimiento(String nuevaFecha) {
        tareaActual = tareaService.editarTarea(
                tareaActual.getId(),
                tareaActual.getTitulo(),
                tareaActual.getDescripcion(),
                LocalDate.parse(nuevaFecha),
                tareaActual.getRecordatorio()
        );
    }

    @Entonces("la lista de tareas debe mostrar la nueva fecha de vencimiento {string}")
    public void verificarNuevaFechaEnLista(String nuevaFecha) {
        Tarea tareaBD = tareaService.obtenerPorId(tareaActual.getId()).get();
        assertEquals(LocalDate.parse(nuevaFecha), tareaBD.getFechaVencimiento());
    }

    @Dado("que la tarea {string} fue editada y guardada")
    public void queLaTareaFueEditada(String titulo) {
        Tarea tarea = new Tarea();
        tarea.setTitulo(titulo);
        tarea.setEstado("PENDIENTE");
        tareaActual = tareaService.crearTarea(tarea);

        tareaActual = tareaService.editarTarea(
                tareaActual.getId(),
                titulo + " (editada)",
                tareaActual.getDescripcion(),
                tareaActual.getFechaVencimiento(),
                tareaActual.getRecordatorio()
        );
    }

    @Cuando("el usuario recarga la página actual")
    public void recargarPagina() {
        tareaActual = tareaService.obtenerPorId(tareaActual.getId()).get();
    }

    @Entonces("la tarea debe reflejar los datos editados correctamente")
    public void verificarPersistenciaDeCambios() {
        assertTrue(tareaActual.getTitulo().contains("(editada)"));
    }
}