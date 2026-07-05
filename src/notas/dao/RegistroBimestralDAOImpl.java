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
import config.ConexionDB;
import java.util.Collections;
/**
 *
 * @author Alexis
 */
public class RegistroBimestralDAOImpl implements IRegistroBimestralDAO {

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

    public RegistroBimestralDAOImpl() {
        this.conexion = ConexionDB.getInstance().getConexion();
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
                entidad.getBimestre(),
                entidad.getActivo() ? "ABIERTO" : "CERRADO") > 0;
    }

    @Override
    public boolean actualizar(RegistroBimestral entidad) {
        if (conexion == null || entidad == null || entidad.getId() == null) return false;
        String sql = "UPDATE RegistroBimestral SET idMatriculaCurso = ?, bimestre = ?, estado = ?, fechaCierre = ? WHERE idRegistroBimestral = ?";
        java.sql.Timestamp fechaCierre = entidad.getActivo() ? null : new java.sql.Timestamp(System.currentTimeMillis());
        
        return JdbcTemplate.update(conexion, sql,
                entidad.getMatriculaCurso() != null ? entidad.getMatriculaCurso().getId() : null,
                entidad.getBimestre(),
                entidad.getActivo() ? "ABIERTO" : "CERRADO",
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
        // CORREGIDO: Se aplica la constante SELECT_BASE con los alias requeridos por el IRowMapper
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
}