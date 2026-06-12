-- ============================================================
-- LIMPIEZA DE USUARIOS DE PRUEBA
-- Conserva solo los usuarios reales: IDs 1, 4, 5, 6, 9
-- ============================================================

SET FOREIGN_KEY_CHECKS = 0;

-- 1. Borrar detalles de ventas de usuarios de prueba
DELETE FROM DetalleVenta
WHERE ventaId IN (
    SELECT id FROM Venta WHERE usuarioId NOT IN (1, 4, 5, 6, 9)
);

-- 2. Borrar ventas de usuarios de prueba
DELETE FROM Venta WHERE usuarioId NOT IN (1, 4, 5, 6, 9);

-- 3. Borrar detalles de ingresos de usuarios de prueba
DELETE FROM DetalleIngreso
WHERE ingresoId IN (
    SELECT id FROM Ingreso WHERE usuarioId NOT IN (1, 4, 5, 6, 9)
);

-- 4. Borrar ingresos de usuarios de prueba
DELETE FROM Ingreso WHERE usuarioId NOT IN (1, 4, 5, 6, 9);

-- 5. Borrar sesiones de usuarios de prueba
DELETE FROM Sesion WHERE usuarioId NOT IN (1, 4, 5, 6, 9);

-- 6. Borrar los usuarios de prueba
DELETE FROM Usuario WHERE id NOT IN (1, 4, 5, 6, 9);

SET FOREIGN_KEY_CHECKS = 1;

-- Verificar resultado
SELECT id, nombre, apellidoPaterno, email FROM Usuario ORDER BY id;
