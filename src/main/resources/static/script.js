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