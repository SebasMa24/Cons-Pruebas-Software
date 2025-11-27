# language: es
Característica: Eliminar tarea
  Como usuario,
  Quiero eliminar tareas que ya no necesito
  Para mantener mi lista de tareas actualizada

  Escenario: Solicitud de eliminación requiere confirmación
    Dado que existe una tarea registrada con el título "Comprar pan"
    Cuando el sistema recibe una solicitud para eliminar la tarea con identificador válido sin confirmación
    Entonces el sistema debe requerir confirmación antes de eliminar la tarea

  Escenario: Eliminación confirmada
    Dado que existe una tarea registrada con el título "Comprar pan"
    Cuando el sistema recibe una solicitud para eliminar la tarea con confirmacion verdadera
    Entonces el sistema debe eliminar la tarea permanentemente

  Escenario: Cancelación de la eliminación
    Dado que existe una tarea registrada con el título "Comprar pan"
    Cuando el sistema recibe una solicitud para eliminar la tarea sin el parámetro de confirmación
    Entonces la tarea no debe ser eliminada y el sistema debe informar que la acción fue cancelada

  Escenario: Persistencia tras eliminación
    Dado que una tarea previamente eliminada con id válido ya no existe en el sistema
    Cuando se consulta la lista de tareas después de la eliminación
    Entonces la tarea eliminada no debe aparecer en la lista de tareas