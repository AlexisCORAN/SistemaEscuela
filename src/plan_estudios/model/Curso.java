/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package plan_estudios.model;
import java.util.Objects;
import docentes.model.Docente;
import java.time.LocalDate;

/**
 *
 * @author Alexis
 */

public class Curso {

    private Integer id;
    private String codigo;
    private String nombre;
    private int horasSemanales;
    private Docente docente;
    private Grado gradoAsignado;
    private boolean activo;

    public Curso() {
    }

    public Curso(Integer id, String codigo, String nombre, int horasSemanales, Docente docente, Grado gradoAsignado, boolean activo) {
        this.id = id;
        this.codigo = validarTextoRequerido(codigo, "El código del curso");
        this.nombre = validarTextoRequerido(nombre, "El nombre del curso");
        validarHoras(horasSemanales);
        this.horasSemanales = horasSemanales;
        this.docente = docente;
        this.gradoAsignado = gradoAsignado;
        this.activo = activo;
    }
    
    public Curso(Curso otroCurso) {
    this.id = otroCurso.getId();
    this.codigo = otroCurso.getCodigo();
    this.nombre = otroCurso.getNombre();
    this.horasSemanales = otroCurso.getHorasSemanales();
    this.docente = otroCurso.getDocente();
    this.gradoAsignado = otroCurso.getGradoAsignado();
    this.activo = otroCurso.isActivo();
}

    private String validarTextoRequerido(String texto, String nombreCampo) {
        String textoValidado = Objects.requireNonNull(texto, nombreCampo + " no pueden ser nulo").trim();
        if (textoValidado.isEmpty()) {
            throw new IllegalArgumentException(nombreCampo + " no pueden estar vacío.");
        }
        return textoValidado;
    }

    private void validarHoras(int horasSemanales) {
        if (horasSemanales < 1) {
            throw new IllegalArgumentException("Las horas semanales deben ser mayores a cero.");
        }
    }

    public void generarCodigoCurso(int correlativo) {
        if (correlativo <= 0) {
            throw new IllegalArgumentException("El correlativo debe ser un número positivo.");
        }
        int anioActual = LocalDate.now().getYear();
        this.codigo = "CUR-" + anioActual + "-" + String.format("%04d", correlativo);
    }

    public Integer getId() {
            return id;
    }
    public String getCodigo() {
        return codigo;
    }
    public String getNombre() {
        return nombre;
    }
    public int getHorasSemanales() {
        return horasSemanales;
    }
    public Docente getDocente() {
        return docente;
    }
    public Grado getGradoAsignado() {
        return gradoAsignado;
    }
    public boolean isActivo() {
        return activo;
    }

    public void setId(Integer id) {
        if (this.id != null) {
            throw new IllegalStateException("El id del curso ya fue asignado, no se puede reasignar.");
        }
        this.id = id;
    }

    public void setCodigo(String codigo) {
         this.codigo = validarTextoRequerido(codigo, "El código del curso");
    }

    public void setNombre(String nombre) {
        this.nombre = validarTextoRequerido(nombre, "El nombre del curso");
    }

    public void setHorasSemanales(int horasSemanales) {
        validarHoras(horasSemanales);
        this.horasSemanales = horasSemanales;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    public void asociarGrado(Grado gradoAsignado) {
        this.gradoAsignado = gradoAsignado;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    public Integer getIdGrado() {
        return (this.gradoAsignado != null) ? this.gradoAsignado.getId() : null;
    }
    
}
