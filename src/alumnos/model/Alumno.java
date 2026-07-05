/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package alumnos.model;

import java.time.LocalDate;
import java.util.Objects;
import shared.Persona;

/**
 *
 * @author Alexis
 */
public class Alumno extends Persona {
    
    private String codigoEstudiante;
    private Apoderado apoderado;

    public Alumno() {
    }

    public Alumno(String codigoEstudiante, Apoderado apoderado, Integer id, String dni, String nombres, String apellidos, LocalDate fechaNacimiento, boolean activo) {
        super(id, dni, nombres, apellidos, fechaNacimiento, activo);
        
        validarRangoEdadEscolar();
        this.codigoEstudiante = validarCodigoEstudiante(codigoEstudiante);
        this.apoderado = validarApoderadoObligatorio(apoderado);
    }
    
    

    private String validarCodigoEstudiante(String codigo) {
        String codigoValidado = Objects.requireNonNull(codigo, "El código de estudiante no puede ser nulo").trim();
        if (codigoValidado.isEmpty()) {
            throw new IllegalArgumentException("El código de estudiante no puede estar vacío.");
        }
        return codigoValidado;
    }

    private Apoderado validarApoderadoObligatorio(Apoderado apoderado) {
        if (apoderado == null) {
            throw new IllegalStateException("Todo alumno en etapa escolar debe tener un apoderado asignado.");
        }
        return apoderado;
    }

    private void validarRangoEdadEscolar() {
        int edad = getEdad();
        if (edad < 5 || edad >= 18) {
            throw new IllegalArgumentException("El alumno debe tener entre 5 y 17 años para ser matriculado en la institución.");
        }
    }
    
    public void generarCodigoEstudiante(int correlativo) {
        if (correlativo <= 0) {
            throw new IllegalArgumentException("El correlativo debe ser un número positivo.");
        }
        int anioActual = LocalDate.now().getYear();
        this.codigoEstudiante = validarCodigoEstudiante("EST-" + anioActual + "-" + String.format("%04d", correlativo));
    }
    
    public String getCodigoEstudiante() {
        return codigoEstudiante;
    }
    
    public Apoderado getApoderado() {
        return apoderado;
    }

    public void setCodigoEstudiante(String codigoEstudiante) {
        this.codigoEstudiante = validarCodigoEstudiante(codigoEstudiante);
    }

    public void setApoderado(Apoderado apoderado) {
        this.apoderado = validarApoderadoObligatorio(apoderado);
    }

    @Override
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        super.setFechaNacimiento(fechaNacimiento);
        validarRangoEdadEscolar(); 
    }

    @Override
    public String obtenerIdentificadorPrincipal() {
        return getCodigoEstudiante();
    }
}