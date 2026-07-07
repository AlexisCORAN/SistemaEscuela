/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package alumnos.model;

import java.time.LocalDate;
import java.util.Objects;
import java.util.regex.Pattern;
import shared.Persona;

/**
 *
 * @author Alexis
 */
public class Apoderado extends Persona {
    
    private String parentesco;
    private String telefono;
    private String correo;
    
    private static final Pattern PATRON_CORREO = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public Apoderado() {
    }

    public Apoderado(String parentesco, String telefono, String correo, Integer id, String dni, String nombres, String apellidos, LocalDate fechaNacimiento, boolean activo) {
        super(id, dni, nombres, apellidos, fechaNacimiento, activo);
        this.parentesco = validarParentesco(parentesco);
        this.telefono = validarTelefonoFormato(telefono);
        this.correo = validarCorreoFormato(correo);
        validarContactoObligatorio(validarTelefonoFormato(telefono), validarCorreoFormato(correo));
    }
    
    public Apoderado(Apoderado otroApoderado) {
        super(otroApoderado.getId(), otroApoderado.getDni(), otroApoderado.getNombres(),
              otroApoderado.getApellidos(), otroApoderado.getFechaNacimiento(), otroApoderado.isActivo());
        this.parentesco = otroApoderado.getParentesco();
        this.telefono = otroApoderado.getTelefono();
        this.correo = otroApoderado.getCorreo();
    }

    private String validarParentesco(String parentesco) {
        String valor = Objects.requireNonNull(parentesco, "El parentesco no puede ser nulo").trim();
        if (valor.isEmpty()) {
            throw new IllegalArgumentException("El parentesco no puede estar vacío.");
        }
        return valor;
    }

    private String validarTelefonoFormato(String telefono) {
        if (telefono == null || telefono.isBlank()) {
            return null; 
        }
        String tel = telefono.trim();
        if (tel.length() < 7) {
            throw new IllegalArgumentException("El número de teléfono parece inválido (mínimo 7 dígitos).");
        }
        return tel;
    }

    private String validarCorreoFormato(String correo) {
        if (correo == null || correo.isBlank()) {
            return null; 
        }
        String correoLimpio = correo.trim();
        if (!PATRON_CORREO.matcher(correoLimpio).matches()) {
            throw new IllegalArgumentException("El correo no tiene un formato válido.");
        }
        return correoLimpio;
    }

    private void validarContactoObligatorio(String tel, String cor) {
        if ((tel == null || tel.isBlank()) && (cor == null || cor.isBlank())) {
            throw new IllegalStateException("El apoderado debe tener obligatoriamente al menos un teléfono o un correo de contacto.");
        }
    }
    
    public void actualizarContacto(String telefono, String correo) {
        String telValidado = validarTelefonoFormato(telefono);
        String corValidado = validarCorreoFormato(correo);
        validarContactoObligatorio(telValidado, corValidado);
        this.telefono = telValidado;
        this.correo = corValidado;
    }

    public String getParentesco() {
        return parentesco;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public String getCorreo() {
        return correo;
    }
    
    public String getContactoPrincipal() {
        if (this.correo != null && !this.correo.isBlank()) {
            return this.correo;
        }
        return this.telefono;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = validarParentesco(parentesco);
    }

    public void setTelefono(String telefono) {
        validarContactoObligatorio(validarTelefonoFormato(telefono), this.correo); 
        this.telefono = validarTelefonoFormato(telefono);
    }

    public void setCorreo(String correo) {
        validarContactoObligatorio(this.telefono, validarCorreoFormato(correo));
        this.correo = validarCorreoFormato(correo);
    }

    @Override
    public String obtenerIdentificadorPrincipal() {
        return this.getDni();
    }
}