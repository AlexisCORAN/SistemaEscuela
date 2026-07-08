# Sistema de Gestión Académica
 
## 1. Visión General
**SistemaEscuela**  es un sistema transaccional de gestión académica diseñado para administrar el ciclo escolar garantizando una consistencia absoluta de los datos. Construido con Java y SQL Server, el sistema implementa un diseño estructural híbrido: utiliza el **patrón MVC (Modelo-Vista-Controlador)** para la interacción fluida en la interfaz gráfica, respaldado por una estricta **arquitectura en capas (Servicios y DAO)**. Esta separación de responsabilidades aísla la lógica de negocio de la persistencia de datos, asegurando un entorno modular, seguro y escalable.
 
## 2. Especificaciones Técnicas
 
- **Lenguaje:** Java (Java Swing para la interfaz gráfica)
- **Base de Datos:** SQL Server (Transaccional)
- **Patrones de Arquitectura:**
  - **DAO (Data Access Object):** Encapsulación de consultas SQL.
  - **Servicio:** Gestión de la lógica de negocio y transaccionalidad.
  - **Modelo:** Definición de entidades de dominio.
- **Gestión de Transacciones:** Implementación de `TransactionRunner` para garantizar operaciones atómicas (ACID).
## 3. Módulos Funcionales
 
### A. Gestión de Matrículas
- Proceso automatizado de inscripción con vinculación estricta entre alumnos, cursos y grados.
- **Inicialización Preventiva:** el sistema genera automáticamente las estructuras de registro bimestral (4 periodos) al momento de la matrícula, evitando la creación de registros huérfanos.
- **Validación de Consistencia:** control de duplicidad de matrículas por alumno y año escolar.
### B. Gestión de Calificaciones
- Registro de notas mediante un sistema de pesos ponderados (Práctica, Tarea, Parcial, Bimestral).
- **Seguridad:** implementación de *triggers* a nivel de base de datos (`trg_Lock_Notas_Cerradas`) que impiden la modificación de evaluaciones en bimestres cerrados, garantizando la inmutabilidad histórica.
- Recálculo dinámico de promedios ponderados tras cada inserción o eliminación de nota.
### C. Reportes de Riesgo Académico
- Motor de detección de estudiantes en riesgo académico mediante consultas SQL con funciones de agregación (`SUM`, `HAVING`).
- Identificación de alumnos con promedios inferiores al umbral de aprobación.
## 4. Estructura de la Base de Datos
 
El sistema se apoya en una base de datos relacional normalizada:
 
- **Entidades:** `Alumno`, `Docente`, `Apoderado`, `Grado`, `Curso`.
- **Procesos:** `Matricula`, `MatriculaCurso`, `RegistroBimestral`, `Evaluacion`.
- **Auditoría:** triggers automáticos para el registro de cambios de estado y fechas de modificación en alumnos, docentes y matrículas.
## 5. Instrucciones de Despliegue
 
1. **Base de Datos:** ejecutar el script `sql base de datos.sql` en SQL Server para crear la estructura, restricciones y cargar el dataset inicial.
2. **Configuración:** actualizar los parámetros de conexión en la clase `config.ConexionDB`.
3. **Ejecución:** compilar mediante el entorno de desarrollo (ej. NetBeans) y ejecutar la clase principal `VentanaPrincipal`.

 
- **Estabilidad:** integridad de llaves foráneas verificada y manejo de excepciones transaccionales implementado.
- **Requisitos:** JDK 17+, SQL Server 2019 o superior, librería de conexión JDBC para SQL Server.
---
