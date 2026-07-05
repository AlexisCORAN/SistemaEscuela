/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package notas.model;
import shared.TipoEvaluacion;
import java.util.Objects;
/**
 *
 * @author Alexis
 */
public class Evaluacion {

    private Integer id; 
    private String nombre;
    private TipoEvaluacion tipo;
    private double peso;
    private double nota;

    public Evaluacion() {
    }

    public Evaluacion(Integer id, String nombre, TipoEvaluacion tipo, double peso, double nota) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.peso = peso;
        this.nota = nota;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        if (this.id != null) {
            throw new IllegalStateException("El id de la evaluación ya fue asignado.");
        }
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = Objects.requireNonNull(nombre, "El nombre no puede ser nulo");
    }

    public TipoEvaluacion getTipo() {
        return tipo;
    }

    public void setTipo(TipoEvaluacion tipo) {
        this.tipo = Objects.requireNonNull(tipo, "El tipo no puede ser nulo");
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        if (peso < 0.0 || peso > 1.0) {
            throw new IllegalArgumentException("El peso de la evaluación debe estar entre 0.0 y 1.0");
        }
        this.peso = peso;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        if (nota < 0.0 || nota > 20.0) {
            throw new IllegalArgumentException("La nota en el sistema vigesimal debe estar entre 0.0 y 20.0");
        }
        this.nota = nota;
    }

    public boolean isAprobado() {
        return this.nota >= 12.0;
    }
}