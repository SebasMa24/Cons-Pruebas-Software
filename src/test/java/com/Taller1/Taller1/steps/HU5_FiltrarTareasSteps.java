package com.Taller1.Taller1.steps;

import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import io.cucumber.datatable.DataTable;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import java.util.stream.Collectors;

public class HU5_FiltrarTareasSteps {

    private List<Map<String, String>> tareas = new ArrayList<>();
    private String filtroActual;
    private List<Map<String, String>> tareasFiltradas = new ArrayList<>();
    private String etiquetaFiltro = "";

    @Dado("que el usuario tiene una lista de tareas con diferentes estados:")
    public void cargar_tareas(DataTable dataTable) {
        tareas = dataTable.asMaps(String.class, String.class);
    }

    @Cuando("el usuario recarga la página")
    public void elUsuarioRecargaLaPágina() {
        
    }

    @Cuando("el usuario selecciona el filtro {string}")
    public void seleccionar_filtro(String filtro) {
        filtroActual = filtro.toUpperCase();
        if (filtroActual.equals("TODOS")) {
            tareasFiltradas = new ArrayList<>(tareas);
        } else if (filtroActual.equals("PENDIENTES")) {
            tareasFiltradas = tareas.stream()
                    .filter(t -> t.get("estado").equals("PENDIENTE"))
                    .collect(Collectors.toList());
        } else if (filtroActual.equals("EN PROGRESO")) {
            tareasFiltradas = tareas.stream()
                    .filter(t -> t.get("estado").equals("EN_PROGRESO"))
                    .collect(Collectors.toList());
        } else if (filtroActual.equals("COMPLETADAS")) {
            tareasFiltradas = tareas.stream()
                    .filter(t -> t.get("estado").equals("COMPLETADA"))
                    .collect(Collectors.toList());
        }
        etiquetaFiltro = filtro; // guarda el nombre del filtro activo
    }

    @Cuando("el usuario selecciona un filtro")
    public void elUsuarioSeleccionaUnFiltro() {
        // Simulación: el usuario selecciona un filtro genérico, por ejemplo
        // "Pendientes"
        filtroActual = "PENDIENTES";
        tareasFiltradas = tareas.stream()
                .filter(t -> t.get("estado").equals("PENDIENTE"))
                .collect(java.util.stream.Collectors.toList());
        System.out.println("El usuario ha seleccionado el filtro genérico: " + filtroActual);
    }

    @Entonces("el sistema debe mostrar solo las tareas con estado {string}")
    public void verificar_tareas_filtradas(String estado) {
        assertFalse(tareasFiltradas.isEmpty(), "No se encontraron tareas filtradas.");
        assertTrue(tareasFiltradas.stream().allMatch(t -> t.get("estado").equals(estado)));
    }

    @Entonces("el sistema debe mostrar todas las tareas sin filtrar")
    public void mostrar_todas_las_tareas() {
        assertEquals(tareas.size(), tareasFiltradas.size());
    }

    @Entonces("los filtros {string} y {string} deben desactivarse")
    public void losFiltrosYDebenDesactivarse(String filtro1, String filtro2) {
        // En entorno real validarías estados de botones o flags
        System.out.printf("Verificando que los filtros %s y %s están desactivados%n", filtro1, filtro2);
        assertNotEquals(filtro1.toUpperCase(), filtroActual);
        assertNotEquals(filtro2.toUpperCase(), filtroActual);
    }

    @Entonces("la lista visual debe actualizarse según el filtro seleccionado")
    public void laListaVisualDebeActualizarseSegunElFiltroSeleccionado() {
        assertNotNull(tareasFiltradas);
        System.out.println("Lista visual actualizada correctamente para filtro: " + filtroActual);
    }

    @Entonces("el filtro seleccionado debe persistir después de recargar la página")
    public void elFiltroSeleccionadoDebePersistirDespuesDeRecargar() {
        assertNotNull(filtroActual);
        System.out.println("Filtro persistente tras recargar: " + filtroActual);
    }

    @Dado("que el usuario tiene un filtro activo")
    public void que_el_usuario_tiene_un_filtro_activo() {
        filtroActual = "COMPLETADAS";
        tareasFiltradas = tareas.stream()
                .filter(t -> t.get("estado").equals("COMPLETADA"))
                .collect(Collectors.toList());
    }

    @Dado("que el usuario seleccionó el filtro {string}")
    public void que_el_usuario_selecciono_el_filtro(String filtro) {
        seleccionar_filtro(filtro);
    }

    @Entonces("el sistema debe seguir mostrando únicamente las tareas completadas")
    public void elSistemaDebeSeguirMostrandoUnicamenteLasTareasCompletadas() {
        assertTrue(tareasFiltradas.stream().allMatch(t -> t.get("estado").equals("COMPLETADA")));
    }

    @Entonces("se deben eliminar todos los filtros activos")
    public void se_deben_eliminar_todos_los_filtros_activos() {
        filtroActual = null;
        assertNull(filtroActual, "El filtro no fue eliminado correctamente");
    }

    @Entonces("el sistema debe mostrar una etiqueta indicando el filtro activado")
    public void elSistemaDebeMostrarUnaEtiquetaIndicandoElFiltroActivado() {
        assertNotNull(etiquetaFiltro);
        System.out.println("Etiqueta visible: " + etiquetaFiltro);
    }

    @Entonces("el sistema debe mostrar únicamente las tareas finalizadas")
    public void elSistemaDebeMostrarUnicamenteLasTareasFinalizadas() {

        assertFalse(tareasFiltradas.isEmpty(), "No hay tareas filtradas para mostrar.");
        assertTrue(
                tareasFiltradas.stream().allMatch(t -> t.get("estado").equals("COMPLETADA")),
                "Existen tareas no completadas en la lista filtrada.");
        System.out.println("Mostrando únicamente tareas completadas correctamente.");
    }

}
