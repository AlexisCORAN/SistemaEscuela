CREATE DATABASE ColegioDB;
GO
USE ColegioDB;
GO

CREATE TABLE Apoderado (
    idApoderado INT IDENTITY(1,1) PRIMARY KEY,
    dni CHAR(8) NOT NULL UNIQUE CHECK (dni NOT LIKE '%[^0-9]%'),
    fechaNacimiento DATE NOT NULL,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    parentesco VARCHAR(30) NOT NULL,
    telefono VARCHAR(15) NULL,
    correo VARCHAR(100) NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO' CHECK (estado IN ('ACTIVO', 'INACTIVO')),
    CONSTRAINT CK_Apoderado_Contacto CHECK (telefono IS NOT NULL OR correo IS NOT NULL)
);

CREATE TABLE Grado (
    idGrado INT IDENTITY(1,1) PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    nivel VARCHAR(50) NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO' CHECK (estado IN ('ACTIVO', 'INACTIVO')),
    CONSTRAINT UQ_Grado UNIQUE (nombre, nivel)
);

CREATE TABLE Alumno (
    idAlumno INT IDENTITY(1,1) PRIMARY KEY,
    codigoEstudiante VARCHAR(20) NOT NULL UNIQUE,
    dni CHAR(8) NOT NULL UNIQUE CHECK (dni NOT LIKE '%[^0-9]%'),
    fechaNacimiento DATE NOT NULL,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO' CHECK (estado IN ('ACTIVO', 'RETIRADO')),
    fechaCambioEstado DATETIME NULL,
    motivoCambioEstado VARCHAR(200) NULL,
    idApoderado INT NOT NULL,
    CONSTRAINT FK_Alumno_Apoderado FOREIGN KEY (idApoderado) REFERENCES Apoderado(idApoderado)
);

CREATE TABLE Docente (
    idDocente INT IDENTITY(1,1) PRIMARY KEY,
    codigoDocente VARCHAR(20) NOT NULL UNIQUE,
    dni CHAR(8) NOT NULL UNIQUE CHECK (dni NOT LIKE '%[^0-9]%'),
    fechaNacimiento DATE NOT NULL,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    tituloProfesional VARCHAR(100) NOT NULL,
    especialidadAcademica VARCHAR(100) NULL,
    telefono VARCHAR(15) NULL,
    correo VARCHAR(100) NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO' CHECK (estado IN ('ACTIVO', 'CESADO')),
    fechaCambioEstado DATETIME NULL,
    motivoCambioEstado VARCHAR(200) NULL
);

CREATE TABLE Curso (
    idCurso INT IDENTITY(1,1) PRIMARY KEY,
    codigoCurso VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    horasSemanales INT NOT NULL DEFAULT 2,
    idDocente INT NULL,
    idGrado INT NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO' CHECK (estado IN ('ACTIVO', 'INACTIVO')),
    CONSTRAINT FK_Curso_Docente FOREIGN KEY (idDocente) REFERENCES Docente(idDocente),
    CONSTRAINT FK_Curso_Grado FOREIGN KEY (idGrado) REFERENCES Grado(idGrado)
);

CREATE TABLE Matricula (
    idMatricula INT IDENTITY(1,1) PRIMARY KEY,
    codigoMatricula VARCHAR(20) NOT NULL UNIQUE,
    idAlumno INT NOT NULL,
    idGrado INT NOT NULL,
    fechaMatricula DATE NOT NULL DEFAULT GETDATE(),
    anioEscolar INT NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'VIGENTE' CHECK (estado IN ('VIGENTE', 'ANULADA')),
    fechaCambioEstado DATETIME NULL,
    motivoCambioEstado VARCHAR(200) NULL,
    CONSTRAINT FK_Matricula_Alumno FOREIGN KEY (idAlumno) REFERENCES Alumno(idAlumno),
    CONSTRAINT FK_Matricula_Grado FOREIGN KEY (idGrado) REFERENCES Grado(idGrado),
    CONSTRAINT UQ_Matricula_Alumno_Anio UNIQUE (idAlumno, anioEscolar)
);

CREATE TABLE MatriculaCurso (
    idMatriculaCurso INT IDENTITY(1,1) PRIMARY KEY,
    idMatricula INT NOT NULL,
    idCurso INT NOT NULL,
    notaFinal DECIMAL(4,2) DEFAULT 0.0 CHECK (notaFinal >= 0.0 AND notaFinal <= 20.0),
    CONSTRAINT FK_MC_Matricula FOREIGN KEY (idMatricula) REFERENCES Matricula(idMatricula),
    CONSTRAINT FK_MC_Curso FOREIGN KEY (idCurso) REFERENCES Curso(idCurso),
    CONSTRAINT UQ_MatriculaCurso UNIQUE (idMatricula, idCurso)
);

CREATE TABLE RegistroBimestral (
    idRegistroBimestral INT IDENTITY(1,1) PRIMARY KEY,
    idMatriculaCurso INT NOT NULL,
    bimestre INT NOT NULL CHECK (bimestre BETWEEN 1 AND 4),
    estado VARCHAR(20) NOT NULL DEFAULT 'ABIERTO' CHECK (estado IN ('ABIERTO', 'CERRADO')),
    fechaCierre DATETIME NULL,
    CONSTRAINT FK_RB_MC FOREIGN KEY (idMatriculaCurso) REFERENCES MatriculaCurso(idMatriculaCurso),
    CONSTRAINT UQ_RegistroBimestral UNIQUE (idMatriculaCurso, bimestre)
);

CREATE TABLE Evaluacion (
    idEvaluacion INT IDENTITY(1,1) PRIMARY KEY,
    idRegistroBimestral INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    tipo VARCHAR(50) NOT NULL CHECK (tipo IN ('PRACTICA', 'PARCIAL', 'BIMESTRAL', 'TAREA')),
    nota DECIMAL(4,2) NOT NULL CHECK (nota >= 0.0 AND nota <= 20.0),
    peso DECIMAL(3,2) NOT NULL,
    CONSTRAINT FK_E_RB FOREIGN KEY (idRegistroBimestral) REFERENCES RegistroBimestral(idRegistroBimestral)
);
GO

CREATE TRIGGER trg_Audit_Alumno ON Alumno AFTER UPDATE AS
    IF UPDATE(estado) UPDATE Alumno SET fechaCambioEstado = GETDATE() FROM Alumno a JOIN inserted i ON a.idAlumno = i.idAlumno;
GO

CREATE TRIGGER trg_Audit_Matricula ON Matricula AFTER UPDATE AS
    IF UPDATE(estado) UPDATE Matricula SET fechaCambioEstado = GETDATE() FROM Matricula m JOIN inserted i ON m.idMatricula = i.idMatricula;
GO

CREATE TRIGGER trg_Audit_Docente ON Docente AFTER UPDATE AS
    IF UPDATE(estado) UPDATE Docente SET fechaCambioEstado = GETDATE() FROM Docente d JOIN inserted i ON d.idDocente = i.idDocente;
GO

CREATE TRIGGER trg_Lock_Notas_Cerradas ON Evaluacion AFTER INSERT, UPDATE, DELETE AS
BEGIN
    IF EXISTS (
        SELECT 1 FROM RegistroBimestral rb JOIN inserted i ON rb.idRegistroBimestral = i.idRegistroBimestral WHERE rb.estado = 'CERRADO'
        UNION ALL
        SELECT 1 FROM RegistroBimestral rb JOIN deleted d ON rb.idRegistroBimestral = d.idRegistroBimestral WHERE rb.estado = 'CERRADO'
    )
    BEGIN
        RAISERROR ('Error: No se pueden modificar notas en un bimestre cerrado.', 16, 1);
        ROLLBACK TRANSACTION;
    END
END;
GO

CREATE INDEX idx_alumno_dni ON Alumno(dni);
CREATE INDEX idx_alumno_codigo ON Alumno(codigoEstudiante);
CREATE INDEX idx_matricula_alumno ON Matricula(idAlumno);
CREATE INDEX idx_matricula_grado ON Matricula(idGrado);
CREATE INDEX idx_rb_mc ON RegistroBimestral(idMatriculaCurso);
GO