# language: es
Característica: Establecer fecha límite y recordatorio
  Como usuario,
  Quiero poder asignar una fecha límite y un recordatorio a mis tareas
  Para organizar mejor mis tiempos y no olvidar tareas importantes

  Escenario: Asignar fecha límite a una tarea
    Dado que el usuario crea una tarea llamada "Entregar informe final"
    Cuando establece la fecha límite "2025-11-10"
    Entonces la tarea debe tener la fecha límite "2025-11-10" en la base de datos

  Escenario: Asignar recordatorio a una tarea
    Dado que existe una tarea llamada "Reunión con cliente"
    Cuando establece un recordatorio para "2025-11-10 14:30"
    Entonces la tarea debe tener el recordatorio "2025-11-10 14:30" en la base de datos

  Escenario: Validar que la fecha sea anterior al recordatorio
    Dado que existe una tarea llamada "Preparar presentación"
    Cuando se intenta establecer la fecha límite después de la fecha del recordatorio
    Entonces el sistema debe rechazar la solicitud
    Y debe devolver un error indicando que la fecha límite debe ser anterior o igual al recordatorio

  Escenario: Validar tareas con fecha límite vencida
    Dado que existe una tarea "Pagar servicios" con fecha límite "2025-01-01"
    Cuando se consulta el estado de las tareas
    Entonces el sistema debe identificar que la tarea está vencida
