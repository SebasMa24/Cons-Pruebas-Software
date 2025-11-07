# language: es
Característica: Crear una nueva tarea
  Como usuario, quiero poder agregar nuevas tareas para que pueda gestionar lo que debo hacer.

  Escenario: Crear tarea correctamente
    Dado que el usuario está en la pantalla principal
    Cuando el usuario ingresa el título "Comprar comida" y hace clic en "Agregar tarea"
    Entonces la tarea se agrega a la lista de tareas pendientes
    Y la tarea visible tiene el título "Comprar comida"

  Escenario: Intentar crear tarea sin título
    Dado que el usuario está en la pantalla principal
    Cuando el usuario intenta agregar una tarea sin ingresar título
    Entonces el sistema muestra un mensaje de error "El título de la tarea no puede estar vacío"

  Escenario: Tarea persiste después de recargar la página
    Dado que el usuario ha agregado una tarea con título "Estudiar"
    Cuando el usuario recarga la página
    Entonces la tarea "Estudiar" aparece en la lista de tareas pendientes

  Escenario: Título de tarea excede los 100 caracteres
    Dado que el usuario está creando una nueva tarea
    Cuando el usuario ingresa un título de más de 100 caracteres
    Entonces el sistema muestra un mensaje de advertencia "El título de la tarea no puede exceder 100 caracteres"
