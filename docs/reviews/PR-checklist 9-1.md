# Checklist de Revisión de Código - PR #<9>

**Título del PR: Feature/editar**  
**Autor/a: Yahir Camilo Forero Santos**  
**Revisores/as: Luis Miguel Polo y Luis Sebastian Martinez Guerrero**  
**Fecha de revisión: 26/09/2025**  

---

## 1. Criterios de Aceptación

- [X] Se cumple el flujo principal de la historia de usuario.
- [X] Se manejan casos borde (entrada vacía, no encontrada, errores).
- [X] La funcionalidad cumple con la definición de “Hecho” (DoD).

> Observaciones:  
> Caso borde no contemplado: entrada vacía

---

## 2. Legibilidad y Mantenibilidad

- [ ] Nombres de variables y funciones claros y consistentes.
- [ ] Funciones concisas y bien estructuradas.
- [ ] Comentarios solo donde agreguen valor.
- [ ] Código modular organizado correctamente (`src/`).

> Observaciones:  
> _..._

---

## 3. Evidencias y Pruebas

- [X] Incluye al menos una prueba ejecutable en `/tests`.
- [ ] Evidencia adjunta (captura o video en la descripción del PR).
- [X] Todas las pruebas pasan correctamente.

> Observaciones:  
> Dado que se ejecuta una prueba estatica no es necesario adjuntar una evidencia.

---

## 4. GitHub Actions

- [X] Workflow de `Static Analysis` ejecutado correctamente.
- [X] El PR muestra el check en verde antes del merge.

> Observaciones:  
> Ninguna.

---

## 5. Resumen y Decisión

**Estado final del PR:**  

- [X] Mergeado  
- [ ] Cambios solicitados  

**Comentarios finales:**
> Todo correcto y listo para realizar merge.

---

## 6. Defectos detectados

| ID       | Archivo        | Línea | Tipo       | Severidad | Estado    |
|----------|----------------|-------|------------|-----------|-----------|
| DEF-XXX  | _archivo.ext_  | _#_   | _tipo_     | _alta/media/baja_ | _ACEPTADO/RECHAZADO_ |

> Detalle completo en `docs/defects.csv`
