# language: es
Característica: Marcar tarea como completada o en progreso
  Como usuario,
  Quiero poder marcar las tareas como completadas o en progreso
  Para saber qué tareas ya he terminado y cuáles estoy realizando

  Antecedentes:
    Dado que existe una tarea con estado "PENDIENTE"

  Escenario: Cambiar estado a COMPLETADA
    Cuando el sistema recibe una solicitud para cambiar el estado de la tarea a "COMPLETADA"
    Entonces la tarea debe tener estado "COMPLETADA" en la base de datos
    Y la fecha de finalización debe estar registrada

  Escenario: Cambiar estado a EN_PROGRESO
    Cuando el sistema recibe una solicitud para cambiar el estado de la tarea a "EN_PROGRESO"
    Entonces la tarea debe tener estado "EN_PROGRESO" en la base de datos
    Y la fecha de finalización debe ser nula

  Escenario: Desmarcar COMPLETADA y volver a PENDIENTE
    Dado que la tarea está en estado "COMPLETADA"
    Cuando el sistema recibe una solicitud para cambiar el estado de la tarea a "PENDIENTE"
    Entonces la tarea debe tener estado "PENDIENTE" en la base de datos
    Y la fecha de finalización debe eliminarse