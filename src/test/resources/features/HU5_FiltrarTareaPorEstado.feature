# language: es
Característica: HU-5 Filtrar tareas por estado
  Como usuario,
  Quiero filtrar mis tareas por estado
  Para obtener una vista organizada

  Antecedentes:
    Dado que el usuario tiene una lista de tareas con diferentes estados:
      | título               | estado        |
      | Comprar pan          | PENDIENTE     |
      | Estudiar para examen | EN_PROGRESO   |
      | Lavar la ropa        | COMPLETADA    |

  Escenario: Filtrar tareas pendientes
    Cuando el sistema recibe una solicitud para filtrar por estado "PENDIENTES"
    Entonces el sistema debe devolver solo las tareas con estado "PENDIENTE"

  Escenario: Filtrar tareas completadas
    Cuando el sistema recibe una solicitud para filtrar por estado "COMPLETADAS"
    Entonces el sistema debe devolver solo las tareas con estado "COMPLETADA"

  Escenario: Filtrar tareas en progreso
    Cuando el sistema recibe una solicitud para filtrar por estado "EN PROGRESO"
    Entonces el sistema debe devolver solo las tareas con estado "EN_PROGRESO"

  Escenario: Restablecer filtro para obtener todas las tareas
    Dado que existe un filtro activo
    Cuando el sistema recibe una solicitud para filtrar por estado "TODOS"
    Entonces el sistema debe devolver la lista completa de tareas

  Escenario: Persistencia del filtro aplicado
    Dado que existe un filtro activo
    Cuando se consulta nuevamente la lista de tareas con el filtro activo
    Entonces el filtro debe persistir en la siguiente solicitud
    Y el sistema debe seguir devolviendo únicamente las tareas completadas
