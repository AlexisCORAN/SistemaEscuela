/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package notas.dao;
import java.sql.Connection;
import java.util.List;
import notas.model.RegistroBimestral;
import shared.JdbcTemplate;
import shared.RowMappers;
import java.util.Collections;
/**
 *
 * @author Alexis
 */
public class RegistroBimestralDAOImpl implements IRegistroBimestralDAO {

    private static final double NOTA_MINIMA_APROBATORIA = 11.0;

    private final Connection conexion;

    private static final String SELECT_BASE = 
        "SELECT rb.idRegistroBimestral, rb.idMatriculaCurso, rb.bimestre, rb.estado, " +
        "       c.nombre AS cursoNombre, " +
        "       a.dni AS alumnoDni, a.nombres AS alumnoNombres, a.apellidos AS alumnoApellidos " +
        "FROM RegistroBimestral rb " +
        "LEFT JOIN MatriculaCurso mc ON rb.idMatriculaCurso = mc.idMatriculaCurso " +
        "LEFT JOIN Curso c ON mc.idCurso = c.idCurso " +
        "LEFT JOIN Matricula m ON mc.idMatricula = m.idMatricula " +
        "LEFT JOIN Alumno a ON m.idAlumno = a.idAlumno";

    public RegistroBimestralDAOImpl(final Connection conexion) {
        this.conexion = conexion;
    }

    @Override
    public List<RegistroBimestral> listarTodos() {
        if (conexion == null) return Collections.emptyList();
        return JdbcTemplate.query(conexion, SELECT_BASE, RowMappers.REGISTRO_BIMESTRAL_ROW_MAPPER);
    }

    @Override
    public RegistroBimestral obtenerPorId(Object id) {
        if (conexion == null || id == null) return null;
        String sql = SELECT_BASE + " WHERE rb.idRegistroBimestral = ?";
        return JdbcTemplate.queryForObject(conexion, sql, RowMappers.REGISTRO_BIMESTRAL_ROW_MAPPER, id);
    }

    @Override
    public boolean insertar(RegistroBimestral entidad) {
        if (conexion == null || entidad == null) return false;
        String sql = "INSERT INTO RegistroBimestral (idMatriculaCurso, bimestre, estado) VALUES (?, ?, ?)";
        return JdbcTemplate.update(conexion, sql,
                entidad.getMatriculaCurso() != null ? entidad.getMatriculaCurso().getId() : null,
                entidad.getBimestre() != null ? entidad.getBimestre().getValorId() : null,
                entidad.isActivo() ? "ABIERTO" : "CERRADO") > 0;
    }

    @Override
    public boolean actualizar(RegistroBimestral entidad) {
        if (conexion == null || entidad == null || entidad.getId() == null) return false;
        String sql = "UPDATE RegistroBimestral SET idMatriculaCurso = ?, bimestre = ?, estado = ?, fechaCierre = ? WHERE idRegistroBimestral = ?";
        java.sql.Timestamp fechaCierre = entidad.isActivo() ? null : new java.sql.Timestamp(System.currentTimeMillis());

        return JdbcTemplate.update(conexion, sql,
                entidad.getMatriculaCurso() != null ? entidad.getMatriculaCurso().getId() : null,
                entidad.getBimestre() != null ? entidad.getBimestre().getValorId() : null,
                entidad.isActivo() ? "ABIERTO" : "CERRADO",
                fechaCierre,
                entidad.getId()) > 0;
    }

    @Override
    public boolean eliminar(Object id) {
        if (conexion == null || id == null) return false;

        String sqlHijos = "DELETE FROM Evaluacion WHERE idRegistroBimestral = ?";
        JdbcTemplate.update(conexion, sqlHijos, id);

        String sqlCabecera = "DELETE FROM RegistroBimestral WHERE idRegistroBimestral = ?";
        return JdbcTemplate.update(conexion, sqlCabecera, id) > 0;
    }

    @Override
    public List<RegistroBimestral> listarPorMatriculaCurso(Integer idMatriculaCurso) {
        if (conexion == null || idMatriculaCurso == null) return Collections.emptyList();
        String sql = SELECT_BASE + " WHERE rb.idMatriculaCurso = ?";
        return JdbcTemplate.query(conexion, sql, RowMappers.REGISTRO_BIMESTRAL_ROW_MAPPER, idMatriculaCurso);
    }

    @Override
    public boolean verificarExistencia(Integer idMatriculaCurso, int bimestre) {
        if (conexion == null || idMatriculaCurso == null) return false;
        String sql = "SELECT COUNT(idRegistroBimestral) FROM RegistroBimestral WHERE idMatriculaCurso = ? AND bimestre = ?";
        Integer resultado = JdbcTemplate.queryForObject(
                conexion, 
                sql, 
                rs -> rs.getInt(1), 
                idMatriculaCurso, 
                bimestre
        );
        return resultado != null && resultado > 0;
    }

    @Override
    public List<RegistroBimestral> listarHistorialPorAlumno(Integer idAlumno) {
        if (conexion == null || idAlumno == null) return Collections.emptyList();
        String sql = SELECT_BASE + " WHERE m.idAlumno = ?";
        return JdbcTemplate.query(conexion, sql, RowMappers.REGISTRO_BIMESTRAL_ROW_MAPPER, idAlumno);
    }

    @Override
    public boolean actualizarNotaFinalMatriculaCurso(Integer idMatriculaCurso, double notaFinal) {
        if (conexion == null || idMatriculaCurso == null) return false;
        String sql = "UPDATE MatriculaCurso SET notaFinal = ? WHERE idMatriculaCurso = ?";
        return JdbcTemplate.update(conexion, sql, notaFinal, idMatriculaCurso) > 0;
    }

    @Override
    public List<Object[]> listarNotasParaTabla(int idGrado, int idCurso, int bimestre) {
        if (conexion == null) return Collections.emptyList();

        String sql = "SELECT rb.idRegistroBimestral, m.codigoMatricula, a.apellidos + ', ' + a.nombres AS alumnoNombre, " +
                     "       MAX(CASE WHEN e.tipo = 'PRACTICA' THEN e.nota ELSE 0 END) as practica, " +
                     "       MAX(CASE WHEN e.tipo = 'TAREA' THEN e.nota ELSE 0 END) as tarea, " +
                     "       MAX(CASE WHEN e.tipo = 'PARCIAL' THEN e.nota ELSE 0 END) as parcial, " +
                     "       MAX(CASE WHEN e.tipo = 'BIMESTRAL' THEN e.nota ELSE 0 END) as bimestral " +
                     "FROM RegistroBimestral rb " +
                     "INNER JOIN MatriculaCurso mc ON rb.idMatriculaCurso = mc.idMatriculaCurso " +
                     "INNER JOIN Matricula m ON mc.idMatricula = m.idMatricula " +
                     "INNER JOIN Alumno a ON m.idAlumno = a.idAlumno " +
                     "LEFT JOIN Evaluacion e ON rb.idRegistroBimestral = e.idRegistroBimestral " +
                     "WHERE m.idGrado = ? AND mc.idCurso = ? AND rb.bimestre = ? " +
                     "GROUP BY rb.idRegistroBimestral, m.codigoMatricula, a.apellidos, a.nombres";

        return JdbcTemplate.query(conexion, sql, rs -> new Object[] {
                rs.getInt("idRegistroBimestral"),
                rs.getString("codigoMatricula"),
                rs.getString("alumnoNombre"),
                rs.getDouble("practica"),
                rs.getDouble("tarea"),
                rs.getDouble("parcial"),
                rs.getDouble("bimestral")
        }, idGrado, idCurso, bimestre);
    }

    @Override
    public List<Object[]> listarAlumnosEnRiesgo(int idGrado, int bimestre) {
        if (conexion == null) return Collections.emptyList();

        String sql = "SELECT a.dni, a.apellidos + ', ' + a.nombres AS alumno, c.nombre AS curso, " +
                     "       (MAX(CASE WHEN e.tipo = 'PRACTICA' THEN e.nota ELSE 0 END)*0.2 + " +
                     "        MAX(CASE WHEN e.tipo = 'TAREA' THEN e.nota ELSE 0 END)*0.2 + " +
                     "        MAX(CASE WHEN e.tipo = 'PARCIAL' THEN e.nota ELSE 0 END)*0.3 + " +
                     "        MAX(CASE WHEN e.tipo = 'BIMESTRAL' THEN e.nota ELSE 0 END)*0.3) AS promedio " +
                     "FROM RegistroBimestral rb " +
                     "INNER JOIN MatriculaCurso mc ON rb.idMatriculaCurso = mc.idMatriculaCurso " +
                     "INNER JOIN Matricula m ON mc.idMatricula = m.idMatricula " +
                     "INNER JOIN Alumno a ON m.idAlumno = a.idAlumno " +
                     "INNER JOIN Curso c ON mc.idCurso = c.idCurso " +
                     "LEFT JOIN Evaluacion e ON rb.idRegistroBimestral = e.idRegistroBimestral " +
                     "WHERE m.idGrado = ? AND rb.bimestre = ? " +
                     "GROUP BY a.dni, a.apellidos, a.nombres, c.nombre " +
                     "HAVING (MAX(CASE WHEN e.tipo = 'PRACTICA' THEN e.nota ELSE 0 END)*0.2 + " +
                     "        MAX(CASE WHEN e.tipo = 'TAREA' THEN e.nota ELSE 0 END)*0.2 + " +
                     "        MAX(CASE WHEN e.tipo = 'PARCIAL' THEN e.nota ELSE 0 END)*0.3 + " +
                     "        MAX(CASE WHEN e.tipo = 'BIMESTRAL' THEN e.nota ELSE 0 END)*0.3) < ?";

        return JdbcTemplate.query(conexion, sql, rs -> new Object[] {
                rs.getString("dni"),
                rs.getString("alumno"),
                rs.getString("curso"),
                rs.getDouble("promedio")
        }, idGrado, bimestre, NOTA_MINIMA_APROBATORIA);
    }
}