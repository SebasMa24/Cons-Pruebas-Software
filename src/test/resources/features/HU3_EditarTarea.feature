# language: es
Característica: HU-3 Editar tarea existente
  Como usuario
  Quiero modificar los datos de una tarea cuando sea necesario
  Para mantener la información actualizada

  Escenario: Actualización del contenido
    Dado que existe una tarea registrada con título "Comprar pan"
    Cuando el sistema recibe una solicitud de edición con título "Comprar pan integral"
    Entonces debe actualizar la tarea y confirmar que los cambios fueron aplicados

  Escenario: Título inválido
    Dado que existe una tarea registrada con título "Comprar leche"
    Cuando se intenta editar la tarea con un título vacío
    Entonces el sistema debe devolver un error indicando que el título no puede estar vacío

  Escenario: Título nulo
    Dado que existe una tarea registrada con título "Hacer ejercicio"
    Cuando se intenta editar la tarea con un título nulo
    Entonces el sistema debe devolver un error indicando que el título no puede estar vacío

  Escenario: Actualización de fecha límite
    Dado que existe una tarea registrada con título "Entregar informe" y fecha de vencimiento "2025-11-10"
    Cuando se recibe una solicitud de edición con fecha de vencimiento "2025-11-15"
    Entonces el sistema debe almacenar esa fecha correctamente

  Escenario: Persistencia de cambios
    Dado que una tarea con título "Estudiar Examen" fue editada a "Estudiar Examen Final"
    Cuando se consulta nuevamente la lista de tareas
    Entonces la tarea debe reflejar los datos actualizados