/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package docentes.model;
import java.time.LocalDate;
import java.util.Objects;
import java.util.regex.Pattern;
import shared.Persona;

/**
 *
 * @author Alexis
 */


public class Docente extends Persona {

    private String codigoDocente;
    private String tituloProfesional;
    private String especialidadAcademica;
    private String correo;
    private String telefono;
    
    private static final Pattern PATRON_CORREO = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public Docente() {
    }

    public Docente(String codigoDocente, String tituloProfesional, String especialidadAcademica, String correo, String telefono, Integer id, String dni, String nombres, String apellidos, LocalDate fechaNacimiento, boolean activo) {
        super(id, dni, nombres, apellidos, fechaNacimiento, activo);
        this.codigoDocente = validarCadena(codigoDocente, "El código de docente");
        this.tituloProfesional = validarCadena(tituloProfesional, "El título profesional");
        this.especialidadAcademica = validarCadena(especialidadAcademica, "La especialidad académica");
        this.telefono = validarTelefonoFormato(telefono);
        this.correo = validarCorreoFormato(correo);
        validarContactoObligatorio(this.telefono, this.correo);
    }

    private String validarCadena(String valor, String campo) {
        String validado = Objects.requireNonNull(valor, campo + " no puede ser nulo").trim();
        if (validado.isEmpty()) {
            throw new IllegalArgumentException(campo + " no puede estar vacío.");
        }
        return validado;
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
            throw new IllegalStateException("El docente debe tener obligatoriamente al menos un teléfono o un correo de contacto.");
        }
    }
    
    public void actualizarContacto(String telefono, String correo) {
        String telValidado = validarTelefonoFormato(telefono);
        String corValidado = validarCorreoFormato(correo);
        validarContactoObligatorio(telValidado, corValidado);
        this.telefono = telValidado;
        this.correo = corValidado;
    }

    public void generarCodigoDocente(int correlativo) {
        if (correlativo <= 0) {
            throw new IllegalArgumentException("El correlativo debe ser un número positivo.");
        }
        int anioActual = LocalDate.now().getYear();
        this.codigoDocente = "DOC-" + anioActual + "-" + String.format("%04d", correlativo);
    }

    public String getCodigoDocente() {
        return codigoDocente;
    }

    public String getTituloProfesional() {
        return tituloProfesional;
    }

    public String getEspecialidadAcademica() {
        return especialidadAcademica;
    }

    public String getCorreo() {
        return correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setCodigoDocente(String codigoDocente) {
        this.codigoDocente = validarCadena(codigoDocente, "El código de docente");
    }

    public void setTituloProfesional(String tituloProfesional) {
        this.tituloProfesional = validarCadena(tituloProfesional, "El título");
    }

    public void setEspecialidadAcademica(String especialidadAcademica) {
        this.especialidadAcademica = validarCadena(especialidadAcademica, "La especialidad");
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
        return getCodigoDocente();
    }
}