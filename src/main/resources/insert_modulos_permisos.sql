-- ============================================
-- SCRIPT: Insertar módulos y permisos necesarios
-- Ejecutar este script en la base de datos sistema_gestion
-- ============================================

-- 1. Insertar módulos si no existen
INSERT IGNORE INTO Modulo (id, nombre, descripcion, estado) VALUES
(1, 'Usuarios', 'Gestión de usuarios del sistema', true),
(2, 'Roles', 'Gestión de roles y permisos', true),
(3, 'Productos', 'Gestión de productos y categorías', true),
(4, 'Ventas', 'Registro y consulta de ventas', true),
(5, 'Ingresos', 'Registro y consulta de ingresos de productos', true),
(6, 'Sesiones', 'Consulta de sesiones de usuario', true);

-- 2. Insertar permisos para el rol ADMIN (asumiendo que rolId=1 es ADMIN)
-- Primero eliminar permisos existentes para evitar duplicados
DELETE FROM RolPermiso WHERE rolId = 1;

-- Insertar permisos completos para ADMIN
INSERT INTO RolPermiso (rolId, moduloId, lectura, escritura) VALUES
(1, 1, true, true),  -- Usuarios
(1, 2, true, true),  -- Roles
(1, 3, true, true),  -- Productos
(1, 4, true, true),  -- Ventas
(1, 5, true, true),  -- Ingresos
(1, 6, true, true);  -- Sesiones

-- 3. Insertar permisos para el rol VENDEDOR (asumiendo que rolId=2 es VENDEDOR)
DELETE FROM RolPermiso WHERE rolId = 2;

-- Insertar permisos limitados para VENDEDOR
INSERT INTO RolPermiso (rolId, moduloId, lectura, escritura) VALUES
(2, 3, true, true),   -- Productos (puede ver y registrar)
(2, 4, true, true);   -- Ventas (puede ver y registrar)

-- 4. Verificar los datos insertados
SELECT m.nombre as Modulo, rp.lectura, rp.escritura, r.nombre as Rol
FROM RolPermiso rp
JOIN Modulo m ON rp.moduloId = m.id
JOIN Rol r ON rp.rolId = r.id
ORDER BY r.nombre, m.nombre;
