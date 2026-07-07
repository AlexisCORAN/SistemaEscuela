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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
/**
 *
 * @author Alexis
 */
public final class RowMappers {

    private RowMappers() {
    }


    private static boolean hasColumn(ResultSet rs, String columnName) throws SQLException {
        java.sql.ResultSetMetaData rsmd = rs.getMetaData();
        int columns = rsmd.getColumnCount();
        for (int x = 1; x <= columns; x++) {
            if (columnName.equalsIgnoreCase(rsmd.getColumnLabel(x))) {
                return true;
            }
        }
        return false;
    }

    public static final IRowMapper<Evaluacion> EVALUACION_ROW_MAPPER = rs -> {
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setId(rs.getInt("idEvaluacion"));
        evaluacion.setNombre(rs.getString("nombre"));
        evaluacion.setTipo(TipoEvaluacion.valueOf(rs.getString("tipo").trim().toUpperCase()));
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

        if (hasColumn(rs, "idGrado") && rs.getObject("idGrado") != null) {
            Grado grado = new Grado();
            grado.setId(rs.getInt("idGrado"));
            grado.setNombre(rs.getString("gradoNombre")); 
            grado.setNivel(rs.getString("gradoNivel"));   
            curso.asociarGrado(grado);
        }

        if (hasColumn(rs, "idDocente") && rs.getObject("idDocente") != null) {
            Docente docente = new Docente();
            docente.setId(rs.getInt("idDocente"));
            docente.setNombres(rs.getString("docenteNombres"));     
            docente.setApellidos(rs.getString("docenteApellidos"));
            curso.setDocente(docente);
        }

        return curso;
    };
    
   public static final IRowMapper<Matricula> MATRICULA_COMPLETA_MAPPER = rs -> {
        final Matricula m = new Matricula();
        m.setId(rs.getInt("idMatricula"));
        m.setCodigoMatricula(rs.getString("codigoMatricula"));
        m.setAnioEscolar(rs.getInt("anioEscolar"));

        java.sql.Date fechaMat = rs.getDate("fechaMatricula");
        m.setFechaMatricula(fechaMat != null ? fechaMat.toLocalDate() : null);

        m.setActivo(rs.getString("estadoCabecera").equalsIgnoreCase("VIGENTE"));

        final Alumno al = new Alumno();
        al.setId(rs.getInt("idAlumno"));
        al.setDni(rs.getString("alumnoDni"));
        al.setNombres(rs.getString("alumnoNombres"));
        al.setApellidos(rs.getString("alumnoApellidos"));
        al.setCodigoEstudiante(rs.getString("alumnoCodigoEstudiante"));
        m.setAlumno(al);

        final Grado g = new Grado();
        g.setId(rs.getInt("idGrado"));
        g.setNombre(rs.getString("gradoNombre"));
        g.setNivel(rs.getString("gradoNivel"));
        m.setGrado(g);

        return m;
    };


    public static final IRowMapper<RegistroBimestral> REGISTRO_BIMESTRAL_ROW_MAPPER = rs -> {
        String estado = rs.getString("estado");
        boolean activo = estado != null && estado.equalsIgnoreCase("ABIERTO");

        Bimestre bimestre = shared.Bimestre.desdeId(rs.getInt("bimestre"));

        MatriculaCurso mc = new MatriculaCurso();
        if (hasColumn(rs, "idMatriculaCurso")) {
            mc.setId(rs.getInt("idMatriculaCurso"));
        }
        if (hasColumn(rs, "cursoNombre")) {
            plan_estudios.model.Curso curso = new plan_estudios.model.Curso();
            curso.setNombre(rs.getString("cursoNombre"));
            mc.setCurso(curso);
        }

        java.time.LocalDateTime fechaCierre = null;
        if (hasColumn(rs, "fechaCierre")) {
            java.sql.Timestamp ts = rs.getTimestamp("fechaCierre");
            fechaCierre = (ts != null) ? ts.toLocalDateTime() : null;
        }

        return new RegistroBimestral(
            rs.getInt("idRegistroBimestral"),
            mc,
            bimestre,
            activo,
            fechaCierre,
            new ArrayList<>()
        );
    };
}