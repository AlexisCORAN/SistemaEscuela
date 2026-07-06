/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package alumnos.service;
import java.util.Collections;
import java.util.List;
import alumnos.dao.AlumnoDAOImpl;
import alumnos.dao.ApoderadoDAOImpl;
import alumnos.dao.IAlumnoDAO;
import alumnos.dao.IApoderadoDAO;
import alumnos.model.Alumno;
import java.sql.Connection;
import java.sql.SQLException;
import config.ConexionDB;
import shared.TransactionRunner;

/**
 *
 * @author Alexis
 */

public class AlumnoService {
    

    public List<Alumno> obtenerAlumnos() {
        try (Connection conn = ConexionDB.getInstance().getConexion()) {
            IAlumnoDAO alumnoDAO = new AlumnoDAOImpl(conn);
            return alumnoDAO.listarTodos();
        } catch (Exception e) {
            System.err.println("Error al obtener alumnos: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public Alumno buscarAlumnoPorCodigoExacto(String codigo) {
        if (codigo == null || codigo.isBlank()) return null;

        try (Connection conn = ConexionDB.getInstance().getConexion()) {
            IAlumnoDAO alumnoDAO = new AlumnoDAOImpl(conn);
            return alumnoDAO.buscarPorCodigo(codigo.trim());
        } catch (SQLException e) {
            System.err.println("Error al buscar alumno exacto: " + e.getMessage());
            return null;
        }
    }

    public List<Alumno> obtenerAlumnosPorEstado(String filtroEstado) {
        if (filtroEstado == null || filtroEstado.equals("TODOS")) {
            return obtenerAlumnos();
        }
        boolean esActivo = filtroEstado.equals("ACTIVOS");
        try (Connection conn = ConexionDB.getInstance().getConexion()) {
            IAlumnoDAO alumnoDAO = new AlumnoDAOImpl(conn);
            return alumnoDAO.listarPorEstado(esActivo);
        } catch (Exception e) {
            System.err.println("Error al filtrar alumnos por estado: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public void registrarAlumno(Alumno alumno) {
        validarExistenciaDnis(alumno.getDni(), alumno.getApoderado().getDni(), null, null);

        TransactionRunner.ejecutar(conn -> {
            IApoderadoDAO apoderadoDAO = new ApoderadoDAOImpl(conn);
            IAlumnoDAO alumnoDAO = new AlumnoDAOImpl(conn);

            if (!apoderadoDAO.insertar(alumno.getApoderado())) {
                throw new RuntimeException("Error al registrar el apoderado en la base de datos.");
            }

            generarYAsignarCodigo(alumno, alumnoDAO);

            if (!alumnoDAO.insertar(alumno)) {
                throw new RuntimeException("Error al registrar el alumno en la base de datos.");
            }
            return null; 
        }, null);
    }

    public void actualizarAlumno(Alumno alumno) {
        validarExistenciaDnis(alumno.getDni(), alumno.getApoderado().getDni(), alumno.getId(), alumno.getApoderado().getId());

        TransactionRunner.ejecutar(conn -> {
            IApoderadoDAO apoderadoDAO = new ApoderadoDAOImpl(conn);
            IAlumnoDAO alumnoDAO = new AlumnoDAOImpl(conn);

            if (!apoderadoDAO.actualizar(alumno.getApoderado())) {
                throw new RuntimeException("Error al actualizar los datos del apoderado.");
            }
            if (!alumnoDAO.actualizar(alumno)) {
                throw new RuntimeException("Error al actualizar los datos del alumno.");
            }
            return null;
        }, null);
    }


    private void generarYAsignarCodigo(Alumno alumno, IAlumnoDAO alumnoDAO) {
        String ultimoCod = alumnoDAO.obtenerUltimoCodigo();
        int correlativo = 1;
        
        if (ultimoCod != null && ultimoCod.contains("-")) {
            String[] partes = ultimoCod.split("-");
            if (partes.length == 3) {
                correlativo = Integer.parseInt(partes[2]) + 1;
            }
        }

        alumno.generarCodigoEstudiante(correlativo);
    }
    
    private void validarExistenciaDnis(String dniAlumno, String dniApoderado, Integer idAlumnoExcluido, Integer idApoderadoExcluido) {
        try (Connection conn = ConexionDB.getInstance().getConexion()) {
            IAlumnoDAO alumnoDAO = new AlumnoDAOImpl(conn);
            IApoderadoDAO apoderadoDAO = new ApoderadoDAOImpl(conn);

            if (alumnoDAO.existeDni(dniAlumno, idAlumnoExcluido)) {
                throw new IllegalArgumentException("El DNI del alumno ya está registrado.");
            }
            if (apoderadoDAO.existeDni(dniApoderado, idApoderadoExcluido)) {
                throw new IllegalArgumentException("El DNI del apoderado ya está registrado.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en validación de base de datos", e);
        }
    }
    
    public void procesarBajaPorCodigo(String codigoEstudiante) {
        TransactionRunner.ejecutar(conn -> {
            IAlumnoDAO alumnoDAO = new AlumnoDAOImpl(conn);
            Alumno alumno = alumnoDAO.buscarPorCodigo(codigoEstudiante);

            if (alumno == null) {
                throw new IllegalArgumentException("No se encontró el alumno con código: " + codigoEstudiante);
            }
            if (!alumno.isActivo()) {
                throw new IllegalArgumentException("El alumno ya se encuentra dado de baja.");
            }
            alumno.setActivo(false); 

            if (!alumnoDAO.actualizar(alumno)) {
                throw new RuntimeException("Error al dar de baja en la base de datos.");
            }            
            return null;
        }, null);
    }

    public void procesarReactivacionPorCodigo(String codigoEstudiante) {
        TransactionRunner.ejecutar(conn -> {
            IAlumnoDAO alumnoDAO = new AlumnoDAOImpl(conn);
            Alumno alumno = alumnoDAO.buscarPorCodigo(codigoEstudiante);

            if (alumno == null) {
                throw new IllegalArgumentException("No se encontró el alumno con código: " + codigoEstudiante);
            }
            
            if (alumno.isActivo()) {
                throw new IllegalArgumentException("El alumno ya se encuentra activo.");
            }

            alumno.setActivo(true); 

            if (!alumnoDAO.actualizar(alumno)) {
                throw new RuntimeException("Error al reactivar en la base de datos.");
            }
            return null;
        }, null);
    }
}