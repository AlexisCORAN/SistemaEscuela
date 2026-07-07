/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package plan_estudios.controller;
import java.util.List;
import plan_estudios.model.*;
import plan_estudios.service.PlanEstudiosService;

/**
 *
 * @author Alexis
 */
public class PlanEstudiosController {
    
    private final PlanEstudiosService service;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(PlanEstudiosController.class.getName());

    public PlanEstudiosController() {
        this.service = new PlanEstudiosService();
    }

    public List<Curso> obtenerCursos() {
        return service.obtenerCursos();
    }
    
     public List<Curso> obtenerCursosPorEstado(String filtroEstado) {
        return service.obtenerCursosPorEstado(filtroEstado);
    }

    public String registrarCurso(Curso curso) {
        try {
            service.registrarCurso(curso);
            return null; 
        } catch (IllegalArgumentException e) {
            return e.getMessage(); 
        } catch (RuntimeException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error crítico al registrar curso", e);
            return "Ocurrió un error en la base de datos: " + e.getMessage();
        }
    }

    public String actualizarCurso(Curso curso) {
        try {
            service.actualizarCurso(curso);
            return null; 
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        } catch (RuntimeException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error crítico al actualizar curso", e);
            return "Ocurrió un error en la base de datos: " + e.getMessage();
        }
    }

    public String procesarBajaPorCodigo(String codigo) {
        try {
            service.procesarBajaPorCodigo(codigo);
            return null;
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        } catch (RuntimeException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error al dar de baja el curso: " + codigo, e);
            return "Error del sistema: " + e.getMessage();
        }
    }

    public String procesarReactivacionPorCodigo(String codigo) {
        try {
            service.procesarReactivacionPorCodigo(codigo);
            return null;
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        } catch (RuntimeException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error al reactivar el curso: " + codigo, e);
            return "Error del sistema: " + e.getMessage();
        }
    }

    public List<Grado> obtenerGradosActivos() {
        return service.obtenerGradosActivos();
    }

    public Grado obtenerGradoPorId(Integer id) {
        return service.obtenerGradoPorId(id);
    }
    
    public Curso obtenerCursoParaEdicion(String codigoCurso) {
        Curso original = service.buscarCursoPorCodigoExacto(codigoCurso);
        return crearCopiaCurso(original);
    }
    
    private Curso crearCopiaCurso(Curso original) {
        return (original != null) ? new Curso(original) : null;
    }
    
    public List<Curso> buscarCursosPorCodigoBusqueda(String codigo) {
        Curso planEstudios = service.buscarCursoPorCodigoExacto(codigo);
        if (planEstudios != null) {
            return java.util.Collections.singletonList(planEstudios);
        }
        return java.util.Collections.emptyList();
    }
    
    public List<Grado> obtenerGradosConCursos() {
        try {
            return service.obtenerGradosConCursos();
        } catch (RuntimeException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error al obtener la estructura curricular para matrículas", e);
            return java.util.Collections.emptyList();
        }
    }
}