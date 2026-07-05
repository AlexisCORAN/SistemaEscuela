/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package notas.model;
import shared.EstadoAcademico;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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
    private List<Evaluacion> evaluaciones;
    private boolean activo;

    public RegistroBimestral() {
        this.evaluaciones = new ArrayList<>();
    }

    public RegistroBimestral(Integer id, MatriculaCurso matriculaCurso, Bimestre bimestre, boolean activo) {
        this.id = id;
        this.matriculaCurso = matriculaCurso;
        this.bimestre = bimestre;
        this.activo = activo;
        this.evaluaciones = new ArrayList<>();
    }


    public void actualizarNotaPorTipo(TipoEvaluacion tipo, double nuevaNota) {
        for (Evaluacion e : evaluaciones) {
            if (e.getTipo() == tipo) {
                e.setNota(nuevaNota);
                return;
            }
        }
        String nombrePorTipo = tipo.toString().substring(0, 1) + tipo.toString().substring(1).toLowerCase();
        double pesoDefecto = (tipo == TipoEvaluacion.PARCIAL || tipo == TipoEvaluacion.BIMESTRAL) ? 0.3 : 0.2;
        this.agregarEvaluacion(new Evaluacion(null, nombrePorTipo, tipo, pesoDefecto, nuevaNota));
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        if (this.id != null) {
            throw new IllegalStateException("El id del registro ya fue asignado.");
        }
        this.id = id;
    }

    public MatriculaCurso getMatriculaCurso() {
        return matriculaCurso;
    }

    public void setMatriculaCurso(MatriculaCurso matriculaCurso) {
        this.matriculaCurso = Objects.requireNonNull(matriculaCurso, "La matrícula de curso no puede ser nula");
    }

    public Bimestre getBimestre() {
        return bimestre;
    }

    public void setBimestre(Bimestre bimestre) {
        this.bimestre = bimestre;
    }

    public List<Evaluacion> getEvaluaciones() {
        return Collections.unmodifiableList(evaluaciones);
    }

    public void setEvaluaciones(List<Evaluacion> evaluaciones) {
        this.evaluaciones = new ArrayList<>(Objects.requireNonNull(evaluaciones));
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public void agregarEvaluacion(Evaluacion evaluacion) {
        this.evaluaciones.add(Objects.requireNonNull(evaluacion, "La evaluación no puede ser nula"));
    }

    public double calcularPromedio() {
        if (evaluaciones.isEmpty()) {
            return 0.0;
        }

        double sumaPonderada = 0.0;
        double sumaPesos = 0.0;
        for (Evaluacion e : evaluaciones) {
            sumaPonderada += e.getNota() * e.getPeso();
            sumaPesos += e.getPeso();
        }
        
        if (sumaPesos == 0.0) return 0.0;
        
        return Math.round((sumaPonderada / sumaPesos) * 100.0) / 100.0;
    }

    public EstadoAcademico obtenerEstadoAcademico() {
        double promedio = calcularPromedio();
        if (promedio >= 14.0) {
            return EstadoAcademico.PROMOCIONADO;
        }
        if (promedio >= 11.0) {
            return EstadoAcademico.REGULAR;
        }
        return EstadoAcademico.REQUIERE_REFUERZO;
    }
    
    public boolean estaAprobado() {
        return calcularPromedio() >= 12.0;
    }

    public int cantidadEvaluaciones() {
        return this.evaluaciones.size();
    }
}