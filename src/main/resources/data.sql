-- ===============================
-- Datos de prueba para la tabla Tarea
-- ===============================

INSERT INTO tarea (titulo, descripcion, fechavencimiento, estado)
VALUES 
('Reunión con el equipo', 'Reunión semanal para seguimiento de avances', '2025-09-20', 'PENDIENTE'),
('Desarrollar módulo de autenticación', 'Implementar login y registro con Spring Security', '2025-09-22', 'EN_PROGRESO'),
('Actualizar documentación', 'Actualizar el README y diagramas de arquitectura', '2025-09-25', 'PENDIENTE'),
('Revisar pull requests', 'Revisar cambios pendientes en GitHub antes de hacer merge', '2025-09-21', 'COMPLETADA'),
('Preparar presentación', 'Crear diapositivas para la demo con el cliente', '2025-09-28', 'EN_PROGRESO'),
('Backup de la base de datos', 'Realizar copia de seguridad completa de la BD', '2025-09-30', 'PENDIENTE');
