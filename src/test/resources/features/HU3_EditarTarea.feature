# language: es
Característica: HU-3 Editar tarea existente
  Como usuario
  Quiero poder editar tareas existentes
  Para cambiar los detalles o la fecha límite si es necesario

  Escenario: Mostrar formulario de edición
    Dado que existe una tarea llamada "Comprar pan" en la lista
    Cuando el usuario hace clic en el icono de editar junto a la tarea
    Entonces debe mostrarse un formulario con los datos actuales de la tarea

  Escenario: Actualizar el título de la tarea
    Dado que el usuario está editando la tarea "Comprar pan"
    Cuando el usuario cambia el título por "Comprar pan integral" y guarda los cambios
    Entonces la lista de tareas debe mostrar la tarea con el título "Comprar pan integral"

  Escenario: Actualizar la fecha de vencimiento
    Dado que el usuario está editando la tarea "Entregar informe" con fecha de vencimiento "2025-11-10"
    Cuando el usuario cambia la fecha de vencimiento a "2025-11-15" y guarda los cambios
    Entonces la lista de tareas debe mostrar la nueva fecha de vencimiento "2025-11-15"

  Escenario: Persistencia de los cambios
    Dado que la tarea "Estudiar Examen" fue editada y guardada
    Cuando el usuario recarga la página actual
    Entonces la tarea debe reflejar los datos editados correctamente
