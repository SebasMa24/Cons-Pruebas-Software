async function fetchWithErrorHandling(url, method = 'GET', data = null, headers = {}) {
    const config = {
        method: method,
        headers: { 'Content-Type': 'application/json', ...headers },
        body: data ? JSON.stringify(data) : undefined
    };

    const response = await fetch(url, config);
    const text = await response.text();
    
    if (!response.ok) {
        throw new Error(extractErrorMessage(text, response.status));
    }

    return text ? JSON.parse(text) : null;
}

function extractErrorMessage(responseText, statusCode) {
    if (!responseText) return getDefaultErrorMessage(statusCode);

    try {
        const errorJson = JSON.parse(responseText);
        return errorJson.trace?.match(/\"([^\"]+)\"/)?.[1] || 
               errorJson.message || 
               errorJson.error || 
               getDefaultErrorMessage(statusCode);
    } catch {
        return responseText.length > 200 ? 
               getDefaultErrorMessage(statusCode) : 
               responseText;
    }
}

function getDefaultErrorMessage(statusCode) {
    const messages = {
        400: 'Solicitud incorrecta', 401: 'No autorizado', 403: 'Prohibido',
        404: 'Recurso no encontrado', 500: 'Error del servidor'
    };
    return messages[statusCode] || 'Error desconocido';
}

// UI Functions
function mostrarMensaje(mensaje, tipo = 'error', tiempo = tipo === 'error' ? 5000 : 3000) {
    const popup = document.getElementById("error-popup");
    if (!popup) return;

    popup.textContent = mensaje;
    popup.className = `popup ${tipo} hidden fixed top-4 right-4 bg-red-500 text-black px-4 py-2 rounded shadow-lg z-40`;
    popup.style.backgroundColor = tipo === 'error' ? '#f8d7da' : '#d4edda';
    popup.classList.remove("hidden");

    setTimeout(() => popup.classList.add("hidden"), tiempo);
}

function obtenerDatosFormulario(formElement) {
    return Object.fromEntries(new FormData(formElement));
}


// ===============================
// Control de Modal para Editar Tarea
// ===============================
const modal = document.getElementById('modal-editar');
const formEditar = document.getElementById('form-editar-tarea');
const cancelarBtn = document.getElementById('cancelar-editar');

// Cuando se hace clic en el botón "Editar" de cada tarea
document.querySelectorAll('.edit-btn').forEach(button => {
    button.addEventListener('click', () => {
        // Cargar datos en el formulario
        document.getElementById('id').value = button.getAttribute('data-id');
        document.getElementById('titulo').value = button.getAttribute('data-titulo');
        document.getElementById('descripcion').value = button.getAttribute('data-descripcion');
        document.getElementById('fechaVencimiento').value = button.getAttribute('data-fecha');

        // Mostrar modal
        modal.classList.remove('hidden');
    });
});



// Cerrar modal sin guardar
cancelarBtn.addEventListener('click', () => {
    modal.classList.add('hidden');
    formEditar.reset();
});


document.getElementById("form-editar-tarea").addEventListener("submit", async function (e) {
    e.preventDefault();
    
    try {
        const datosFormulario = obtenerDatosFormulario(e.target);
        const idTarea = datosFormulario.id;

        if (!idTarea) {
            mostrarMensaje("No se encontró el ID de la tarea", "error");
            return;
        }

        const data = {
            titulo: datosFormulario.titulo,
            descripcion: datosFormulario.descripcion,
            fechaVencimiento: datosFormulario.fechaVencimiento
        };

        await fetchWithErrorHandling(`/tarea/${idTarea}`, "PUT", data);
        modal.classList.add('hidden');
        formEditar.reset();
        mostrarMensaje("Tarea editada exitosamente!", "success");

    } catch (error) {
        mostrarMensaje(error.message);
    }
});


document.getElementById("form-tarea").addEventListener("submit", async function (e) {
    e.preventDefault();

    try {
        const datosFormulario = obtenerDatosFormulario(e.target);
        await fetchWithErrorHandling("/tarea", "POST", datosFormulario);
        e.target.reset();
        mostrarMensaje("Tarea creada exitosamente!", "success");
    } catch (error) {
        mostrarMensaje(error.message);
    }
});

// ===============================
// HU6: Búsqueda de tareas
// ===============================
const inputBusqueda = document.getElementById('busqueda-input');
const btnLimpiar = document.getElementById('limpiar-busqueda');
const mensajeBusqueda = document.getElementById('mensaje-busqueda');

// Buscar mientras el usuario escribe (debounce)
let timeoutBusqueda;
inputBusqueda.addEventListener('input', function() {
    clearTimeout(timeoutBusqueda);
    
    timeoutBusqueda = setTimeout(async () => {
        const texto = this.value.trim();
        
        if (texto === '') {
            // Si está vacío, recargar página para mostrar todas
            window.location.reload();
            return;
        }
        
        await buscarTareas(texto);
    }, 300); // Espera 300ms después de que el usuario deja de escribir
});

// Botón limpiar búsqueda
btnLimpiar.addEventListener('click', function() {
    inputBusqueda.value = '';
    mensajeBusqueda.classList.add('hidden');
    window.location.reload();
});

// Función para buscar tareas
async function buscarTareas(texto) {
    try {
        const tareas = await fetchWithErrorHandling(`/tareas/buscar?texto=${encodeURIComponent(texto)}`);
        
        if (tareas.length === 0) {
            mensajeBusqueda.textContent = 'No se encontraron tareas con ese título';
            mensajeBusqueda.classList.remove('hidden');
            mostrarTareasEnListado([]);
        } else {
            mensajeBusqueda.classList.add('hidden');
            mostrarTareasEnListado(tareas);
        }
    } catch (error) {
        mostrarMensaje(error.message);
    }
}

// Función para renderizar tareas dinámicamente
function mostrarTareasEnListado(tareas) {
    // Buscar el contenedor correcto - el div con clase space-y-4 dentro de la sección de listado
    const seccionListado = document.querySelector('section.bg-white.rounded-lg.shadow-md.p-6:last-of-type');
    let contenedor = seccionListado.querySelector('.space-y-4');
    
    // Si no existe el contenedor, crearlo
    if (!contenedor) {
        const mensajeVacio = seccionListado.querySelector('.text-center.text-gray-500.italic');
        if (mensajeVacio) {
            mensajeVacio.remove();
        }
        contenedor = document.createElement('div');
        contenedor.className = 'space-y-4';
        seccionListado.appendChild(contenedor);
    }
    
    if (tareas.length === 0) {
        contenedor.innerHTML = '<p class="text-center text-gray-500 italic">No hay tareas que coincidan con tu búsqueda.</p>';
        return;
    }
    
    contenedor.innerHTML = tareas.map(tarea => `
        <div class="bg-white shadow-md rounded-lg p-4 border border-gray-200 ${tarea.estado === 'COMPLETADA' ? 'opacity-60 line-through' : ''}">
            <!-- Título -->
            <h2 class="text-lg font-semibold text-gray-800 mb-2">${escapeHtml(tarea.titulo)}</h2>

            <!-- Descripción -->
            <p class="text-gray-600 text-sm mb-3">${escapeHtml(tarea.descripcion || '')}</p>

            <!-- Form para cambiar estado -->
            <form action="/tareas/${tarea.id}/estado" method="post" class="mb-3">
                <select name="estado" onchange="this.form.submit()" class="px-2 py-1 border rounded text-sm ${
                    tarea.estado === 'COMPLETADA' ? 'bg-green-100 text-green-800' :
                    tarea.estado === 'PENDIENTE' ? 'bg-red-100 text-red-800' : 'bg-yellow-100 text-yellow-800'
                }">
                    <option value="PENDIENTE" ${tarea.estado === 'PENDIENTE' ? 'selected' : ''}>Pendiente</option>
                    <option value="EN_PROGRESO" ${tarea.estado === 'EN_PROGRESO' ? 'selected' : ''}>En Progreso</option>
                    <option value="COMPLETADA" ${tarea.estado === 'COMPLETADA' ? 'selected' : ''}>Completada</option>
                </select>
            </form>
            
            <!-- Información inferior -->
            <div class="flex justify-between items-center">
                <div class="flex items-center space-x-3">
                    <!-- Fecha -->
                    <span class="text-xs text-gray-500">${formatearFecha(tarea.fechaVencimiento)}</span>
                </div>
                <div class="flex gap-2">
                    <!-- Botón editar -->
                    <button 
                        type="button" 
                        class="edit-btn bg-orange-500 hover:bg-orange-600 text-white text-xs px-3 py-1 rounded shadow"
                        data-id="${tarea.id}"
                        data-titulo="${escapeHtml(tarea.titulo)}"
                        data-descripcion="${escapeHtml(tarea.descripcion || '')}"
                        data-fecha="${tarea.fechaVencimiento}">
                        Editar
                    </button>
                    <!-- Botón eliminar -->
                    <form action="/eliminar" method="post" class="inline" onsubmit="return confirm('¿Estás seguro de que deseas eliminar esta tarea?')">
                        <input type="hidden" name="id" value="${tarea.id}" />
                        <button type="submit" class="bg-red-500 hover:bg-red-600 text-white text-xs px-3 py-1 rounded shadow">
                            Eliminar
                        </button>
                    </form>
                </div>
            </div>
        </div>
    `).join('');
    
    // Re-adjuntar event listeners a los nuevos botones de editar
    //adjuntarListenersEditar();
}

// Funciones auxiliares
function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

function formatearFecha(fechaStr) {
    const fecha = new Date(fechaStr + 'T00:00:00');
    return fecha.toLocaleDateString('es-ES', { day: '2-digit', month: '2-digit', year: 'numeric' });
}