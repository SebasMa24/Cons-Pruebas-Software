package com.Taller1.Taller1.steps;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.Taller1.Taller1.Entity.Tarea;
import com.Taller1.Taller1.Service.TareaService;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;

@SpringBootTest
public class HU7_EstablecerFechaYRecordatorioSteps {

    @Autowired
    private TareaService tareaService;

    private Tarea tarea;
    private List<Tarea> tareas;
    private boolean alertaGenerada;
    private boolean resaltada;
    @SuppressWarnings("unused")
    private boolean vencida;

    private final DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ======================
    // ESCENARIO 1
    // ======================
    @Dado("que el usuario crea una tarea llamada {string}")
    public void que_el_usuario_crea_una_tarea_llamada(String titulo) {
        tarea = new Tarea();
        tarea.setTitulo(titulo);
        tarea.setDescripcion("Descripción de prueba");
        tarea = tareaService.crearTarea(tarea);
        System.out.println("✅ Tarea creada: " + tarea.getTitulo());
    }

    @Cuando("establece la fecha límite {string}")
    public void establece_la_fecha_límite(String fechaStr) {
        LocalDate fecha = LocalDate.parse(fechaStr, inputFormatter);
        tarea = tareaService.editarTarea(
                tarea.getId(),
                tarea.getTitulo(),
                tarea.getDescripcion(),
                fecha,
                tarea.getRecordatorio()
        );
        System.out.println("📅 Fecha límite establecida: " + tarea.getFechaVencimiento());
    }

    @Entonces("el sistema debe mostrar la fecha {string} junto al título de la tarea")
    public void el_sistema_debe_mostrar_la_fecha_junto_al_titulo_de_la_tarea(String fechaEsperada) {
        String fechaFormateada = tarea.getFechaVencimiento().format(outputFormatter);
        System.out.println("🧾 Fecha mostrada: " + fechaFormateada);
        assertEquals(fechaEsperada, fechaFormateada);
    }

    // ======================
    // ESCENARIO 2
    // ======================
    @Dado("que existe una tarea llamada {string} con fecha límite en menos de {int} horas")
    public void que_existe_una_tarea_llamada_con_fecha_límite_en_menos_de_horas(String titulo, Integer horas) {
        tarea = new Tarea();
        tarea.setTitulo(titulo);
        tarea.setDescripcion("Tarea próxima a vencer");
        tarea.setFechaVencimiento(LocalDate.now()); // misma fecha → próxima
        tarea = tareaService.crearTarea(tarea);
        System.out.println("⚠️ Tarea creada: " + titulo);
    }

    @Cuando("el usuario ingresa nuevamente a la aplicación")
    public void el_usuario_ingresa_nuevamente_a_la_aplicación() {
        tareas = tareaService.obtenerTodas();
        var visual = tareaService.calcularEstadoVisual(tareas);
        resaltada = visual.get(tarea.getId()).equals("proxima");
    }

    @Entonces("la tarea debe mostrarse resaltada con un color o ícono de advertencia")
    public void la_tarea_debe_mostrarse_resaltada_con_un_color_o_ícono_de_advertencia() {
        assertTrue(resaltada, "❌ La tarea no se resaltó correctamente");
        System.out.println("✅ Tarea resaltada por vencerse pronto");
    }

    // ======================
    // ESCENARIO 3
    // ======================
    @Dado("que el usuario establece un recordatorio para la tarea {string} a la hora actual")
    public void que_el_usuario_establece_un_recordatorio_para_la_tarea_a_la_hora_actual(String titulo) {
        tarea = new Tarea();
        tarea.setTitulo(titulo);
        tarea.setDescripcion("Tarea con recordatorio");
        tarea.setRecordatorio(LocalDateTime.now());
        tarea = tareaService.crearTarea(tarea);
        System.out.println("🔔 Recordatorio establecido para: " + titulo);
    }

    @Cuando("llega la hora configurada del recordatorio")
    public void llega_la_hora_configurada_del_recordatorio() {
        List<Tarea> proximas = tareaService.tareasConRecordatorioProximo();
        alertaGenerada = proximas.stream().anyMatch(t -> t.getId().equals(tarea.getId()));
    }

    @Entonces("el sistema debe generar una alerta o notificación visible al usuario")
    public void el_sistema_debe_generar_una_alerta_o_notificación_visible_al_usuario() {
        assertTrue(alertaGenerada, "❌ No se generó la alerta");
        System.out.println("✅ Alerta generada correctamente");
    }

    // ======================
    // ESCENARIO 4
    // ======================
    @Dado("que existe una tarea llamada {string} cuya fecha límite ya ha pasado")
    public void que_existe_una_tarea_llamada_cuya_fecha_límite_ya_ha_pasado(String titulo) {
        tarea = new Tarea();
        tarea.setTitulo(titulo);
        tarea.setDescripcion("Tarea vencida");
        tarea.setFechaVencimiento(LocalDate.now().minusDays(1));
        tarea = tareaService.crearTarea(tarea);
        vencida = true;
        System.out.println("💀 Tarea vencida creada: " + titulo);
    }

    @Cuando("el usuario visualiza la lista de tareas")
    public void el_usuario_visualiza_la_lista_de_tareas() {
        tareas = tareaService.obtenerTodas();
    }

    @Entonces("la tarea debe mostrarse con un estilo visual distintivo \\(por ejemplo, en rojo o con la etiqueta {string})")
    public void la_tarea_debe_mostrarse_con_un_estilo_visual_distintivo_por_ejemplo_en_rojo_o_con_la_etiqueta(String etiquetaEsperada) {
        var visual = tareaService.calcularEstadoVisual(tareas);
        String estado = visual.get(tarea.getId());
        assertEquals("vencida", estado);
        System.out.println("✅ Tarea mostrada con etiqueta: " + etiquetaEsperada);
    }
}
