/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package plan_estudios.dao;
import java.sql.Connection;
import java.util.List;
import plan_estudios.model.Curso;
import shared.JdbcTemplate;
import shared.RowMappers;
import java.util.Collections;

/**
 *
 * @author Alexis
 */

public class CursoDAOImpl implements ICursoDAO {

    private final Connection conexion;

    private static final String SELECT_BASE = 
    "SELECT C.idCurso, C.codigoCurso, C.nombre, C.horasSemanales, C.idDocente, C.estado, " +
    "G.idGrado, G.nombre AS gradoNombre, G.nivel AS gradoNivel, " +
    "D.nombres AS docenteNombres, D.apellidos AS docenteApellidos " + 
    "FROM Curso C " +
    "LEFT JOIN Grado G ON C.idGrado = G.idGrado " +
    "LEFT JOIN Docente D ON C.idDocente = D.idDocente";

    public CursoDAOImpl(final Connection conexion) {
        this.conexion = conexion;
    }

    @Override
    public List<Curso> listarTodos() {
        if (conexion == null) return Collections.emptyList();
        return JdbcTemplate.query(conexion, SELECT_BASE, RowMappers.CURSO_ROW_MAPPER);
    }

    @Override
    public Curso obtenerPorId(final Object id) {
        if (conexion == null) return null;
        final String sql = SELECT_BASE + " WHERE C.idCurso = ?";
        return JdbcTemplate.queryForObject(conexion, sql, RowMappers.CURSO_ROW_MAPPER, id);
    }

    @Override
    public String obtenerUltimoCodigo() {
        if (conexion == null) return null;
        final String sql = "SELECT MAX(codigoCurso) FROM Curso";
        return JdbcTemplate.queryForObject(conexion, sql, (rs) -> rs.getString(1));
    }

    @Override
    public boolean insertar(final Curso entidad) {
        if (conexion == null) return false;
        final String sql = "INSERT INTO Curso (codigoCurso, nombre, horasSemanales, idDocente, idGrado, estado) VALUES (?, ?, ?, ?, ?, ?)";
        return JdbcTemplate.update(conexion, sql,
                entidad.getCodigo(),
                entidad.getNombre(),
                entidad.getHorasSemanales(),
                entidad.getDocente() != null ? entidad.getDocente().getId() : null,
                entidad.getGradoAsignado() != null ? entidad.getGradoAsignado().getId() : null,
                entidad.isActivo() ? "ACTIVO" : "INACTIVO") > 0;
    }

    @Override
    public boolean actualizar(final Curso entidad) {
        if (conexion == null) return false;
        final String sql = "UPDATE Curso SET codigoCurso = ?, nombre = ?, horasSemanales = ?, idDocente = ?, idGrado = ?, estado = ? WHERE idCurso = ?";
        return JdbcTemplate.update(conexion, sql,
                entidad.getCodigo(),
                entidad.getNombre(),
                entidad.getHorasSemanales(),
                entidad.getDocente() != null ? entidad.getDocente().getId() : null,
                entidad.getGradoAsignado() != null ? entidad.getGradoAsignado().getId() : null,
                entidad.isActivo() ? "ACTIVO" : "INACTIVO",
                entidad.getId()) > 0;
    }

    @Override
    public boolean eliminar(final Object id) {
        if (conexion == null) return false;
        final String sql = "DELETE FROM Curso WHERE idCurso = ?";
        return JdbcTemplate.update(conexion, sql, id) > 0;
    }

    @Override
    public Curso buscarPorCodigo(final String codigoCurso) {
        if (conexion == null) return null;
        final String sql = SELECT_BASE + " WHERE C.codigoCurso = ?";
        return JdbcTemplate.queryForObject(conexion, sql, RowMappers.CURSO_ROW_MAPPER, codigoCurso);
    }

    @Override
    public List<Curso> listarPorEstado(boolean activo) {
        if (conexion == null) return Collections.emptyList();

        final String estadoDb = activo ? "ACTIVO" : "INACTIVO";
        final String sql = SELECT_BASE + " WHERE C.estado = ?";

        return JdbcTemplate.query(conexion, sql, RowMappers.CURSO_ROW_MAPPER, estadoDb);
    }

    @Override
    public Curso buscarPorNombre(final String nombre) {
        if (conexion == null) return null;
        final String sql = SELECT_BASE + " WHERE C.nombre = ?";
        return JdbcTemplate.queryForObject(conexion, sql, RowMappers.CURSO_ROW_MAPPER, nombre);
    }
}