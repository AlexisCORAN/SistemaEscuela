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
    private RegistroBimestral registroBimestral;
    private String nombre;
    private TipoEvaluacion tipo;
    private Double nota;
    private Double peso;

    public Evaluacion() {
    }

    public Evaluacion(Integer id, RegistroBimestral registroBimestral, String nombre, TipoEvaluacion tipo, Double nota, Double peso) {
        this.id = validarIdEvaluacion(id, null); 
        this.registroBimestral = registroBimestral;
        this.nombre = validarNombre(nombre);
        this.tipo = validarTipo(tipo);
        this.nota = validarNota(nota);
        this.peso = validarPeso(peso);
    }

    private Integer validarIdEvaluacion(Integer nuevoId, Integer idActual) {
        if (nuevoId != null && nuevoId <= 0) {
            throw new IllegalArgumentException("El ID de la evaluación debe ser positivo.");
        }
        if (idActual != null && !idActual.equals(nuevoId)) {
            throw new IllegalStateException("El ID de la evaluación ya fue asignado y no puede ser modificado.");
        }
        return nuevoId;
    }

    private String validarNombre(String nombre) {
        String validado = Objects.requireNonNull(nombre, "El nombre de la evaluación no puede ser nulo.").trim();
        if (validado.isEmpty()) {
            throw new IllegalArgumentException("El nombre de la evaluación no puede estar vacío.");
        }
        return validado;
    }

    private TipoEvaluacion validarTipo(TipoEvaluacion tipo) {
        return Objects.requireNonNull(tipo, "El tipo de evaluación es obligatorio.");
    }

    private Double validarNota(Double nota) {
        if (nota == null) {
            throw new IllegalArgumentException("La nota no puede ser nula.");
        }
        if (nota < 0.0 || nota > 20.0) {
            throw new IllegalArgumentException("La nota debe estar estrictamente en el rango de 0.0 a 20.0. Valor inválido: " + nota);
        }
        return Math.round(nota * 100.0) / 100.0;
    }

    private Double validarPeso(Double peso) {
        if (peso == null || peso <= 0.0 || peso > 1.0) {
            throw new IllegalArgumentException("El peso debe ser un valor decimal mayor a 0 y menor o igual a 1.0.");
        }
        return peso;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = validarIdEvaluacion(id, this.id);
    }

    public RegistroBimestral getRegistroBimestral() {
        return registroBimestral;
    }

    public void setRegistroBimestral(RegistroBimestral registroBimestral) {
        this.registroBimestral = registroBimestral;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = validarNombre(nombre);
    }

    public TipoEvaluacion getTipo() {
        return tipo;
    }

    public void setTipo(TipoEvaluacion tipo) {
        this.tipo = validarTipo(tipo);
    }

    public Double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = validarNota(nota);
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = validarPeso(peso);
    }
}