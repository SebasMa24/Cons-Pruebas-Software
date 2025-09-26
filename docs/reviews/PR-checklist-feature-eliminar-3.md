# Checklist de Revisión de Código - PR #<7>

**Título del PR: Feature/eliminar**  
**Autor/a: Luis Miguel Polo**  
**Revisores/as: Luis Sebastian Martinez Guerrero y Miguel Angel Veloza**  
**Fecha de revisión:25/09/2025**  

---
### 1. Legibilidad y buenas prácticas

* [X] Los nombres de métodos y variables son claros.
* [ ] El endpoint en el `TareaController` sigue convención REST.
* [X] El código en el frontend (Thymeleaf + Tailwind) no está duplicado y es fácil de entender.
* [X] Comentarios solo donde agreguen valor.
* [X] Código modular organizado correctamente (`src/`).

### 2. Casos borde / robustez

* [X] ¿Qué ocurre si el ID de la tarea no existe? (se maneja con excepción, mensaje de error o silenciosamente).

### 3. Evidencias

* [X] Incluye al menos una prueba ejecutable en `/tests`.
* [X] Se verifica que tras recargar la página la tarea realmente no aparece.
* [X] Se muestra evidencia del backend (por ejemplo, registro en DB de que se borró).

> Observaciones:  
> Dado que se ejecuta una prueba estatica no es necesario adjuntar una evidencia.


## 5. Resumen y Decisión

**Estado final del PR:**  

- [ ] Mergeado  
- [X] Cambios solicitados  

**Comentarios finales:**
> El endpoint /eliminar no sigue del todo las convenciones REST, se podría mejorar a DELETE /tareas/{id}.

## 6. Defectos detectados
id,pr,archivo,linea,tipo,severidad,descripcion,estado,reportado_por,decidido_por,comentarios
| id      | pr   | archivo                                      | linea | tipo   | severidad | descripcion                                                                                                      | estado   | reportado_por | decidido_por | comentarios                                                          |
|---------|------|----------------------------------------------|-------|--------|-----------|------------------------------------------------------------------------------------------------------------------|----------|---------------|--------------|----------------------------------------------------------------------|
| DEF-001 | #7   | src/main/java/.../TareaController.java        | 44    | estilo | baja      | El endpoint usa `@PostMapping("/eliminar")` en lugar de `@DeleteMapping("/tareas/{id}")`, lo cual no sigue REST | ACEPTADO | @MiguelVeloza   | @LuisBuilds11        | Se recomienda cambiar a `@DeleteMapping("/tareas/{id}")` por buenas prácticas REST |

> Detalle completo en `docs/defects.csv`

