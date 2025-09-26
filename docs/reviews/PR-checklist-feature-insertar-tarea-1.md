# Checklist de Revisión de Código - PR #<6>

**Título del PR: Feature/insertar tarea**  
**Autor/a: Luis Sebastian Martinez Guerrero **  
**Revisores/as: Miguel Angel Veloza y Yahir Camilo Forero Santos**  
**Fecha de revisión:25/09/2025**  

---

### 1. Criterios de aceptacion

* [X] Agregar tarea con título válido y verla en la lista al recargar.
* [X] Se valida que el título **no sea vacío**.
* [X] Se valida que el título **no exceda 100 caracteres** y de un mensaje de advertencia.
* [X] Persistencia de tarea después de recarga
* [ ] El endpoint en `TareaController` es `POST /tareas` (correcto en REST).


### 2. Pruebas unitarias

* [X] Existe al menos un test para título vacío → falla con BAD_REQUEST.
* [X] Existe al menos un test para crear tarea
* [X] Existe al menos un test para título null → falla con BAD_REQUEST.
* [X] Existe al menos un test para título >100 caracteres → falla con BAD_REQUEST.

### 3. Casos borde adicionales

* [X] ¿Qué pasa si el título tiene exactamente 100 caracteres? (debería ser válido).
* [X] ¿Qué pasa si se envían campos adicionales en el JSON? (no debería romper el flujo).
* [X] ¿Qué pasa si no se envía `title` en el JSON? (probablemente BAD_REQUEST).
* [ ] ¿Qué pasa so se envia una nota con el mismo id?  -> Lo permite y se actualiza
 
### 4. GitHub Actions

- [X] Workflow de `Static Analysis` ejecutado correctamente.
- [X] El PR muestra el check en verde antes del merge.

> Observaciones:  
> Se ejecutaron correctamente las pruebas automaticas

## 5. Resumen y Decisión

**Estado final del PR:**  

- [ ] Mergeado  
- [x] Cambios solicitados  

**Comentarios finales:**
> - Actualmente se permite crear una tarea con un ID existente, lo que provoca que la tarea anterior se sobrescriba. Esto podría generar pérdida de datos.
> - Nota de visualización: Cuando el título de la tarea excede el límite del contenedor, el texto se desborda y afecta la presentación.
Sugerencia: Limitar la longitud visible con CSS (text-overflow: ellipsis;) o ajustar el tamaño del contenedor para mantener la UI consistente.
> - El endpoint para crear tareas está en la raíz (/), lo que puede causar conflictos con otros endpoints de la aplicación.
Sugerencia: Considerar agregar un path base en el Controller, por ejemplo @PostMapping("/tareas"), para que el endpoint sea /tareas y mantener consistencia en la API.

## 6. Defectos detectados
| id       | pr | archivo           | linea | tipo            | severidad   | descripcion                                                                 | estado        | reportado_por   |
|----------|----|-----------------|-------|----------------|------------|-----------------------------------------------------------------------------|---------------|----------------|
| DEF-001  | 6  | TareaService.java | 51   | Funcionalidad  | Alta       | Permite crear/actualizar una tarea con un ID existente, sobrescribiendo datos.| Pendiente       | MiguelVeloza   |
| DEF-002  | 6  | index.html | 26   | UI/Visual      | Baja       | El texto de tareas largas se desborda del contenedor, afectando la visualización.| Aceptado       | MiguelVeloza   |
| DEF-003  | 6  | TareaController.java | 45   | Configuración  | Media      | El endpoint para crear tareas está en la raíz `/` en lugar de `/tareas`, lo que puede generar conflictos.| Pendiente      | MiguelVeloza   |

> Detalle completo en `docs/defects.csv`
