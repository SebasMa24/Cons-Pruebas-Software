# language: es
Característica: HU-5 Filtrar tareas por estado
  Como usuario,
  Quiero poder filtrar las tareas por estado (pendientes, en progreso o completadas)
  Para visualizar solo las que me interesen en el momento

  Antecedentes:
    Dado que el usuario tiene una lista de tareas con diferentes estados:
      | título               | estado        |
      | Comprar pan          | PENDIENTE     |
      | Estudiar para examen | EN_PROGRESO   |
      | Lavar la ropa        | COMPLETADA    |

  Escenario: Filtrar tareas pendientes
    Cuando el usuario selecciona el filtro "Pendientes"
    Entonces el sistema debe mostrar solo las tareas con estado "PENDIENTE"
    Y los filtros "Completadas" y "En progreso" deben desactivarse
    Y la lista visual debe actualizarse según el filtro seleccionado

  Escenario: Filtrar tareas completadas
    Cuando el usuario selecciona el filtro "Completadas"
    Entonces el sistema debe mostrar únicamente las tareas finalizadas
    Y los filtros "Pendientes" y "En progreso" deben desactivarse
    Y el filtro seleccionado debe persistir después de recargar la página

  Escenario: Filtrar tareas en progreso
    Cuando el usuario selecciona el filtro "En progreso"
    Entonces el sistema debe mostrar solo las tareas con estado "EN_PROGRESO"
    Y los filtros "Pendientes" y "Completadas" deben desactivarse

  Escenario: Mostrar todas las tareas
    Dado que el usuario tiene un filtro activo
    Cuando el usuario selecciona el filtro "Todos"
    Entonces el sistema debe mostrar todas las tareas sin filtrar
    Y se deben eliminar todos los filtros activos

  Escenario: Persistencia del filtro tras recargar la página
    Dado que el usuario seleccionó el filtro "Completadas"
    Cuando el usuario recarga la página
    Entonces el sistema debe seguir mostrando únicamente las tareas completadas

  Escenario: Mostrar etiqueta de filtro activo
    Cuando el usuario selecciona un filtro
    Entonces el sistema debe mostrar una etiqueta indicando el filtro activado