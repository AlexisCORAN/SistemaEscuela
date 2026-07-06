/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package plan_estudios.model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Alexis
 */
public class Grado {

    private Integer id; 
    private String nombre;
    private String nivel;
    private List<Curso> cursos;
    private boolean activo; 

    public Grado() {
        this.cursos = new ArrayList<>();
    }
    
    public Grado(Integer id, String nombre, String nivel, boolean activo) {
        this.id = id;
        this.nombre = validarCadena(nombre, "El nombre del grado");
        this.nivel = validarCadena(nivel, "El nivel del grado");
        this.activo = activo;
        this.cursos = new ArrayList<>();
    }
    
    public Grado(Grado otro) {
        this.id = otro.getId();
        this.nombre = otro.getNombre();
        this.nivel = otro.getNivel();
        this.activo = otro.isActivo();
    }

    private String validarCadena(String valor, String campo) {
        String validado = Objects.requireNonNull(valor, campo + " no puede ser nulo").trim();
        if (validado.isEmpty()) {
            throw new IllegalArgumentException(campo + " no puede estar vacío.");
        }
        return validado;
    }

    public Integer getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }
    public String getNivel() {
        return nivel;
    }
    public List<Curso> getCursos() {
        return Collections.unmodifiableList(cursos);
    }
    public boolean isActivo() {
            return activo;
    }

    public void setId(Integer id) {
        if (this.id != null) throw new IllegalStateException("El id del grado ya fue asignado.");
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = validarCadena(nombre, "El nombre");
    }
    
    public void setNivel(String nivel) {
        this.nivel = validarCadena(nivel, "El nivel");
    }
    
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public void agregarCurso(Curso curso) {
        Objects.requireNonNull(curso, "El curso no puede ser nulo");
        if (!this.cursos.contains(curso)) {
            this.cursos.add(curso);
            curso.asociarGrado(this);
        }
    }

    public void removerCurso(Curso curso) {
        if (this.cursos.remove(curso)){
            curso.asociarGrado(null); 
        }
    }

}