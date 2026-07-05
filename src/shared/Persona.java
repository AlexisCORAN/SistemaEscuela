/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shared;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

/**
 *
 * @author Alexis
 */

public abstract class Persona {

    private Integer id;
    private String dni;
    private String nombres;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private boolean activo;

    public Persona() {
    }

    public Persona(Integer id, String dni, String nombres, String apellidos, LocalDate fechaNacimiento, boolean activo) {
        this.id = id;
        this.dni = validarDni(dni);
        this.nombres = validarTextoRequerido(nombres, "Los nombres");
        this.apellidos = validarTextoRequerido(apellidos, "Los apellidos");
        this.fechaNacimiento = validarFechaNacimiento(fechaNacimiento);
        this.activo = activo;
    }
    

    private String validarDni(String dni) {
        String dniValidado = Objects.requireNonNull(dni, "El dni no puede ser nulo").trim();
        if (!dniValidado.matches("\\d{8}")) {
            throw new IllegalArgumentException("El DNI debe contener exactamente 8 dígitos.");
        }
        return dniValidado;
    }

    private String validarTextoRequerido(String texto, String nombreCampo) {
        String textoValidado = Objects.requireNonNull(texto, nombreCampo + " no pueden ser nulo").trim();
        if (textoValidado.isEmpty()) {
            throw new IllegalArgumentException(nombreCampo + " no pueden estar vacío.");
        }
        return textoValidado;
    }

    private LocalDate validarFechaNacimiento(LocalDate fecha) {
        LocalDate fechaValidada = Objects.requireNonNull(fecha, "La fecha de nacimiento no puede ser nula");
        if (fechaValidada.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede estar en el futuro.");
        }
        return fechaValidada;
    }

    public Integer getId() {
        return id;
    }

    public String getDni() {
        return dni;
    }

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setId(Integer id) {
    if (id != null && id <= 0) {
        throw new IllegalArgumentException("El ID de la persona debe ser un valor positivo.");
    }
    
    if (this.id != null && !this.id.equals(id)) {
        throw new IllegalStateException("El ID ya fue asignado previamente y no puede ser modificado.");
    }
    
    this.id = id;
}
    
    public boolean estaPersistido() {
        return this.id != null;
    }

    public void setDni(String dni) {
        this.dni = validarDni(dni);
    }

    public void setNombres(String nombres) {
        this.nombres = validarTextoRequerido(nombres, "Los nombres");
    }

    public void setApellidos(String apellidos) {
        this.apellidos = validarTextoRequerido(apellidos, "Los apellidos");
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = validarFechaNacimiento(fechaNacimiento);
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getNombreCompleto() {
        return String.format("%s %s", nombres, apellidos).trim();
    }
    
     public int getEdad() {
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
     }
    
    public boolean esMayorDeEdad() {
        return getEdad() >= 18;
    }

    public abstract String obtenerIdentificadorPrincipal();

}
