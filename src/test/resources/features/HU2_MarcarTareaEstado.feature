# language: es
Característica: Marcar tarea como completada o en progreso
  Como usuario,
  Quiero poder marcar las tareas como completadas o en progreso
  Para saber qué tareas ya he terminado y cuáles estoy realizando

  Antecedentes:
    Dado que existe una tarea en la lista con estado "PENDIENTE"

  Escenario: Cambiar el estado de una tarea al marcar la casilla
    Cuando el usuario haga clic en la casilla de verificación junto a la tarea
    Entonces la tarea debe cambiar su estado a "COMPLETADA" o "EN_PROGRESO"
    Y la tarea debe aparecer visualmente tachada en la lista de tareas pendientes

  Escenario: Diferenciar visualmente las tareas completadas
    Dado que la tarea ha sido marcada como "COMPLETADA"
    Cuando la tarea se muestre en la sección de tareas completadas
    Entonces la tarea debe estar visualmente diferenciada (por ejemplo, gris o tachada)

  Escenario: Registrar fecha de finalización
    Dado que la tarea está en estado "PENDIENTE"
    Cuando el usuario la marca como "COMPLETADA"
    Entonces el sistema debe guardar la fecha de finalización de la tarea

  Escenario: Desmarcar tarea completada y devolverla a pendientes
    Dado que la tarea está marcada como "COMPLETADA"
    Cuando el usuario desmarca la casilla de la tarea
    Entonces la tarea debe regresar a la sección de tareas pendientes
    Y la tarea debe mostrarse sin tachar ni diferenciación visual
    Y la fecha de finalización debe eliminarse