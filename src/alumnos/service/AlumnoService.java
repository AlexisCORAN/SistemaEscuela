/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package alumnos.service;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import alumnos.dao.AlumnoDAOImpl;
import alumnos.dao.ApoderadoDAOImpl;
import alumnos.dao.IAlumnoDAO;
import alumnos.dao.IApoderadoDAO;
import alumnos.model.Alumno;
import alumnos.model.Apoderado;
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

    public List<Alumno> buscarPorCodigo(String criterio) {
        if (criterio == null || criterio.trim().isEmpty() || criterio.equals("Buscar por Código")) {
            return obtenerAlumnos();
        }
        try (Connection conn = ConexionDB.getInstance().getConexion()) {
            IAlumnoDAO alumnoDAO = new AlumnoDAOImpl(conn);
            Alumno resultado = alumnoDAO.buscarPorCodigo(criterio.trim());
            return resultado != null ? List.of(resultado) : Collections.emptyList();
        } catch (Exception e) {
            System.err.println("Error al buscar alumno: " + e.getMessage());
            return Collections.emptyList();
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

    public void registrarAlumnoDesdeFormulario(
        Date fechaNacAlumno, String dniAlumno, String nombresAlumno, String apellidosAlumno,
        Date fechaNacApoderado, String dniApoderado, String nombresApoderado, String apellidosApoderado,
        String parentesco, String telefonoApoderado, String correoApoderado) 
        {
            validarExistenciaDnis(dniAlumno, dniApoderado, null, null);
            LocalDate nacAlum = fechaNacAlumno.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate nacApod = fechaNacApoderado.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            Apoderado apoderado = new Apoderado(parentesco, telefonoApoderado, correoApoderado, 
                                                null, dniApoderado, nombresApoderado, apellidosApoderado, nacApod, true);

            Alumno alumno = new Alumno("TEMP", apoderado, null, dniAlumno, nombresAlumno, apellidosAlumno, nacAlum, true);

            TransactionRunner.ejecutar(conn -> {
            IApoderadoDAO apoderadoDAO = new ApoderadoDAOImpl(conn);
            IAlumnoDAO alumnoDAO = new AlumnoDAOImpl(conn);

            if (!apoderadoDAO.insertar(apoderado)) {
                throw new RuntimeException("Error al registrar el apoderado en la base de datos.");
            }

            generarYAsignarCodigo(alumno, alumnoDAO);

            if (!alumnoDAO.insertar(alumno)) {
                throw new RuntimeException("Error al registrar el alumno en la base de datos.");
            }
            return null; 
        }, null);
    }

    public void modificarAlumnoDesdeFormulario(
        Integer idAlumno, String codigoEstudiante, boolean alumnoActivo,
        Date fechaNacAlumno, String dniAlumno, String nombresAlumno, String apellidosAlumno,
        Integer idApoderado, boolean apoderadoActivo,
        Date fechaNacApoderado, String dniApoderado, String nombresApoderado, String apellidosApoderado,
        String parentesco, String telefonoApoderado, String correoApoderado) 
        {
            validarExistenciaDnis(dniAlumno, dniApoderado, idAlumno, idApoderado);
            
            LocalDate nacAlum = fechaNacAlumno.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate nacApod = fechaNacApoderado.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            Apoderado apMod = new Apoderado(parentesco, telefonoApoderado, correoApoderado, 
                                            idApoderado, dniApoderado, nombresApoderado, apellidosApoderado, 
                                            nacApod, apoderadoActivo);

            Alumno alumMod = new Alumno(codigoEstudiante, apMod, idAlumno, dniAlumno, 
                                        nombresAlumno, apellidosAlumno, nacAlum, alumnoActivo);

            TransactionRunner.ejecutar(conn -> {
            IApoderadoDAO apoderadoDAO = new ApoderadoDAOImpl(conn);
            IAlumnoDAO alumnoDAO = new AlumnoDAOImpl(conn);

            if (!apoderadoDAO.actualizar(alumMod.getApoderado())) {
                throw new RuntimeException("Error al actualizar los datos del apoderado.");
            }
            if (!alumnoDAO.actualizar(alumMod)) {
                throw new RuntimeException("Error al actualizar los datos del alumno.");
            }
            return null;
        }, null);
    }

    public boolean procesarBajaAlumno(Alumno alumno) {
        if (alumno == null) return false;
        try (Connection conn = ConexionDB.getInstance().getConexion()) {
            IAlumnoDAO alumnoDAO = new AlumnoDAOImpl(conn);
            alumno.setActivo(false);
            return alumnoDAO.actualizar(alumno);
        } catch (Exception e) {
            System.err.println("Error al procesar baja: " + e.getMessage());
            return false;
        }
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


}