/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package notas.model;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import matricula.model.MatriculaCurso;
import shared.Bimestre;
import shared.TipoEvaluacion;
/**
 *
 * @author Alexis
 */
public class RegistroBimestral {

    private Integer id;
    private MatriculaCurso matriculaCurso;
    private Bimestre bimestre;
    private boolean activo; 
    private LocalDateTime fechaCierre;
    private List<Evaluacion> evaluaciones;

    public RegistroBimestral() {
        this.evaluaciones = new ArrayList<>();
        this.activo = true; 
    }

    public RegistroBimestral(Integer id, MatriculaCurso matriculaCurso, Bimestre bimestre, boolean activo, LocalDateTime fechaCierre, List<Evaluacion> evaluaciones) {
        this.id = validarIdRegistro(id, null);
        this.matriculaCurso = matriculaCurso;
        this.bimestre = bimestre;
        this.activo = activo;
        this.fechaCierre = fechaCierre;
        this.evaluaciones = (evaluaciones != null) ? evaluaciones : new ArrayList<>();
    }

    private Integer validarIdRegistro(Integer nuevoId, Integer idActual) {
        if (nuevoId != null && nuevoId <= 0) {
            throw new IllegalArgumentException("El ID del registro debe ser positivo.");
        }
        if (idActual != null && !idActual.equals(nuevoId)) {
            throw new IllegalStateException("El ID del registro ya fue asignado.");
        }
        return nuevoId;
    }

    private void validarEstadoAbierto() {
        if (!this.activo) {
            throw new IllegalStateException("No se pueden modificar notas ni evaluaciones en un bimestre que ya se encuentra CERRADO.");
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = validarIdRegistro(id, this.id);
    }

    public MatriculaCurso getMatriculaCurso() {
        return matriculaCurso;
    }

    public void setMatriculaCurso(MatriculaCurso matriculaCurso) {
        this.matriculaCurso = matriculaCurso;
    }

    public Bimestre getBimestre() {
        return bimestre;
    }

    public void setBimestre(Bimestre bimestre) {
        this.bimestre = bimestre;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaCierre() {
        return fechaCierre;
    }

    public void cerrarRegistro() {
        this.activo = false;
        this.fechaCierre = LocalDateTime.now();
    }

    public List<Evaluacion> getEvaluaciones() {
        return evaluaciones;
    }

    public void setEvaluaciones(List<Evaluacion> evaluaciones) {
        validarEstadoAbierto();
        this.evaluaciones = (evaluaciones != null) ? evaluaciones : new ArrayList<>();
    }

    public void actualizarNotaPorTipo(TipoEvaluacion tipo, Double nuevaNota) {
        validarEstadoAbierto();
        
        for (Evaluacion eval : evaluaciones) {
            if (eval.getTipo() == tipo) {
                eval.setNota(nuevaNota);
                return;
            }
        }
    }

    public double calcularPromedio() {
        double total = 0.0;
        for (Evaluacion e : evaluaciones) {
            if (e.getNota() != null && e.getTipo() != null) {
                total += e.getNota() * e.getTipo().getPeso();
            }
        }
        return Math.round(total * 100.0) / 100.0;
    }

    public static double calcularPromedioEstatico(double practica, double tarea, double parcial, double bimestral) {
        double total = (practica * TipoEvaluacion.PRACTICA.getPeso()) + 
                       (tarea * TipoEvaluacion.TAREA.getPeso()) + 
                       (parcial * TipoEvaluacion.PARCIAL.getPeso()) + 
                       (bimestral * TipoEvaluacion.BIMESTRAL.getPeso());
        return Math.round(total * 100.0) / 100.0;
    }
    
    public void validarNuevaEvaluacion(TipoEvaluacion tipo) {
        long contador = this.evaluaciones.stream().filter(e -> e.getTipo() == tipo).count();
        
        if ((tipo == TipoEvaluacion.PARCIAL || tipo == TipoEvaluacion.BIMESTRAL) && contador >= 1) {
            throw new IllegalStateException("Error: Solo se permite registrar un " + tipo.name() + " por bimestre.");
        }
        
        if (tipo == TipoEvaluacion.PRACTICA && contador >= 4) {
            throw new IllegalStateException("Error: El límite máximo es de 4 Prácticas Calificadas por bimestre.");
        }
        
        if (tipo == TipoEvaluacion.TAREA && contador >= 4) {
            throw new IllegalStateException("Error: El límite máximo es de 4 Tareas Académicas por bimestre.");
        }
    }
}