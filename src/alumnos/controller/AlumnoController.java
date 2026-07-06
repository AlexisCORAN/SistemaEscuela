/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package alumnos.controller;
import java.util.List;
import alumnos.model.Alumno;
import alumnos.service.AlumnoService;
/**
 *
 * @author Alexis
 */
public class AlumnoController {

    private final AlumnoService alumnoService;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AlumnoController.class.getName());

    public AlumnoController() {
        this.alumnoService = new AlumnoService();
    }

    public List<Alumno> obtenerAlumnos() {
        return alumnoService.obtenerAlumnos();
    }

    public List<Alumno> obtenerAlumnosPorEstado(String filtroEstado) {
        return alumnoService.obtenerAlumnosPorEstado(filtroEstado);
    }

    public String registrarAlumno(Alumno alumno) {
        try {
            alumnoService.registrarAlumno(alumno);
            return null;
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        } catch (RuntimeException e) {
            return "Ocurrió un error en la base de datos: " + e.getMessage();
        }
    }

    public String actualizarAlumno(Alumno alumno) {
         try {
            alumnoService.actualizarAlumno(alumno);
            return null;
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        } catch (RuntimeException e) {
            return "Ocurrió un error en la base de datos: " + e.getMessage();
        }
    }

    public String procesarBajaPorCodigo(String codigo) {
        try {
            alumnoService.procesarBajaPorCodigo(codigo);
            return null; 
        } catch (IllegalArgumentException e) {
            return e.getMessage(); 
        } catch (RuntimeException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error al dar de baja al alumno con código " + codigo, e);
            return "Error del sistema: " + e.getMessage();
        }
    }

    public String procesarReactivacionPorCodigo(String codigo) {
        try {
            alumnoService.procesarReactivacionPorCodigo(codigo);
            return null; 
        } catch (IllegalArgumentException e) {
            return e.getMessage(); 
        } catch (RuntimeException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error al reactivar al alumno con código " + codigo, e);
            return "Error del sistema: " + e.getMessage();
        }
    }
    
    public Alumno obtenerAlumnoParaEdicion(String codigoEstudiante) {
        Alumno original = alumnoService.buscarAlumnoPorCodigoExacto(codigoEstudiante);
        return crearCopiaAlumno(original);
    }
    
    private Alumno crearCopiaAlumno(Alumno original) {
        return (original != null) ? new Alumno(original) : null;
    }
    
    public List<Alumno> buscarAlumnosPorCodigoBusqueda(String codigo) {
        Alumno alumno = alumnoService.buscarAlumnoPorCodigoExacto(codigo);
        if (alumno != null) {
            return java.util.Collections.singletonList(alumno);
        }
        return java.util.Collections.emptyList();
    }
}