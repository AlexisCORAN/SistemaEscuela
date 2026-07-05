/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shared;
import matricula.model.Matricula;
import matricula.model.MatriculaCurso;
import plan_estudios.model.Curso;
import plan_estudios.model.Grado;
import alumnos.model.Apoderado;
import notas.model.RegistroBimestral;
import notas.model.Evaluacion;
import alumnos.model.Alumno;
import docentes.model.Docente;
import java.util.ArrayList;
/**
 *
 * @author Alexis
 */
public final class RowMappers {

    private RowMappers() {
    }

    public static final IRowMapper<Evaluacion> EVALUACION_ROW_MAPPER = rs -> {
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setId(rs.getInt("idEvaluacion"));
        evaluacion.setNombre(rs.getString("nombre"));
        evaluacion.setTipo(TipoEvaluacion.valueOf(rs.getString("tipo")));
        evaluacion.setNota(rs.getDouble("nota"));
        evaluacion.setPeso(rs.getDouble("peso"));
        return evaluacion;
    };

    public static final IRowMapper<Grado> GRADO_ROW_MAPPER = rs -> {
        Grado grado = new Grado();
        grado.setId(rs.getInt("idGrado"));
        grado.setNombre(rs.getString("nombre"));
        grado.setNivel(rs.getString("nivel"));
        grado.setActivo(rs.getString("estado").equalsIgnoreCase("ACTIVO"));
        return grado;
    };

    public static final IRowMapper<Alumno> ALUMNO_ROW_MAPPER = rs -> {
        Apoderado apoderado = null;
        
        if (rs.getObject("idApoderado") != null) {
            java.sql.Date apodFechaNac = rs.getDate("apoderadoFechaNacimiento");
            
            apoderado = new Apoderado(
                    rs.getString("apoderadoParentesco"),
                    rs.getString("apoderadoTelefono"),
                    rs.getString("apoderadoCorreo"),
                    rs.getInt("idApoderado"),
                    rs.getString("apoderadoDni"),
                    rs.getString("apoderadoNombres"),
                    rs.getString("apoderadoApellidos"),
                    apodFechaNac != null ? apodFechaNac.toLocalDate() : null,
                    "ACTIVO".equalsIgnoreCase(rs.getString("apoderadoEstado"))
            );
        }

        java.sql.Date alumFechaNac = rs.getDate("fechaNacimiento");

        return new Alumno(
                rs.getString("codigoEstudiante"),
                apoderado,
                rs.getInt("idAlumno"),
                rs.getString("dni"),
                rs.getString("nombres"),
                rs.getString("apellidos"),
                alumFechaNac != null ? alumFechaNac.toLocalDate() : null, 
                "ACTIVO".equalsIgnoreCase(rs.getString("estado"))
        );
    };
    

    public static final IRowMapper<Docente> DOCENTE_ROW_MAPPER = rs -> {
        java.sql.Date docFechaNac = rs.getDate("fechaNacimiento");
        
        return new Docente(
                rs.getString("codigoDocente"),
                rs.getString("tituloProfesional"),
                rs.getString("especialidadAcademica"),
                rs.getString("correo"),
                rs.getString("telefono"),
                rs.getInt("idDocente"),
                rs.getString("dni"),
                rs.getString("nombres"),
                rs.getString("apellidos"),
                docFechaNac != null ? docFechaNac.toLocalDate() : null, 
                "ACTIVO".equalsIgnoreCase(rs.getString("estado"))
        );
    };

    public static final IRowMapper<Curso> CURSO_ROW_MAPPER = rs -> {
        Curso curso = new Curso();
        curso.setId(rs.getInt("idCurso"));
        curso.setCodigo(rs.getString("codigoCurso"));
        curso.setNombre(rs.getString("nombre"));
        curso.setHorasSemanales(rs.getInt("horasSemanales"));
        curso.setActivo(rs.getString("estado").equalsIgnoreCase("ACTIVO"));

        
        try {
            if (rs.getObject("idGrado") != null) {
                Grado grado = new Grado();
                grado.setId(rs.getInt("idGrado"));
                grado.setNombre(rs.getString("gradoNombre")); 
                grado.setNivel(rs.getString("gradoNivel"));   
                curso.asociarGrado(grado);
            }
        } catch (java.sql.SQLException e) {
        }

        try {
            if (rs.getObject("idDocente") != null) {
                Docente docente = new Docente();
                docente.setId(rs.getInt("idDocente"));
                docente.setNombres(rs.getString("docenteNombres"));     
                docente.setApellidos(rs.getString("docenteApellidos"));
                curso.setDocente(docente);
            }
        } catch (java.sql.SQLException e) {
        }

        return curso;
    };

    public static final IRowMapper<Matricula> MATRICULA_ROW_MAPPER = rs -> {
        Matricula matricula = new Matricula();
        matricula.setId(rs.getInt("idMatricula"));
        matricula.setCodigoMatricula(rs.getString("codigoMatricula"));
        matricula.setAñoEscolar(rs.getInt("anioEscolar"));
        matricula.setFechaMatricula(rs.getDate("fechaMatricula").toLocalDate());
        matricula.setActivo(rs.getString("estado").equalsIgnoreCase("VIGENTE"));
        return matricula;
    };

    public static final IRowMapper<RegistroBimestral> REGISTRO_BIMESTRAL_ROW_MAPPER = rs -> {
        RegistroBimestral rb = new RegistroBimestral();

        rb.setId(rs.getInt("idRegistroBimestral"));
        rb.setBimestre(shared.Bimestre.desdeId(rs.getInt("bimestre")));
        rb.setActivo(rs.getString("estado").equalsIgnoreCase("ABIERTO"));

        MatriculaCurso mc = new MatriculaCurso();
        mc.setId(rs.getInt("idMatriculaCurso"));

        try {
            plan_estudios.model.Curso curso = new plan_estudios.model.Curso();
            curso.setNombre(rs.getString("cursoNombre"));
            mc.setCurso(curso);
        } catch (Exception e) {
        }

        rb.setMatriculaCurso(mc);
        rb.setEvaluaciones(new ArrayList<>());

        return rb;
    };
}