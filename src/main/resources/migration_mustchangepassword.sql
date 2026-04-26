-- Migración: agregar dni y mustChangePassword a la tabla Usuario
ALTER TABLE Usuario
    ADD COLUMN dni VARCHAR(20) NOT NULL DEFAULT '' AFTER passwordHash,
    ADD COLUMN mustChangePassword BOOLEAN NOT NULL DEFAULT TRUE AFTER dni;

-- Los usuarios existentes (admin/vendedor) no deben ser forzados a cambiar contraseña
UPDATE Usuario SET mustChangePassword = FALSE WHERE email IN ('admin@pharmman.com', 'vendedor@pharmman.com');
