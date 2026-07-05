/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package matricula.dao;
import alumnos.model.Alumno;
import java.sql.Connection;
import java.util.List;
import matricula.model.Matricula;
import matricula.model.MatriculaCurso;
import shared.JdbcTemplate;
import java.util.ArrayList;
import java.util.Collections;
import plan_estudios.model.Curso;
import plan_estudios.model.Grado;
import shared.RowMappers;
import shared.IRowMapper;
/*
 * @author Alexis
 */

public class MatriculaDAOImpl implements IMatriculaDAO {

    private final Connection conexion;

    private static final String SELECT_MATRICULA_BASE = 
        "SELECT M.idMatricula, M.codigoMatricula, M.fechaMatricula, M.añoEscolar AS anioEscolar, M.estado AS estadoCabecera, " +
        "A.idAlumno, A.dni AS alumnoDni, A.nombres AS alumnoNombres, A.apellidos AS alumnoApellidos, A.codigoEstudiante AS alumnoCodigoEstudiante, " +
        "G.idGrado, G.nombre AS gradoNombre, G.nivel AS gradoNivel " +
        "FROM Matricula M " +
        "INNER JOIN Alumno A ON M.idAlumno = A.idAlumno " +
        "INNER JOIN Grado G ON M.idGrado = G.idGrado";

    private static final String SELECT_DETALLE_CURSOS =
        "SELECT MC.idMatriculaCurso, MC.notaFinal, C.idCurso, C.codigoCurso, C.nombre AS cursoNombre, C.horasSemanales " +
        "FROM MatriculaCurso MC " +
        "INNER JOIN Curso C ON MC.idCurso = C.idCurso " +
        "WHERE MC.idMatricula = ?";

    public MatriculaDAOImpl(final Connection conexion) {
        this.conexion = conexion;
    }

    private static final IRowMapper<Matricula> MATRICULA_COMPLETA_MAPPER = rs -> {
        Matricula m = new Matricula();
        m.setId(rs.getInt("idMatricula"));
        m.setCodigoMatricula(rs.getString("codigoMatricula"));
        m.setAñoEscolar(rs.getInt("anioEscolar"));
        m.setFechaMatricula(rs.getDate("fechaMatricula").toLocalDate());
        m.setActivo(rs.getString("estadoCabecera").equalsIgnoreCase("VIGENTE"));

        Alumno al = new Alumno();
        al.setId(rs.getInt("idAlumno"));
        al.setDni(rs.getString("alumnoDni"));
        al.setNombres(rs.getString("alumnoNombres"));
        al.setApellidos(rs.getString("alumnoApellidos"));
        al.setCodigoEstudiante(rs.getString("alumnoCodigoEstudiante")); 
        m.setAlumno(al);

        Grado g = new Grado();
        g.setId(rs.getInt("idGrado"));
        g.setNombre(rs.getString("gradoNombre"));
        g.setNivel(rs.getString("gradoNivel"));
        m.setGrado(g);

        return m;
    };

    private void cargarDetalleCursos(Matricula m) {
        if (conexion == null || m == null) return;

        try {
            List<MatriculaCurso> detalles = JdbcTemplate.query(conexion, SELECT_DETALLE_CURSOS, rs -> {
                Curso c = new Curso();
                c.setId(rs.getInt("idCurso"));
                c.setCodigo(rs.getString("codigoCurso"));
                c.setNombre(rs.getString("cursoNombre"));
                c.setHorasSemanales(rs.getInt("horasSemanales"));
                return new MatriculaCurso(rs.getInt("idMatriculaCurso"), c, rs.getDouble("notaFinal"));
            }, m.getId());

            boolean estadoReal = m.getActivo();
            m.setActivo(true); 
            for (MatriculaCurso mc : detalles) {
                m.agregarCurso(mc);
            }
            m.setActivo(estadoReal); 
        } catch (Exception e) {
            System.err.println("Error al cargar el detalle de cursos de la matrícula: " + e.getMessage());
        }
    }

    @Override
    public List<Grado> listarGradosConCursos() {
        if (conexion == null) return Collections.emptyList();

        try {
            String sqlGrados = "SELECT idGrado, nombre, nivel, estado FROM Grado WHERE estado = 'ACTIVO'";
            List<Grado> gradosDB = JdbcTemplate.query(conexion, sqlGrados, RowMappers.GRADO_ROW_MAPPER);
            
            String sqlCursos = "SELECT idCurso, codigoCurso, nombre, horasSemanales FROM Curso WHERE idGrado = ?";
            for (Grado g : gradosDB) {
                List<Curso> cursosGrado = JdbcTemplate.query(conexion, sqlCursos, rs -> {
                    Curso c = new Curso();
                    c.setId(rs.getInt("idCurso"));
                    c.setCodigo(rs.getString("codigoCurso"));
                    c.setNombre(rs.getString("nombre"));
                    c.setHorasSemanales(rs.getInt("horasSemanales"));
                    return c;
                }, g.getId());
                
                for (Curso curso : cursosGrado) {
                    g.agregarCurso(curso);
                }
            }
            return gradosDB;
        } catch (Exception e) {
            System.err.println("Error al listar grados con cursos: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Matricula> listarTodos() {
        if (conexion == null) return Collections.emptyList();

        List<Matricula> lista = JdbcTemplate.query(conexion, SELECT_MATRICULA_BASE, MATRICULA_COMPLETA_MAPPER);
        for (Matricula m : lista) {
            cargarDetalleCursos(m);
        }
        return lista;
    }

    @Override
    public Matricula obtenerPorId(Object id) {
        if (conexion == null) return null;

        String sql = SELECT_MATRICULA_BASE + " WHERE M.idMatricula = ?";
        Matricula m = JdbcTemplate.queryForObject(conexion, sql, MATRICULA_COMPLETA_MAPPER, id);
        cargarDetalleCursos(m);
        return m;
    }

    @Override
    public boolean insertar(Matricula entidad) {
        if (conexion == null) return false;

        String sqlMatricula = "INSERT INTO Matricula (codigoMatricula, idAlumno, idGrado, fechaMatricula, añoEscolar, estado) VALUES (?, ?, ?, ?, ?, ?)";
        boolean matriculaInsertada = JdbcTemplate.update(conexion, sqlMatricula,
                entidad.getCodigoMatricula(),
                entidad.getAlumno().getId(), 
                entidad.getGrado().getId(),
                entidad.getFechaMatricula() != null ? java.sql.Date.valueOf(entidad.getFechaMatricula()) : java.sql.Date.valueOf(java.time.LocalDate.now()),
                entidad.getAñoEscolar(),
                entidad.getActivo() ? "VIGENTE" : "ANULADA"
        ) > 0;

        if (!matriculaInsertada) return false;

        String sqlId = "SELECT IDENT_CURRENT('Matricula')";
        Integer idMatriculaGenerado = JdbcTemplate.queryForObject(conexion, sqlId, (rs) -> rs.getInt(1));
        entidad.setId(idMatriculaGenerado);

        String sqlDetalle = "INSERT INTO MatriculaCurso (idMatricula, idCurso, notaFinal) VALUES (?, ?, ?)";
        for (MatriculaCurso mc : entidad.getCursosMatriculados()) {
            boolean cursoInsertado = JdbcTemplate.update(conexion, sqlDetalle,
                    entidad.getId(),
                    mc.getCurso().getId(),
                    mc.getNotaFinal()
            ) > 0;
            if (!cursoInsertado) return false;
        }
        return true;
    }

    @Override
    public boolean actualizar(Matricula entidad) {
        if (conexion == null) return false;

        String sql = "UPDATE Matricula SET idAlumno = ?, idGrado = ?, fechaMatricula = ?, añoEscolar = ?, estado = ? WHERE idMatricula = ?";
        return JdbcTemplate.update(conexion, sql,
                entidad.getAlumno().getId(),
                entidad.getGrado().getId(),
                entidad.getFechaMatricula() != null ? java.sql.Date.valueOf(entidad.getFechaMatricula()) : null,
                entidad.getAñoEscolar(),
                entidad.getActivo() ? "VIGENTE" : "ANULADA",
                entidad.getId()) > 0;
    }

    @Override
    public boolean eliminar(Object id) {
        if (conexion == null) return false;

        String sqlDetalle = "DELETE FROM MatriculaCurso WHERE idMatricula = ?";
        JdbcTemplate.update(conexion, sqlDetalle, id);
        String sqlMatricula = "DELETE FROM Matricula WHERE idMatricula = ?";
        return JdbcTemplate.update(conexion, sqlMatricula, id) > 0;
    }

    @Override
    public Matricula obtenerMatriculaActiva(String codigoEstudiante, int añoEscolar) {
        if (conexion == null) return null;

        String sql = SELECT_MATRICULA_BASE + " WHERE (A.codigoEstudiante = ? OR A.dni = ?) AND M.añoEscolar = ? AND M.estado = 'VIGENTE'";
        Matricula m = JdbcTemplate.queryForObject(conexion, sql, MATRICULA_COMPLETA_MAPPER, codigoEstudiante, codigoEstudiante, añoEscolar);
        cargarDetalleCursos(m);
        return m;
    }

    @Override
    public List<Matricula> listarPorGradoAño(int idGrado, int añoEscolar) {
        if (conexion == null) return Collections.emptyList();

        String sql = SELECT_MATRICULA_BASE + " WHERE M.idGrado = ? AND M.añoEscolar = ?";
        List<Matricula> lista = JdbcTemplate.query(conexion, sql, MATRICULA_COMPLETA_MAPPER, idGrado, añoEscolar);
        for (Matricula m : lista) {
            cargarDetalleCursos(m);
        }
        return lista;
    }

    @Override
    public boolean anularMatricula(Integer idMatricula) {
        if (conexion == null) return false;

        String sql = "UPDATE Matricula SET estado = 'ANULADA' WHERE idMatricula = ?";
        return JdbcTemplate.update(conexion, sql, idMatricula) > 0;
    }

    @Override
    public String obtenerUltimoCodigo() {
        if (conexion == null) return null;

        String sql = "SELECT TOP 1 codigoMatricula FROM Matricula ORDER BY idMatricula DESC";
        try {
            return JdbcTemplate.queryForObject(conexion, sql, (rs) -> rs.getString(1));
        } catch (Exception e) {
            return null;
        }
    }
}