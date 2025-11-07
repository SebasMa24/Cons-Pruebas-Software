package com.Taller1.Taller1.steps;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.Taller1.Taller1.Entity.Tarea;
import com.Taller1.Taller1.Repository.TareaRepository;
import com.Taller1.Taller1.Service.TareaService;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.es.*;

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
        // Limpiar la base de datos antes de cada escenario
        tareaRepository.deleteAll();
        resultadoBusqueda = null;
        mensajeResultado = null;
    }

    @Dado("que existen las siguientes tareas en el sistema:")
    public void queExistenLasSiguientesTareasEnElSistema(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps();
        
        for (Map<String, String> row : rows) {
            Tarea tarea = new Tarea();
            tarea.setTitulo(row.get("titulo"));
            tarea.setDescripcion(row.get("descripcion"));
            tarea.setEstado(row.get("estado"));
            tareaRepository.save(tarea);
        }
    }

    @Cuando("el usuario busca tareas con el texto {string}")
    public void elUsuarioBuscaTareasConElTexto(String texto) {
        resultadoBusqueda = tareaService.buscarPorTitulo(texto);
        
        if (resultadoBusqueda.isEmpty()) {
            mensajeResultado = "No se encontraron tareas con ese título";
        }
    }

    @Entonces("el sistema debe mostrar {int} tarea\\(s)")
    public void elSistemaDebeMostrarTareas(int cantidadEsperada) {
        assertNotNull(resultadoBusqueda, "El resultado de la búsqueda no debe ser nulo");
        assertEquals(cantidadEsperada, resultadoBusqueda.size(), 
            "La cantidad de tareas encontradas no coincide con la esperada");
    }

    @Entonces("todas las tareas mostradas deben contener {string} en el título")
    public void todasLasTareasMostradasDebenContenerEnElTitulo(String textoEsperado) {
        assertNotNull(resultadoBusqueda, "El resultado de la búsqueda no debe ser nulo");
        assertFalse(resultadoBusqueda.isEmpty(), "La lista de resultados no debe estar vacía");
        
        for (Tarea tarea : resultadoBusqueda) {
            assertTrue(
                tarea.getTitulo().toLowerCase().contains(textoEsperado.toLowerCase()),
                "La tarea '" + tarea.getTitulo() + "' no contiene el texto '" + textoEsperado + "'"
            );
        }
    }

    @Entonces("se debe mostrar el mensaje {string}")
    public void seDebeMostrarElMensaje(String mensajeEsperado) {
        assertEquals(mensajeEsperado, mensajeResultado, 
            "El mensaje mostrado no coincide con el esperado");
    }

    @Dado("que el usuario ha buscado tareas con el texto {string}")
    public void queElUsuarioHaBuscadoTareasConElTexto(String texto) {
        elUsuarioBuscaTareasConElTexto(texto);
    }

    @Entonces("el resultado de la búsqueda muestra {int} tarea\\(s)")
    public void elResultadoDeLaBusquedaMuestraTareas(int cantidadEsperada) {
        elSistemaDebeMostrarTareas(cantidadEsperada);
    }

    @Cuando("el usuario limpia el campo de búsqueda")
    public void elUsuarioLimpiaElCampoDeBusqueda() {
        // Simular búsqueda con texto vacío (debería mostrar todas)
        resultadoBusqueda = tareaService.buscarPorTitulo("");
        mensajeResultado = null;
    }
}