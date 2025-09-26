# Checklist de Revisión de Código - PR #<8>

**Título del PR: feature/02-marcar-estado**  
**Autor/a: Miguel Angel Veloza Ortiz**  
**Revisores/as: Yahir Camilo Forero Santos y Luis Miguel Polo**  
**Fecha de revisión: 25/09/2025** 

---

## 1. Legibilidad

- [X] Nombres de variables y funciones claros y consistentes.
- [X] Funciones concisas y bien estructuradas.
- [X] Comentarios solo donde agreguen valor.
- [X] Código modular organizado correctamente (`src/`).


## 2. Casos borde / robustez
- [X] ¿Qué ocurre si una tarea cambia de COMPLETADA a PENDIENTE? El service limpia la fechaFinalizacion poniéndola en null, y existe un test que lo cubre.



## 3. Evidencias y Pruebas

- [X] Incluye al menos una prueba ejecutable en `/tests`.
- [X] Todas las pruebas pasan correctamente.

> Observaciones:  
> Dado que se ejecuta una prueba estatica no es necesario adjuntar una evidencia.


## 5. Resumen y Decisión

**Estado final del PR:**  

- [X] Mergeado  
- [ ] Cambios solicitados  

**Comentarios finales:**
> El método presenta nombres inconsistentes: en el controller se usa cambiarEstado mientras que en el service se emplea actualizarEstado. Esto puede generar confusión al mantener el código. Se sugiere unificar ambos nombres bajo actualizarEstado para mantener coherencia y claridad en toda la capa de la aplicación.

## 6. Defectos detectados

| ID       | Archivo        | Línea | Tipo       | Severidad | Estado    | Estado |
|----------|----------------|-------|------------|-----------|-----------|--------|
| DEF-001  | src/main/java/com/Taller1/Taller1/Controller/TareaController.java | 43 | estilo | baja | ACEPTADO |En el controller se usa cambiarEstado mientras que en el service se emplea actualizarEstado.|

> Detalle completo en `docs/defects.csv`
