# language: es
Característica: Crear una nueva tarea
  Como usuario, quiero poder agregar nuevas tareas para gestionar mis pendientes.

  Escenario: Crear tarea exitosamente
    Dado que el usuario intenta crear una nueva tarea
    Cuando el usuario envía una solicitud con el título "Comprar comida"
    Entonces el sistema debe crear la tarea y devolver una respuesta con el identificador y el título "Comprar comida"

  Escenario: Validación de tarea sin título
    Dado que el usuario intenta crear una nueva tarea
    Cuando el usuario envía una solicitud sin título
    Entonces el sistema debe devolver un error indicando "El título de la tarea no puede estar vacío"

  Escenario: Persistencia de la tarea
    Dado que el usuario ha creado una tarea previamente con el título "Estudiar"
    Cuando el usuario consulta la lista de tareas mediante el endpoint correspondiente
    Entonces el sistema debe retornar la tarea con el título "Estudiar", garantizando su persistencia

  Escenario: Validación de longitud del título
    Dado que el usuario intenta crear una nueva tarea
    Cuando el usuario envía un título que excede los 100 caracteres
    Entonces el sistema debe devolver un mensaje de advertencia indicando que el título es demasiado largo