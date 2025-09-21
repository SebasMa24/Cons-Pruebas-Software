# Taller 1: PR por CLI + Revisión por Pares + Projects + Actions

## Autores

- **Luis Sebastián Martínez Guerrero**
- **Luis Miguel Polo H**
- **Yahir Camilo Forero Santos**
- **Miguel Angel Veloza Ortiz**

---

## Descripción del Proyecto

Este proyecto es una aplicación básica para la gestión de tareas, desarrollada en Java utilizando Spring Boot y JPA para la persistencia de datos. Su objetivo es permitir la creación, actualización, visualización y eliminación de tareas, facilitando la organización y seguimiento de pendientes.

## Estructura

```text
Taller1/
├─ src/                      # Código fuente
│  ├─ main/
│  ├─ test/                  # Pruebas básicas
├─ docs/
│  ├─ defects.csv            # Registro de defectos
│  ├─ reviews/               # Checklists por PR
│  └─ enlaces.md             # Links a Issues, PRs y Project
├─ .github/
│  └─ workflows/
│     └─ static-analysis.yml # Linter en PR
├─ .gitignore
└─ README.md
```

## Rotación de Roles

Para asegurar que todos los integrantes participen activamente en el proceso de desarrollo, revisión y documentación, se estableció una **rotación de roles**.  
Cada historia de usuario (HU) contará con:

- **Autor/a** → Implementa la HU y abre el Pull Request.  
- **Revisor/a 1** → Evalúa funcionalidad vs criterios de aceptación.  
- **Revisor/a 2** → Evalúa legibilidad y casos borde.  
- **Relator/a** → Actualiza tablero, `docs/defects.csv` y `docs/enlaces.md`.

---

### Asignación de Historias

| Historia de Usuario      | Integrante (Autor/a)             |
|--------------------------|----------------------------------|
| HU-1: Búsqueda            | Luis Sebastian Martinez Guerrero |
| HU-2: Validación          | Miguel Angel Veloza              |
| HU-3: Listado             | Yahir Camilo Forero Santos       |
| HU-4: Otra funcionalidad  | Luis Miguel Polo                  |

---

### Cuadro de Rotación

| HU    | Autor/a                      | Revisor/a 1                | Revisor/a 2                | Relator/a                   |
|-------|------------------------------|----------------------------|----------------------------|-----------------------------|
| HU-1  | Luis Sebastian Martinez Guerrero | Miguel Angel Veloza           | Yahir Camilo Forero Santos   | Luis Miguel Polo             |
| HU-2  | Miguel Angel Veloza             | Yahir Camilo Forero Santos     | Luis Miguel Polo              | Luis Sebastian Martinez Guerrero |
| HU-3  | Yahir Camilo Forero Santos      | Luis Miguel Polo                | Luis Sebastian Martinez Guerrero | Miguel Angel Veloza           |
| HU-4  | Luis Miguel Polo                 | Luis Sebastian Martinez Guerrero | Miguel Angel Veloza             | Yahir Camilo Forero Santos     |

---