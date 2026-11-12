# SistemaEscuela — Transactional Academic Management System

## 1. Overview
**SistemaEscuela** is a transactional academic management system designed to administer the school lifecycle while guaranteeing strict data consistency. Built with **Java** and **SQL Server**, the application implements a hybrid structural design: it leverages the **MVC (Model-View-Controller)** pattern for responsive GUI interaction, backed by a strict layered architecture (**Service** and **DAO** layers). This separation of concerns isolates business logic from data persistence, ensuring a modular, secure, and scalable environment.

## 2. Technical Specifications
* **Language:** Java (Java Swing for the graphical user interface)
* **Database Engine:** Microsoft SQL Server (Transactional)
* **Architecture Patterns:**
  * **DAO (Data Access Object):** Encapsulation of SQL queries and JDBC operations.
  * **Service Layer:** Business logic orchestration and transactional control.
  * **Model:** Domain entity definitions.
* **Transaction Management:** Custom `TransactionRunner` implementation ensuring atomic, consistent, isolated, and durable (**ACID**) operations across multi-table writes.

## 3. Functional Modules

### A. Enrollment Management
* Automated enrollment workflow enforcing strict relational binding between students, courses, and grade levels.
* **Preventive Initialization:** Automatically generates bimonthly grading structures (4 academic periods) upon enrollment, preventing orphan records.
* **Consistency Validation:** Enforces business rules to prevent duplicate enrollments per student and academic year.

### B. Grading & Evaluation Management
* Grade recording using a weighted scoring engine (Practice, Homework, Midterm, and Bimonthly exams).
* **Database-Level Security:** Implements SQL triggers (`trg_Lock_Notas_Cerradas`) that block modifications to evaluations within closed academic periods, guaranteeing historical immutability.
* **Dynamic Recalculation:** Automatically recalculates weighted student averages after every grade insertion or deletion.

### C. Academic Risk Reporting
* Automated detection engine identifying at-risk students via optimized SQL queries utilizing aggregation functions (`SUM`, `HAVING`).
* Flags students whose cumulative weighted averages fall below the passing threshold.

## 4. Database Architecture
The system is backed by a normalized relational database schema:
* **Core Entities:** `Alumno` (Student), `Docente` (Teacher), `Apoderado` (Guardian), `Grado` (Grade Level), `Curso` (Course).
* **Transactional Processes:** `Matricula` (Enrollment), `MatriculaCurso`, `RegistroBimestral`, `Evaluacion`.
* **Auditing:** Automated database triggers logging status changes and modification timestamps across students, teachers, and enrollments.

## 5. Deployment & Setup Instructions

### Prerequisites
* **JDK 17** or higher
* **Microsoft SQL Server 2019** or higher
* **Microsoft JDBC Driver** for SQL Server

### Installation Steps
1. **Database Setup:** Execute the SQL script `base de datos.sql` in SQL Server Management Studio (SSMS) to create the schema, constraints, triggers, and load the initial dataset.
2. **Connection Configuration:** Update the database credentials and connection string inside `config.ConexionDB`.
3. **Build & Run:** Compile the project using your preferred IDE (e.g., NetBeans) and execute the main entry class `VentanaPrincipal`.

---
Developed by **Alexis Coronado** — Systems Engineering Student & Full-Stack Developer
