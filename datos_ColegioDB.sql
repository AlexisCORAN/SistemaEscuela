-- 1. Insertar Grados (11 registros exactos: 6 Primaria, 5 Secundaria)
INSERT INTO Grado (nombre, nivel) VALUES
('1°', 'Primaria'), ('2°', 'Primaria'), ('3°', 'Primaria'),
('4°', 'Primaria'), ('5°', 'Primaria'), ('6°', 'Primaria'),
('1°', 'Secundaria'), ('2°', 'Secundaria'), ('3°', 'Secundaria'),
('4°', 'Secundaria'), ('5°', 'Secundaria');

-- 2. Insertar Apoderados (5 registros)
INSERT INTO Apoderado (dni, fechaNacimiento, nombres, apellidos, parentesco, telefono, correo) VALUES
('40123456', '1980-05-15', 'Carlos', 'Mendoza Vargas', 'Padre', '987654321', 'cmendoza@gmail.com'),
('41234567', '1982-08-22', 'Maria', 'Rojas Torres', 'Madre', '912345678', 'mrojas@hotmail.com'),
('09876543', '1975-11-03', 'Luis', 'Quispe Mamani', 'Tío', '934567890', 'lquispe@yahoo.com'),
('45678901', '1988-02-14', 'Ana', 'Condori Silva', 'Madre', '945678901', 'acondori@outlook.com'),
('76543210', '1990-07-30', 'Jorge', 'Perez Garcia', 'Padre', '956789012', 'jperez@gmail.com');

-- 3. Insertar Alumnos (10 registros vinculados a los apoderados)
INSERT INTO Alumno (codigoEstudiante, dni, fechaNacimiento, nombres, apellidos, idApoderado) VALUES
('ALU-2026-001', '70111222', '2015-04-10', 'Mateo', 'Mendoza Rojas', 1),
('ALU-2026-002', '70222333', '2015-06-18', 'Camila', 'Mendoza Rojas', 1),
('ALU-2026-003', '70333444', '2013-09-25', 'Sebastian', 'Rojas Torres', 2),
('ALU-2026-004', '70444555', '2012-12-05', 'Valentina', 'Quispe Mamani', 3),
('ALU-2026-005', '70555666', '2010-03-14', 'Matias', 'Condori Silva', 4),
('ALU-2026-006', '70666777', '2016-01-20', 'Luciana', 'Perez Garcia', 5),
('ALU-2026-007', '70777888', '2014-08-08', 'Diego', 'Mendoza Vargas', 1),
('ALU-2026-008', '70888999', '2011-11-11', 'Mariana', 'Rojas Torres', 2),
('ALU-2026-009', '70999000', '2009-05-22', 'Joaquin', 'Quispe Mamani', 3),
('ALU-2026-010', '71000111', '2010-07-07', 'Valeria', 'Condori Silva', 4);

-- 4. Insertar Docentes (5 registros)
INSERT INTO Docente (codigoDocente, dni, fechaNacimiento, nombres, apellidos, tituloProfesional, especialidadAcademica, telefono) VALUES
('DOC-001', '10203040', '1985-02-10', 'Roberto', 'Alvarado Diaz', 'Licenciado en Educación', 'Matemáticas', '980123456'),
('DOC-002', '20304050', '1979-10-25', 'Carmen', 'Merma Morocco', 'Licenciada en Educación', 'Comunicación', '981234567'),
('DOC-003', '30405060', '1992-06-15', 'Julio', 'Salazar Flores', 'Ingeniero de Sistemas', 'Computación', '982345678'),
('DOC-004', '40506070', '1988-04-20', 'Patricia', 'Gomez Ruiz', 'Licenciada en Educación', 'Ciencias Sociales', '983456789'),
('DOC-005', '50607080', '1981-12-05', 'Fernando', 'Castro Vega', 'Profesor de Educación Física', 'Deportes', '984567890');

-- 5. Insertar Cursos (10 registros vinculados a grados y docentes)
-- Matemáticas y Comunicación para 1° Primaria (ID 1) y 1° Secundaria (ID 7)
INSERT INTO Curso (codigoCurso, nombre, horasSemanales, idDocente, idGrado) VALUES
('CUR-MAT-P1', 'Matemáticas Básica', 4, 1, 1),
('CUR-COM-P1', 'Lectura y Escritura', 4, 2, 1),
('CUR-MAT-S1', 'Álgebra y Geometría', 5, 1, 7),
('CUR-COM-S1', 'Literatura', 5, 2, 7),
('CUR-COM-P2', 'Computación Básica', 2, 3, 2),
('CUR-HIS-S1', 'Historia del Perú', 3, 4, 7),
('CUR-DEP-P1', 'Educación Física', 2, 5, 1),
('CUR-DEP-S1', 'Deportes', 2, 5, 7),
('CUR-ING-P3', 'Inglés Inicial', 3, 2, 3),
('CUR-CTA-S2', 'Ciencia y Tecnología', 4, 4, 8);

-- 6. Insertar Matrículas (10 registros, uno para cada alumno)
INSERT INTO Matricula (codigoMatricula, idAlumno, idGrado, anioEscolar) VALUES
('MAT-2026-001', 1, 1, 2026), -- Mateo en 1° Primaria
('MAT-2026-002', 2, 1, 2026), -- Camila en 1° Primaria
('MAT-2026-003', 3, 2, 2026), -- Sebastian en 2° Primaria
('MAT-2026-004', 4, 3, 2026), -- Valentina en 3° Primaria
('MAT-2026-005', 5, 7, 2026), -- Matias en 1° Secundaria
('MAT-2026-006', 6, 1, 2026), -- Luciana en 1° Primaria
('MAT-2026-007', 7, 2, 2026), -- Diego en 2° Primaria
('MAT-2026-008', 8, 7, 2026), -- Mariana en 1° Secundaria
('MAT-2026-009', 9, 8, 2026), -- Joaquin en 2° Secundaria
('MAT-2026-010', 10, 7, 2026); -- Valeria en 1° Secundaria

-- 7. Insertar MatriculaCurso (12 registros vinculando alumnos a los cursos de su grado)
INSERT INTO MatriculaCurso (idMatricula, idCurso) VALUES
(1, 1), (1, 2), (1, 7), -- Mateo en Mate, Comu y EF (1° Primaria)
(2, 1), (2, 2),         -- Camila en Mate y Comu (1° Primaria)
(6, 1), (6, 7),         -- Luciana en Mate y EF (1° Primaria)
(5, 3), (5, 4), (5, 6), -- Matias en Álgebra, Literatura e Historia (1° Secundaria)
(8, 3), (8, 8);         -- Mariana en Álgebra y Deportes (1° Secundaria)

-- 8. Insertar RegistroBimestral (48 registros: 4 bimestres por cada MatriculaCurso)
-- Esto simula el comportamiento automático que implementamos en MatriculaService
INSERT INTO RegistroBimestral (idMatriculaCurso, bimestre, estado)
SELECT mc.idMatriculaCurso, b.numeroBimestre, 'ABIERTO'
FROM MatriculaCurso mc
CROSS JOIN (VALUES (1), (2), (3), (4)) AS b(numeroBimestre);

-- 9. Insertar Evaluaciones (12 registros de prueba para el Bimestre 1)
-- Insertamos notas para Mateo (MatriculaCurso 1 -> RegistroBimestral 1)
INSERT INTO Evaluacion (idRegistroBimestral, nombre, tipo, nota, peso) VALUES
(1, 'Práctica Calificada 1', 'PRACTICA', 16.50, 0.20),
(1, 'Tarea Domiciliaria 1', 'TAREA', 18.00, 0.20),
(1, 'Examen Parcial', 'PARCIAL', 15.00, 0.30),
(1, 'Examen Bimestral', 'BIMESTRAL', 17.00, 0.30);

-- Insertamos notas para Camila (MatriculaCurso 4 -> RegistroBimestral 13)
INSERT INTO Evaluacion (idRegistroBimestral, nombre, tipo, nota, peso) VALUES
(13, 'Práctica Calificada 1', 'PRACTICA', 14.00, 0.20),
(13, 'Tarea Domiciliaria 1', 'TAREA', 15.00, 0.20),
(13, 'Examen Parcial', 'PARCIAL', 13.50, 0.30),
(13, 'Examen Bimestral', 'BIMESTRAL', 16.00, 0.30);

-- Insertamos notas para Matias (MatriculaCurso 7 -> RegistroBimestral 25)
INSERT INTO Evaluacion (idRegistroBimestral, nombre, tipo, nota, peso) VALUES
(25, 'Práctica Álgebra', 'PRACTICA', 12.00, 0.20),
(25, 'Trabajo de Investigación', 'TAREA', 14.50, 0.20),
(25, 'Simulacro Parcial', 'PARCIAL', 11.00, 0.30),
(25, 'Examen Bimestral', 'BIMESTRAL', 13.00, 0.30);