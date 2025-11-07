# language: es
Característica: Establecer fecha límite y recordatorio
  Como usuario,
  Quiero poder asignar una fecha límite y un recordatorio a mis tareas
  Para organizar mejor mis tiempos y no olvidar tareas importantes

  Escenario: Mostrar fecha junto al título de la tarea
    Dado que el usuario crea una tarea llamada "Entregar informe final"
    Cuando establece la fecha límite "2025-11-10"
    Entonces el sistema debe mostrar la fecha "10/11/2025" junto al título de la tarea

  Escenario: Resaltar tareas próximas al vencimiento
    Dado que existe una tarea llamada "Preparar presentación" con fecha límite en menos de 24 horas
    Cuando el usuario ingresa nuevamente a la aplicación
    Entonces la tarea debe mostrarse resaltada con un color o ícono de advertencia

  Escenario: Generar alerta al llegar la hora del recordatorio
    Dado que el usuario establece un recordatorio para la tarea "Reunión con cliente" a la hora actual
    Cuando llega la hora configurada del recordatorio
    Entonces el sistema debe generar una alerta o notificación visible al usuario

  Escenario: Mostrar tareas vencidas
    Dado que existe una tarea llamada "Pagar servicios" cuya fecha límite ya ha pasado
    Cuando el usuario visualiza la lista de tareas
    Entonces la tarea debe mostrarse con un estilo visual distintivo (por ejemplo, en rojo o con la etiqueta "Vencida")
