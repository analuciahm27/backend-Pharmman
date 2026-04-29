-- Agregar campo prefijo a la tabla Categoria
ALTER TABLE Categoria ADD COLUMN prefijo VARCHAR(5) NULL;

-- Asignar prefijos a las categorías existentes
UPDATE Categoria SET prefijo = 'MED' WHERE nombre = 'Farmacia';
UPDATE Categoria SET prefijo = 'BEB' WHERE nombre = 'Bebes';
UPDATE Categoria SET prefijo = 'CUP' WHERE nombre = 'Cuidado Personal';
UPDATE Categoria SET prefijo = 'VIT' WHERE nombre = 'Vitaminas y Suplementos';
UPDATE Categoria SET prefijo = 'EQM' WHERE nombre = 'Equipos Médicos';
UPDATE Categoria SET prefijo = 'HIG' WHERE nombre = 'Higiene';
UPDATE Categoria SET prefijo = 'OTR' WHERE nombre = 'Otros';
