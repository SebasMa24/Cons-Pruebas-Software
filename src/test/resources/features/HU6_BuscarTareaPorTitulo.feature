# language: es
Característica: Búsqueda de tareas por título
  Como usuario
  Quiero poder buscar una tarea por su título
  Para encontrar rápidamente una tarea específica entre muchas

  Antecedentes:
    Dado que existen las siguientes tareas en el sistema:
      | titulo                    | descripcion      | estado     |
      | Preparar informe mensual  | Informe Q4       | PENDIENTE  |
      | Enviar correo a cliente   | Follow up        | COMPLETADA |
      | preparar PRESENTACIÓN     | Slides Q4        | PENDIENTE  |
      | Revisar código backend    | Code review PR23 | PENDIENTE  |

  Escenario: Buscar tareas con coincidencias ignorando mayúsculas y minúsculas
    Cuando el usuario busca tareas con el texto "preparar"
    Entonces el sistema debe mostrar 2 tarea(s)
    Y todas las tareas mostradas deben contener "preparar" en el título

  Escenario: Buscar tareas sin coincidencias
    Cuando el usuario busca tareas con el texto "compras"
    Entonces el sistema debe mostrar 0 tarea(s)
    Y se debe mostrar el mensaje "No se encontraron tareas con ese título"

  Escenario: Limpiar búsqueda muestra todas las tareas disponibles
    Dado que el usuario ha buscado tareas con el texto "correo"
    Y el resultado de la búsqueda muestra 1 tarea(s)
    Cuando el usuario limpia el campo de búsqueda
    Entonces el sistema debe mostrar 4 tarea(s)