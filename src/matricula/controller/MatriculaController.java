/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package matricula.controller;

import alumnos.dao.AlumnoDAOImpl;
import alumnos.dao.IAlumnoDAO;
import alumnos.model.Alumno;
import config.ConexionDB;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import matricula.dao.IMatriculaDAO;
import matricula.dao.MatriculaDAOImpl;
import matricula.model.Matricula;
import plan_estudios.model.Grado;

/**
 *
 * @author Alexis
 */

public class MatriculaController {

    public MatriculaController() {
    }

    public List<Matricula> obtenerMatriculas() {
        try {
            final Connection conn = ConexionDB.getInstance().getConexion();
            final IMatriculaDAO matriculaDAO = new MatriculaDAOImpl(conn);
            return matriculaDAO.listarTodos();
        } catch (final Exception e) {
            System.err.println("Error al obtener matrículas: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<Matricula> buscarMatriculas(final String criterio) {
        if (criterio == null || criterio.trim().isEmpty() || criterio.equals("Buscar por DNI, Codigo o Nombre del Alumno")) {
            return obtenerMatriculas();
        }

        try {
            final Connection conn = ConexionDB.getInstance().getConexion();
            final IMatriculaDAO matriculaDAO = new MatriculaDAOImpl(conn);
            final Matricula resultado = matriculaDAO.obtenerMatriculaActiva(criterio.trim(), java.time.LocalDate.now().getYear());
            if (resultado != null) {
                return List.of(resultado);
            }

            final List<Matricula> todas = obtenerMatriculas();
            final List<Matricula> filtradas = new ArrayList<>();
            for (final Matricula m : todas) {
                if (m.getAlumno() != null &&
                    (m.getAlumno().getDni().contains(criterio) ||
                     m.getAlumno().getCodigoEstudiante().equalsIgnoreCase(criterio) ||
                     m.getAlumno().getNombreCompleto().toLowerCase().contains(criterio.toLowerCase()))) {
                    filtradas.add(m);
                }
            }
            return filtradas;
        } catch (final Exception e) {
            System.err.println("Error en búsqueda adaptativa: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public Alumno buscarAlumnoParaMatricula(final String criterio) throws IllegalStateException {
        if (criterio == null || criterio.trim().isEmpty()) return null;

        try {
            final Connection conn = ConexionDB.getInstance().getConexion();
            final IAlumnoDAO alumnoDAO = new AlumnoDAOImpl(conn);

            Alumno alum = alumnoDAO.buscarPorCodigo(criterio.trim());
            if (alum == null) {
                final List<Alumno> todosAlumnos = alumnoDAO.listarTodos();
                for (final Alumno a : todosAlumnos) {
                    if (a.getDni().equals(criterio.trim())) {
                        alum = a;
                        break;
                    }
                }
            }

            if (alum != null && !alum.isActivo()) {
                throw new IllegalStateException("El estudiante se encuentra en estado RETIRADO. No se puede matricular.");
            }
            return alum;
        } catch (final IllegalStateException e) {
            throw e;
        } catch (final Exception e) {
            System.err.println("Error al buscar alumno para matrícula: " + e.getMessage());
            return null;
        }
    }

    public List<Grado> obtenerGradosConCursos() {
        try {
            final Connection conn = ConexionDB.getInstance().getConexion();
            final IMatriculaDAO matriculaDAO = new MatriculaDAOImpl(conn);
            return matriculaDAO.listarGradosConCursos();
        } catch (final Exception e) {
            System.err.println("Error al obtener grados: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public boolean registrarMatricula(final Matricula m) {
        Connection conn = null;
        try {
            conn = ConexionDB.getInstance().getConexion();
            conn.setAutoCommit(false);

            final IMatriculaDAO matriculaDAO = new MatriculaDAOImpl(conn);

            final String ultimoCod = matriculaDAO.obtenerUltimoCodigo();
            int correlativo = 1;
            if (ultimoCod != null && ultimoCod.contains("-")) {
                final String[] partes = ultimoCod.split("-");
                if (partes.length == 3) {
                    correlativo = Integer.parseInt(partes[2]) + 1;
                }
            }

            m.generarCodigoMatricula(correlativo);
            m.setActivo(true);

            if (!matriculaDAO.insertar(m)) {
                conn.rollback();
                return false;
            }

            conn.commit();
            return true;

        } catch (final Exception e) {
            System.err.println("Error transaccional al registrar matrícula: " + e.getMessage());
            try {
                if (conn != null) conn.rollback();
            } catch (final Exception ex) {
                System.err.println("Error ejecutando el rollback: " + ex.getMessage());
            }
            return false;
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (final Exception ex) {
                System.err.println("Error restaurando autocommit: " + ex.getMessage());
            }
        }
    }

    public boolean anularMatricula(final Matricula matricula) {
        if (matricula == null || matricula.getId() == null) return false;

        try {
            final Connection conn = ConexionDB.getInstance().getConexion();
            final IMatriculaDAO matriculaDAO = new MatriculaDAOImpl(conn);
            return matriculaDAO.anularMatricula(matricula.getId());
        } catch (final Exception e) {
            System.err.println("Error al anular matrícula: " + e.getMessage());
            return false;
        }
    }
}