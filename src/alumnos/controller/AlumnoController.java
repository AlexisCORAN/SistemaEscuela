/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package alumnos.controller;

import java.sql.Connection;
import java.util.List;
import java.util.Collections;
import alumnos.dao.IAlumnoDAO;
import alumnos.dao.AlumnoDAOImpl;
import alumnos.dao.IApoderadoDAO;
import alumnos.dao.ApoderadoDAOImpl;
import alumnos.model.Alumno;
import alumnos.model.Apoderado;
import config.ConexionDB;
/**
 *
 * @author Alexis
 */
public class AlumnoController {

    public AlumnoController() {
    }

    public List<Alumno> obtenerAlumnos() {
        try {
            final Connection conn = ConexionDB.getInstance().getConexion();
            final IAlumnoDAO alumnoDAO = new AlumnoDAOImpl(conn);
            return alumnoDAO.listarTodos();
        } catch (final Exception e) {
            System.err.println("Error al obtener alumnos: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<Alumno> buscarPorCodigo(final String criterio) {
        if (criterio == null || criterio.trim().isEmpty() || criterio.equals("Buscar por Código")) {
            return obtenerAlumnos();
        }
        try {
            final Connection conn = ConexionDB.getInstance().getConexion();
            final IAlumnoDAO alumnoDAO = new AlumnoDAOImpl(conn);
            final Alumno resultado = alumnoDAO.buscarPorCodigo(criterio.trim());
            return resultado != null ? List.of(resultado) : Collections.emptyList();
        } catch (final Exception e) {
            System.err.println("Error al buscar alumno: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public boolean registrarAlumnoYApoderado(final Alumno alumno) {
        Connection conn = null;
        try {
            conn = ConexionDB.getInstance().getConexion();
            conn.setAutoCommit(false); 

            final IApoderadoDAO apoderadoDAO = new ApoderadoDAOImpl(conn);
            final IAlumnoDAO alumnoDAO = new AlumnoDAOImpl(conn);

            final Apoderado apoderado = alumno.getApoderado();
            if (!apoderadoDAO.insertar(apoderado)) {
                conn.rollback(); 
                return false;
            }

            final String ultimoCod = alumnoDAO.obtenerUltimoCodigo();
            int correlativo = 1;
            if (ultimoCod != null && ultimoCod.contains("-")) {
                final String[] partes = ultimoCod.split("-");
                if (partes.length == 3) {
                    correlativo = Integer.parseInt(partes[2]) + 1;
                }
            }

            alumno.generarCodigoEstudiante(correlativo);
            alumno.setActivo(true);

            if (!alumnoDAO.insertar(alumno)) {
                conn.rollback(); 
                return false;
            }

            conn.commit();
            return true;

        } catch (final Exception e) {
            System.err.println("Error transaccional al registrar: " + e.getMessage());
            try {
                if (conn != null) conn.rollback();
            } catch (Exception ex) {
                System.err.println("Error ejecutando el rollback: " + ex.getMessage());
            }
            return false;
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (Exception ex) {
                System.err.println("Error restaurando autocommit: " + ex.getMessage());
            }
        }
    }

    public boolean modificarAlumnoYApoderado(final Alumno alumno) {
        Connection conn = null;
        try {
            conn = ConexionDB.getInstance().getConexion();
            conn.setAutoCommit(false);

            final IApoderadoDAO apoderadoDAO = new ApoderadoDAOImpl(conn);
            final IAlumnoDAO alumnoDAO = new AlumnoDAOImpl(conn);

            if (!apoderadoDAO.actualizar(alumno.getApoderado())) {
                conn.rollback();
                return false;
            }

            if (!alumnoDAO.actualizar(alumno)) {
                conn.rollback();
                return false;
            }

            conn.commit();
            return true;

        } catch (final Exception e) {
            System.err.println("Error transaccional al modificar alumno: " + e.getMessage());
            try {
                if (conn != null) conn.rollback();
            } catch (Exception ex) {
                System.err.println("Error ejecutando el rollback: " + ex.getMessage());
            }
            return false;
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (Exception ex) {
                System.err.println("Error restaurando autocommit: " + ex.getMessage());
            }
        }
    }

    public boolean procesarBajaAlumno(final Alumno alumno) {
        if (alumno == null) {
            return false;
        }
        try {
            final Connection conn = ConexionDB.getInstance().getConexion();
            final IAlumnoDAO alumnoDAO = new AlumnoDAOImpl(conn);
            
            alumno.setActivo(false); 
            return alumnoDAO.actualizar(alumno);
        } catch (final Exception e) {
            System.err.println("Error al procesar baja: " + e.getMessage());
            return false;
        }
    }
}
