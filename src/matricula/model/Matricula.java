/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package matricula.model;
import plan_estudios.model.Grado;
import alumnos.model.Alumno;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Alexis
 */
public class Matricula {

    private Integer id; 
    private String codigoMatricula;
    private Alumno alumno;
    private LocalDate fechaMatricula;
    private int añoEscolar;
    private boolean activo;
    private List<MatriculaCurso> cursosMatriculados;
    private Grado grado;

    public Matricula() {
        this.cursosMatriculados = new ArrayList<>();
    }

    public Matricula(Integer id, String codigoMatricula, Alumno alumno, Grado grado, LocalDate fechaMatricula, int añoEscolar, boolean activo) {
        this.id = id;
        this.codigoMatricula = codigoMatricula;
        this.alumno = alumno;
        this.grado = grado;
        this.fechaMatricula = fechaMatricula;
        this.añoEscolar = añoEscolar; 
        this.activo = activo;
        this.cursosMatriculados = new ArrayList<>();
        validarAñoEscolar(añoEscolar, fechaMatricula);
    }

    private static void validarAñoEscolar(int añoEscolar, LocalDate fechaMatricula) {
        if (añoEscolar <= 0) {
            throw new IllegalArgumentException("El año escolar debe ser un valor positivo.");
        }
        int añoFecha = fechaMatricula.getYear();
        if (Math.abs(añoEscolar - añoFecha) > 1) {
            throw new IllegalArgumentException(
                "El año escolar (" + añoEscolar + ") es inconsistente con la fecha de matrícula (" + fechaMatricula + ")."
            );
        }
    }

    public Integer getId() {
        return id;
    }

    public String getCodigoMatricula() {
        return codigoMatricula;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public LocalDate getFechaMatricula() {
        return fechaMatricula;
    }

    public int getAñoEscolar() {
        return añoEscolar;
    }

    public boolean getActivo() {
        return activo;
    }

    public List<MatriculaCurso> getCursosMatriculados() {
         return Collections.unmodifiableList(cursosMatriculados);
    }

    public Grado getGrado() {
        return grado;
    }

    public void setId(Integer id) {
         if (this.id != null) {
            throw new IllegalStateException("El id de la matrícula ya fue asignado, no se puede reasignar.");
        }
        this.id = id;

    }

    public void setCodigoMatricula(String codigoMatricula) {
        this.codigoMatricula = Objects.requireNonNull(codigoMatricula, "El código de matrícula no puede ser nulo");
    }

    public void setAlumno(Alumno alumno) {
         this.alumno = Objects.requireNonNull(alumno, "El alumno no puede ser nulo");
    }

    public void setFechaMatricula(LocalDate fechaMatricula) {
        this.fechaMatricula = Objects.requireNonNull(fechaMatricula, "La fecha de matrícula no puede ser nula");
    }

    public void setAñoEscolar(int añoEscolar) {
        this.añoEscolar = añoEscolar;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public void setGrado(Grado grado) {
        this.grado = grado;
    }
    
    public void agregarCurso(MatriculaCurso cursoMatricula) {
        if (!activo) {
            throw new IllegalStateException("No se puede agregar un curso a una matrícula anulada.");
        }
        this.cursosMatriculados.add(Objects.requireNonNull(cursoMatricula, "El curso de matrícula no puede ser nulo"));
    }
    
    public void anular() {
        this.activo = false;
    }

    
    public void asignarGrado(Grado grado) {
        this.grado = Objects.requireNonNull(grado, "El grado a asignar no puede ser nulo");
    }
    
    public void generarCodigoMatricula(int correlativo) {
        this.codigoMatricula = "MAT-" + this.añoEscolar + "-" + String.format("%04d", correlativo);
    }
}
