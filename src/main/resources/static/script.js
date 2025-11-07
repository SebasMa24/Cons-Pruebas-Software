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
        document.getElementById('recordatorio').value = button.getAttribute('data-recordatorio');

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
            fechaVencimiento: datosFormulario.fechaVencimiento,
            recordatorio: datosFormulario.recordatorio
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
// script.js - Recordatorios con toast minimalista

// ----------------------------
// Utilidad: convertir fecha a Date
// ----------------------------
function parseFecha(fechaStr) {
  if (!fechaStr) return null;
  if (fechaStr instanceof Date) return fechaStr;

  const m = String(fechaStr).match(/^(\d{4})-(\d{2})-(\d{2})T(\d{2}):(\d{2})(?::(\d{2}))?/);
  if (m) {
    const [_, y, mo, d, h, mi, s] = m;
    return new Date(y, mo - 1, d, h, mi, s || 0);
  }

  const parsed = Date.parse(fechaStr);
  return isNaN(parsed) ? null : new Date(parsed);
}

// ----------------------------
// Toast visual simple
// ----------------------------
function mostrarToast(mensaje) {
  let toast = document.getElementById("recordatorio-toast");
  if (!toast) {
    toast = document.createElement("div");
    toast.id = "recordatorio-toast";
    toast.className = "fixed bottom-6 right-6 p-4 rounded shadow-lg z-50";
    toast.style.background = "#fff3cd";
    toast.style.color = "#856404";
    toast.style.border = "1px solid #ffeeba";
    toast.style.transition = "opacity 0.5s ease";
    toast.style.opacity = "0";
    document.body.appendChild(toast);
  }
  toast.textContent = mensaje;
  toast.style.opacity = "1";
  setTimeout(() => { toast.style.opacity = "0"; }, 7000);
}

// ----------------------------
// Lógica principal
// ----------------------------
function iniciarRecordatorios({ intervaloSeg = 30 } = {}) {
  const raw = window._TASKS_FROM_SERVER ?? window.recordatorios ?? [];
  if (!Array.isArray(raw) || raw.length === 0) return;

  const tareas = raw.map(t => ({
    ...t,
    _fechaObj: parseFecha(t.recordatorio),
    _notificado: false
  }));

  const revisar = () => {
    const ahora = new Date();
    tareas.forEach(t => {
      if (!t._fechaObj || t._notificado) return;

      const diffMs = t._fechaObj.getTime() - ahora.getTime();
      if (diffMs <= 0 && diffMs > -60000) { // dentro del último minuto
        mostrarToast(`⏰ Recordatorio: ${t.titulo}${t.descripcion ? " — " + t.descripcion : ""}`);
        t._notificado = true;
      }
    });
  };

  revisar();
  setInterval(revisar, intervaloSeg * 1000);
}

// ----------------------------
// Inicialización
// ----------------------------
document.addEventListener("DOMContentLoaded", () => {
  try {
    iniciarRecordatorios({ intervaloSeg: 10 });
  } catch (err) {
    console.error("Error en recordatorios:", err);
  }
});
