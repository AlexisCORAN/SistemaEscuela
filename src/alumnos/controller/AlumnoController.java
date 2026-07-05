/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package alumnos.controller;
import java.util.Date;
import java.util.List;
import alumnos.model.Alumno;
import alumnos.service.AlumnoService;
/**
 *
 * @author Alexis
 */
public class AlumnoController {

    private final AlumnoService alumnoService;

    public AlumnoController() {
        this.alumnoService = new AlumnoService();
    }

    public List<Alumno> obtenerAlumnos() {
        return alumnoService.obtenerAlumnos();
    }

    public List<Alumno> buscarPorCodigo(String criterio) {
        return alumnoService.buscarPorCodigo(criterio);
    }
    
    public List<Alumno> obtenerAlumnosPorEstado(String filtroEstado) {
        return alumnoService.obtenerAlumnosPorEstado(filtroEstado);
    }

    public void registrarAlumnoDesdeFormulario(
        Date fechaNacAlumno, String dniAlumno, String nombresAlumno, String apellidosAlumno,
        Date fechaNacApoderado, String dniApoderado, String nombresApoderado, String apellidosApoderado,
        String parentesco, String telefonoApoderado, String correoApoderado) {
            
            alumnoService.registrarAlumnoDesdeFormulario(
                fechaNacAlumno, dniAlumno, nombresAlumno, apellidosAlumno,
                fechaNacApoderado, dniApoderado, nombresApoderado, apellidosApoderado,
                parentesco, telefonoApoderado, correoApoderado);
    }

    public void modificarAlumnoDesdeFormulario(
        Integer idAlumno, String codigoEstudiante, boolean alumnoActivo,
        Date fechaNacAlumno, String dniAlumno, String nombresAlumno, String apellidosAlumno,
        Integer idApoderado, boolean apoderadoActivo,
        Date fechaNacApoderado, String dniApoderado, String nombresApoderado, String apellidosApoderado,
        String parentesco, String telefonoApoderado, String correoApoderado) {
            alumnoService.modificarAlumnoDesdeFormulario(
                    
                idAlumno, codigoEstudiante, alumnoActivo,
                fechaNacAlumno, dniAlumno, nombresAlumno, apellidosAlumno,
                idApoderado, apoderadoActivo,
                fechaNacApoderado, dniApoderado, nombresApoderado, apellidosApoderado,
                parentesco, telefonoApoderado, correoApoderado);
    }

    public boolean procesarBajaAlumno(Alumno alumno) {
        return alumnoService.procesarBajaAlumno(alumno); 
    }
    
    public Alumno obtenerAlumnoParaEdicion(String codigoEstudiante) {
        List<Alumno> alumnos = buscarPorCodigo(codigoEstudiante);
        if (alumnos != null && !alumnos.isEmpty()) {
            return crearCopiaAlumno(alumnos.get(0));
        }
        return null;
    }
    
    private Alumno crearCopiaAlumno(Alumno original) {
        return (original != null) ? new Alumno(original) : null;
    }
}