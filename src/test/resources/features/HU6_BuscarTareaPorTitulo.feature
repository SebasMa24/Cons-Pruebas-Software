# language: es
Característica: HU-6 Buscar tarea por título
  Como usuario
  Quiero buscar tareas por su título
  Para encontrarlas rápidamente

  Antecedentes:
    Dado que existen múltiples tareas registradas en el sistema:
      | titulo                    | descripcion      | estado     |
      | Preparar informe mensual  | Informe Q4       | PENDIENTE  |
      | Enviar correo a cliente   | Follow up        | COMPLETADA |
      | preparar PRESENTACIÓN     | Slides Q4        | PENDIENTE  |
      | Revisar código backend    | Code review PR23 | PENDIENTE  |

  Escenario: Búsqueda por coincidencia
    Cuando el sistema recibe una solicitud de búsqueda con el texto "preparar"
    Entonces debe devolver 2 tarea(s)
    Y todas las tareas devueltas deben contener "preparar" en el título sin distinguir mayúsculas/minúsculas

  Escenario: Sin coincidencias
    Cuando el sistema recibe una solicitud de búsqueda con el texto "compras"
    Entonces debe devolver 0 tarea(s)
    Y el sistema debe informar que no se encontraron tareas

  Escenario: Restablecimiento de resultados
    Dado que se realizó una búsqueda previamente con el texto "correo"
    Y la búsqueda devolvió 1 tarea(s)
    Cuando el sistema recibe una solicitud de búsqueda con texto vacío
    Entonces debe devolver todas las tareas disponibles
    Y el sistema debe devolver 4 tarea(s)
    