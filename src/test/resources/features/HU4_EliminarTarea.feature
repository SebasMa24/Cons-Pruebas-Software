# language: es
Característica: Eliminar tarea
  Como usuario,
  Quiero poder eliminar tareas
  Para no ver las tareas que ya no son relevantes o necesarias

  Escenario: Mostrar ventana de confirmación al eliminar
    Dado que el usuario desea eliminar la tarea "Comprar pan"
    Cuando el usuario hace clic en el botón de "eliminar" junto a la tarea
    Entonces el sistema debe mostrar una ventana emergente con el mensaje "¿Estás seguro de que deseas eliminar esta tarea?"

  Escenario: Confirmar eliminación de la tarea
    Dado que existe una tarea llamada "Comprar pan" en la lista de tareas
    Y el usuario desea eliminar la tarea "Comprar pan"
    Cuando el usuario confirma la acción de eliminar
    Entonces la tarea debe desaparecer de la lista de tareas pendientes
    Y la tarea debe ser eliminada permanentemente del sistema

  Escenario: Cancelar eliminación de la tarea
    Dado que existe una tarea llamada "Comprar pan" en la lista de tareas
    Y el usuario desea eliminar la tarea "Comprar pan"
    Cuando el usuario hace clic en "Cancelar" en la ventana emergente
    Entonces la tarea debe permanecer en la lista sin cambios

  Escenario: Persistencia de eliminación tras recargar la página
    Dado que el usuario ha eliminado la tarea "Comprar pan"
    Cuando el usuario recarga la página visible
    Entonces la tarea eliminada no debe aparecer en ninguna de las listas de tareas