/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package matricula.model;
import plan_estudios.model.Curso;
import java.util.Objects;

/**
 *
 * @author Alexis
 */
public class MatriculaCurso {

    private Integer id;
    private Curso curso;
    private double notaFinal;

    public MatriculaCurso() {
    }

    public MatriculaCurso(Integer id, Curso curso, double notaFinal) {
        this.id = id;
        this.curso = Objects.requireNonNull(curso, "El curso no puede ser nulo");
        this.notaFinal = notaFinal;
    }

    public MatriculaCurso(Curso curso) {
        this.curso = Objects.requireNonNull(curso, "El curso no puede ser nulo");
        this.notaFinal = 0.0;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = Objects.requireNonNull(curso, "El curso no puede ser nulo");
    }

    public double getNotaFinal() {
        return notaFinal;
    }

    public void actualizarNotaFinal(double notaFinal) {
        if (notaFinal < 0.0 || notaFinal > 20.0) {
            throw new IllegalArgumentException("La nota final debe estar entre 0.0 y 20.0");
        }
        this.notaFinal = notaFinal;
    }

    public boolean estaAprobado() {
        return this.notaFinal >= 12; 
    }
}
