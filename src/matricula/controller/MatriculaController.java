/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package matricula.controller;
import alumnos.model.Alumno;
import java.util.List;
import matricula.model.Matricula;
import matricula.service.MatriculaService;
import plan_estudios.model.Grado;
import plan_estudios.service.PlanEstudiosService;
/**
 *
 * @author Alexis
 */

public class MatriculaController {

    private final MatriculaService matriculaService;
    private static final String TEXTO_BUSQUEDA_DEFECTO = "Buscar por DNI, Codigo o Nombre del Alumno";
    private final PlanEstudiosService planEstudiosService;

    public MatriculaController() {
        this.matriculaService = new MatriculaService();
        this.planEstudiosService = new PlanEstudiosService();
    }
    
    public List<Matricula> obtenerMatriculas() {
        return matriculaService.obtenerMatriculas();
    }

    public List<Matricula> buscarMatriculas(final String criterio) {
        if (criterio == null || criterio.trim().isEmpty() || criterio.trim().equals(TEXTO_BUSQUEDA_DEFECTO)) {
            return matriculaService.obtenerMatriculas();
        }
        return matriculaService.buscarMatriculas(criterio.trim());
    }

    public Alumno buscarAlumnoParaMatricula(final String criterio) {
        if (criterio == null || criterio.trim().isEmpty() || criterio.trim().equals(TEXTO_BUSQUEDA_DEFECTO)) {
            return null;
        }
        try {
            return matriculaService.buscarAlumnoParaMatricula(criterio.trim());
        } catch (IllegalStateException e) {
            throw e;
        }
    }

    public List<Grado> obtenerGradosConCursos() {
        return planEstudiosService.obtenerGradosConCursos();
    }
    public String registrarMatricula(final Matricula m) {
        if (m == null) {
            return "Error: La información de matrícula no puede ser nula.";
        }
        if (m.getAlumno() == null) {
            return "Error: Debe seleccionar un alumno válido.";
        }
        if (m.getGrado() == null) {
            return "Error: Debe asignar un grado académico.";
        }
        
        try {
            matriculaService.registrarMatricula(m);
            return null;
        } catch (IllegalArgumentException | IllegalStateException e) {
            return e.getMessage(); 
        } catch (RuntimeException e) {
            return "Ocurrió un error en el sistema: " + e.getMessage();
        }
    }

    public String anularMatricula(final Matricula matricula) {
        if (matricula == null || matricula.getId() == null) {
            return "Error: La matrícula seleccionada no es válida para su anulación.";
        }
        try {
            matriculaService.anularMatricula(matricula.getId());
            return null; 
        } catch (IllegalArgumentException | IllegalStateException e) {
            return e.getMessage();
        } catch (RuntimeException e) {
            return "Error del sistema al anular: " + e.getMessage();
        }
    }

    
    public Matricula obtenerMatriculaParaEdicion(Integer idMatricula) {
        Matricula original = matriculaService.obtenerMatriculaPorId(idMatricula);
        return crearCopiaMatricula(original);
    }
    
    private Matricula crearCopiaMatricula(Matricula original) {
        return (original != null) ? new Matricula(original) : null;
    }
    
    public List<Matricula> obtenerMatriculasPorEstado(String filtroEstado) {
    try {
        return matriculaService.obtenerMatriculasPorEstado(filtroEstado);
    } catch (RuntimeException e) {
        return java.util.Collections.emptyList();
    }
}
}