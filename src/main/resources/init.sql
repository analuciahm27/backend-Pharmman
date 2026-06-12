-- ============================================================
-- SCHEMA COMPLETO PharmMan — sistema_gestion
-- Se ejecuta automáticamente al iniciar el contenedor MySQL
-- ============================================================

CREATE DATABASE IF NOT EXISTS sistema_gestion
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE sistema_gestion;

-- ------------------------------------------------------------
-- 1. Tablas sin dependencias
-- ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS Modulo (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    estado      BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS Rol (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    estado      BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS Categoria (
    id      INT AUTO_INCREMENT PRIMARY KEY,
    nombre  VARCHAR(100) NOT NULL,
    prefijo VARCHAR(5)
);

-- ------------------------------------------------------------
-- 2. Tablas con dependencias
-- ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS RolPermiso (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    rolId     INT NOT NULL,
    moduloId  INT NOT NULL,
    lectura   BOOLEAN NOT NULL DEFAULT FALSE,
    escritura BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_rolpermiso_rol    FOREIGN KEY (rolId)    REFERENCES Rol(id),
    CONSTRAINT fk_rolpermiso_modulo FOREIGN KEY (moduloId) REFERENCES Modulo(id)
);

CREATE TABLE IF NOT EXISTS Usuario (
    id                 INT AUTO_INCREMENT PRIMARY KEY,
    nombre             VARCHAR(100) NOT NULL,
    apellidoPaterno    VARCHAR(100) NOT NULL,
    apellidoMaterno    VARCHAR(100),
    email              VARCHAR(150) NOT NULL UNIQUE,
    passwordHash       VARCHAR(255) NOT NULL,
    dni                VARCHAR(20)  NOT NULL DEFAULT '',
    mustChangePassword BOOLEAN NOT NULL DEFAULT TRUE,
    estado             BOOLEAN NOT NULL DEFAULT TRUE,
    createdAt          DATETIME DEFAULT CURRENT_TIMESTAMP,
    rolId              INT,
    CONSTRAINT fk_usuario_rol FOREIGN KEY (rolId) REFERENCES Rol(id)
);

CREATE TABLE IF NOT EXISTS Sesion (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    entrada   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    salida    DATETIME,
    usuarioId INT,
    CONSTRAINT fk_sesion_usuario FOREIGN KEY (usuarioId) REFERENCES Usuario(id)
);

CREATE TABLE IF NOT EXISTS Producto (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    codigo      VARCHAR(50)  NOT NULL UNIQUE,
    nombre      VARCHAR(150) NOT NULL,
    descripcion TEXT,
    precio      DOUBLE NOT NULL,
    stock       INT    NOT NULL DEFAULT 0,
    activo      BOOLEAN NOT NULL DEFAULT TRUE,
    categoriaId INT,
    CONSTRAINT fk_producto_categoria FOREIGN KEY (categoriaId) REFERENCES Categoria(id)
);

CREATE TABLE IF NOT EXISTS Venta (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    metodoPago  VARCHAR(50) NOT NULL,
    total       DOUBLE NOT NULL,
    fecha       DATETIME DEFAULT CURRENT_TIMESTAMP,
    usuarioId   INT,
    CONSTRAINT fk_venta_usuario FOREIGN KEY (usuarioId) REFERENCES Usuario(id)
);

CREATE TABLE IF NOT EXISTS DetalleVenta (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    cantidad       INT    NOT NULL,
    precioUnitario DOUBLE NOT NULL,
    subtotal       DOUBLE NOT NULL,
    ventaId        INT,
    productoId     INT,
    CONSTRAINT fk_detalleventa_venta    FOREIGN KEY (ventaId)    REFERENCES Venta(id),
    CONSTRAINT fk_detalleventa_producto FOREIGN KEY (productoId) REFERENCES Producto(id)
);

CREATE TABLE IF NOT EXISTS Ingreso (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    fecha     DATETIME DEFAULT CURRENT_TIMESTAMP,
    usuarioId INT,
    CONSTRAINT fk_ingreso_usuario FOREIGN KEY (usuarioId) REFERENCES Usuario(id)
);

CREATE TABLE IF NOT EXISTS DetalleIngreso (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    cantidad   INT NOT NULL,
    ingresoId  INT,
    productoId INT,
    CONSTRAINT fk_detalleingreso_ingreso  FOREIGN KEY (ingresoId)  REFERENCES Ingreso(id),
    CONSTRAINT fk_detalleingreso_producto FOREIGN KEY (productoId) REFERENCES Producto(id)
);

-- ------------------------------------------------------------
-- 3. Datos iniciales
-- ------------------------------------------------------------

-- Módulos
INSERT IGNORE INTO Modulo (id, nombre, descripcion, estado) VALUES
(1, 'Usuarios',  'Gestión de usuarios del sistema',        TRUE),
(2, 'Roles',     'Gestión de roles y permisos',            TRUE),
(3, 'Productos', 'Gestión de productos y categorías',      TRUE),
(4, 'Ventas',    'Registro y consulta de ventas',          TRUE),
(5, 'Ingresos',  'Registro y consulta de ingresos',        TRUE),
(6, 'Sesiones',  'Consulta de sesiones de usuario',        TRUE);

-- Roles
INSERT IGNORE INTO Rol (id, nombre, descripcion, estado) VALUES
(1, 'ADMIN',    'Administrador con acceso total',   TRUE),
(2, 'VENDEDOR', 'Vendedor con acceso limitado',     TRUE);

-- Permisos ADMIN (todos)
INSERT IGNORE INTO RolPermiso (rolId, moduloId, lectura, escritura) VALUES
(1, 1, TRUE, TRUE),
(1, 2, TRUE, TRUE),
(1, 3, TRUE, TRUE),
(1, 4, TRUE, TRUE),
(1, 5, TRUE, TRUE),
(1, 6, TRUE, TRUE);

-- Permisos VENDEDOR (productos y ventas)
INSERT IGNORE INTO RolPermiso (rolId, moduloId, lectura, escritura) VALUES
(2, 3, TRUE, TRUE),
(2, 4, TRUE, TRUE);

-- Categorías base
INSERT IGNORE INTO Categoria (id, nombre, prefijo) VALUES
(1, 'Farmacia',                 'MED'),
(2, 'Bebes',                    'BEB'),
(3, 'Cuidado Personal',         'CUP'),
(4, 'Vitaminas y Suplementos',  'VIT'),
(5, 'Equipos Médicos',          'EQM'),
(6, 'Higiene',                  'HIG'),
(7, 'Otros',                    'OTR');

-- Usuario ADMIN inicial
-- Contraseña: Admin1234! (BCrypt 10 rounds)
INSERT IGNORE INTO Usuario (id, nombre, apellidoPaterno, email, passwordHash, dni, mustChangePassword, estado, rolId) VALUES
(1, 'Admin', 'PharmMan', 'admin@pharmman.com',
 '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
 '00000000', FALSE, TRUE, 1);
