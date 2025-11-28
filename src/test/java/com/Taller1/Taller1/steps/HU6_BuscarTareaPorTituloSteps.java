
package com.Taller1.Taller1.steps;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.Taller1.Taller1.Entity.Tarea;
import com.Taller1.Taller1.Repository.TareaRepository;
import com.Taller1.Taller1.Service.TareaService;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;

@SpringBootTest
public class HU6_BuscarTareaPorTituloSteps {

    @Autowired
    private TareaService tareaService;

    @Autowired
    private TareaRepository tareaRepository;

    private List<Tarea> resultadoBusqueda;
    private String mensajeResultado;

    @Before
    public void limpiarBaseDeDatos() {
        tareaRepository.deleteAll();
        resultadoBusqueda = null;
        mensajeResultado = null;
    }

    // Antecedentes
    @Dado("que existen múltiples tareas registradas en el sistema:")
    public void queExistenMultiplesTareasRegistradasEnElSistema(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps();
        
        for (Map<String, String> row : rows) {
            Tarea tarea = new Tarea();
            tarea.setTitulo(row.get("titulo"));
            tarea.setDescripcion(row.get("descripcion"));
            tarea.setEstado(row.get("estado"));
            tareaRepository.save(tarea);
        }
        
        // Verificar que las tareas fueron creadas
        long cantidadTareas = tareaRepository.count();
        assertEquals(rows.size(), cantidadTareas, 
            "Las tareas no fueron registradas correctamente");
    }

    // Escenario 1: Búsqueda por coincidencia
    @Cuando("el sistema recibe una solicitud de búsqueda con el texto {string}")
    public void elSistemaRecibeSolicitudDeBusquedaConTexto(String texto) {
        resultadoBusqueda = tareaService.buscarPorTitulo(texto);
        
        if (resultadoBusqueda.isEmpty()) {
            mensajeResultado = "No se encontraron tareas";
        } else {
            mensajeResultado = null;
        }
    }

    @Entonces("debe devolver {int} tarea\\(s)")
    public void debeDevolver(int cantidadEsperada) {
        assertNotNull(resultadoBusqueda, "El resultado de la búsqueda no debe ser nulo");
        assertEquals(cantidadEsperada, resultadoBusqueda.size(), 
            "La cantidad de tareas devueltas no coincide con la esperada");
    }

    @Entonces("todas las tareas devueltas deben contener {string} en el título sin distinguir mayúsculas/minúsculas")
    public void todasLasTareasDevueltasDebenContenerEnElTitulo(String textoEsperado) {
        assertNotNull(resultadoBusqueda, "El resultado de la búsqueda no debe ser nulo");
        assertFalse(resultadoBusqueda.isEmpty(), "La lista de resultados no debe estar vacía");
        
        for (Tarea tarea : resultadoBusqueda) {
            String tituloLower = tarea.getTitulo().toLowerCase();
            String textoLower = textoEsperado.toLowerCase();
            
            assertTrue(
                tituloLower.contains(textoLower),
                String.format("La tarea '%s' no contiene el texto '%s' (sin distinguir mayúsculas/minúsculas)", 
                    tarea.getTitulo(), textoEsperado)
            );
        }
    }

    // Escenario 2: Sin coincidencias
    @Entonces("el sistema debe informar que no se encontraron tareas")
    public void elSistemaDebeInformarQueNoSeEncontraronTareas() {
        assertTrue(
            resultadoBusqueda == null || resultadoBusqueda.isEmpty(),
            "El resultado debe estar vacío cuando no hay coincidencias"
        );
        assertNotNull(mensajeResultado, "Debe existir un mensaje informativo");
        assertTrue(
            mensajeResultado.toLowerCase().contains("no se encontraron"),
            "El mensaje debe indicar que no se encontraron tareas"
        );
    }

    // Escenario 3: Restablecimiento de resultados
    @Dado("que se realizó una búsqueda previamente con el texto {string}")
    public void queSeRealizoBusquedaPreviamente(String texto) {
        elSistemaRecibeSolicitudDeBusquedaConTexto(texto);
    }

    @Entonces("la búsqueda devolvió {int} tarea\\(s)")
    public void laBusquedaDevolvio(int cantidadEsperada) {
        debeDevolver(cantidadEsperada);
    }

    @Cuando("el sistema recibe una solicitud de búsqueda con texto vacío")
    public void elSistemaRecibeSolicitudConTextoVacio() {
        resultadoBusqueda = tareaService.buscarPorTitulo("");
        mensajeResultado = null;
    }

    @Entonces("debe devolver todas las tareas disponibles")
    public void debeDevolverTodasLasTareasDisponibles() {
        assertNotNull(resultadoBusqueda, "El resultado de la búsqueda no debe ser nulo");
        
        long totalTareas = tareaRepository.count();
        assertEquals(totalTareas, resultadoBusqueda.size(),
            "Debe devolver todas las tareas cuando el texto de búsqueda está vacío");
    }

    @Entonces("el sistema debe devolver {int} tarea\\(s)")
    public void elSistemaDebeDevolver(int cantidadEsperada) {
        debeDevolver(cantidadEsperada);
    }
}