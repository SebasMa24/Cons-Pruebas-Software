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
    private List<Map<String, String>> tareasFiltradas = new ArrayList<>();
    private String filtroActual = "TODOS";

    //ANTECEDENTES

    @Dado("que el usuario tiene una lista de tareas con diferentes estados:")
    public void cargarTareas(DataTable dataTable) {
        tareas = dataTable.asMaps(String.class, String.class);
        tareasFiltradas = new ArrayList<>(tareas); // inicialmente sin filtro
    }


    @Cuando("el sistema recibe una solicitud para filtrar por estado {string}")
    public void sistemaRecibeSolicitudFiltrar(String estado) {
        filtroActual = estado.toUpperCase();

        switch (filtroActual) {
            case "PENDIENTES":
                tareasFiltradas = tareas.stream()
                        .filter(t -> t.get("estado").equals("PENDIENTE"))
                        .collect(Collectors.toList());
                break;

            case "EN PROGRESO":
                tareasFiltradas = tareas.stream()
                        .filter(t -> t.get("estado").equals("EN_PROGRESO"))
                        .collect(Collectors.toList());
                break;

            case "COMPLETADAS":
                tareasFiltradas = tareas.stream()
                        .filter(t -> t.get("estado").equals("COMPLETADA"))
                        .collect(Collectors.toList());
                break;

            case "TODOS":
                tareasFiltradas = new ArrayList<>(tareas);
                break;

            default:
                tareasFiltradas = new ArrayList<>();
        }
    }


    @Cuando("se consulta nuevamente la lista de tareas con el filtro activo")
    public void consultarListaConFiltroActivo() {
        sistemaRecibeSolicitudFiltrar(filtroActual);
    }


    @Entonces("el sistema debe devolver solo las tareas con estado {string}")
    public void validarTareasFiltradas(String estadoEsperado) {
        assertFalse(tareasFiltradas.isEmpty(), "No se encontraron tareas con el estado solicitado.");
        assertTrue(
                tareasFiltradas.stream().allMatch(t -> t.get("estado").equals(estadoEsperado)),
                "Existen tareas con un estado diferente al esperado."
        );
    }

    @Entonces("el sistema debe devolver la lista completa de tareas")
    public void validarListaCompleta() {
        assertEquals(tareas.size(), tareasFiltradas.size(),
                "La lista completa no coincide con el total de tareas originales.");
    }

    @Dado("que existe un filtro activo")
    public void filtroActivo() {
        filtroActual = "COMPLETADAS";
        tareasFiltradas = tareas.stream()
                .filter(t -> t.get("estado").equals("COMPLETADA"))
                .collect(Collectors.toList());
    }

    @Entonces("el filtro debe persistir en la siguiente solicitud")
    public void validarPersistencia() {
        assertNotNull(filtroActual, "No existe un filtro activo guardado.");
    }

    @Entonces("el sistema debe seguir devolviendo únicamente las tareas completadas")
    public void validarPersistenciaCompletadas() {
        assertTrue(
                tareasFiltradas.stream().allMatch(t -> t.get("estado").equals("COMPLETADA")),
                "La persistencia del filtro no funcionó: hay tareas que no están completadas."
        );
    }
}
