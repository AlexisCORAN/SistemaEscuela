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
    
    public List<Alumno> obtenerAlumnosPorEstado(String filtroEstado) {
        if (filtroEstado == null || filtroEstado.equals("TODOS")) {
            return obtenerAlumnos();
        }

        boolean esActivo = filtroEstado.equals("ACTIVOS");

        try {
            final Connection conn = ConexionDB.getInstance().getConexion();
                final IAlumnoDAO alumnoDAO = new AlumnoDAOImpl(conn); 
            return alumnoDAO.listarPorEstado(esActivo);
        } catch (final Exception e) {
            System.err.println("Error al filtrar alumnos por estado: " + e.getMessage());
            return Collections.emptyList();
        }
    }
    
    public boolean registrarAlumnoDesdeFormulario(
        java.util.Date fechaNacAlumno, String dniAlumno, String nombresAlumno, String apellidosAlumno,
        java.util.Date fechaNacApoderado, String dniApoderado, String nombresApoderado, String apellidosApoderado,
        String parentesco, String telefonoApoderado, String correoApoderado) {

        java.time.LocalDate nacAlum = fechaNacAlumno.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        java.time.LocalDate nacApod = fechaNacApoderado.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

        Apoderado apod = new Apoderado(parentesco, telefonoApoderado, correoApoderado, 
                                       null, dniApoderado, nombresApoderado, apellidosApoderado, nacApod, true);

        Alumno alum = new Alumno("TEMP", apod, null, dniAlumno, nombresAlumno, apellidosAlumno, nacAlum, true);

        return registrarAlumnoYApoderado(alum);
    }

    public boolean modificarAlumnoDesdeFormulario(
        Integer idAlumno, String codigoEstudiante, boolean alumnoActivo,
        java.util.Date fechaNacAlumno, String dniAlumno, String nombresAlumno, String apellidosAlumno,
        Integer idApoderado, boolean apoderadoActivo,
        java.util.Date fechaNacApoderado, String dniApoderado, String nombresApoderado, String apellidosApoderado,
        String parentesco, String telefonoApoderado, String correoApoderado) {

        java.time.LocalDate nacAlum = fechaNacAlumno.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        java.time.LocalDate nacApod = fechaNacApoderado.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

        Apoderado apMod = new Apoderado();
        apMod.setId(idApoderado);
        apMod.setDni(dniApoderado);
        apMod.setNombres(nombresApoderado);
        apMod.setApellidos(apellidosApoderado);
        apMod.setParentesco(parentesco);
        apMod.actualizarContacto(telefonoApoderado, correoApoderado);
        apMod.setFechaNacimiento(nacApod);
        apMod.setActivo(apoderadoActivo); 

        Alumno alumMod = new Alumno();
        alumMod.setId(idAlumno);
        alumMod.setCodigoEstudiante(codigoEstudiante);
        alumMod.setDni(dniAlumno);
        alumMod.setNombres(nombresAlumno);
        alumMod.setApellidos(apellidosAlumno);
        alumMod.setFechaNacimiento(nacAlum);
        alumMod.setActivo(alumnoActivo); 
        alumMod.setApoderado(apMod);

        return modificarAlumnoYApoderado(alumMod);
    }
}
