/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package plan_estudios.dao;
import java.sql.Connection;
import java.util.List;
import plan_estudios.model.Grado;
import shared.JdbcTemplate;
import shared.RowMappers;
import java.util.Collections;
/**
 *
 * @author Alexis
 */

public class GradoDAOImpl implements IGradoDAO {

    private final Connection conexion;

    public GradoDAOImpl(final Connection conexion) {
        this.conexion = conexion;
    }

    @Override
    public List<Grado> listarTodos() {
        if (conexion == null) return Collections.emptyList();
        final String sql = "SELECT idGrado, nombre, nivel, estado FROM Grado";
        return JdbcTemplate.query(conexion, sql, RowMappers.GRADO_ROW_MAPPER);
    }

    @Override
    public Grado obtenerPorId(final Object id) {
        if (conexion == null) return null;
        final String sql = "SELECT idGrado, nombre, nivel, estado FROM Grado WHERE idGrado = ?";
        return JdbcTemplate.queryForObject(conexion, sql, RowMappers.GRADO_ROW_MAPPER, id);
    }

    @Override
    public boolean insertar(final Grado entidad) {
        if (conexion == null) return false;
        final String sql = "INSERT INTO Grado (nombre, nivel, estado) VALUES (?, ?, ?)";
        return JdbcTemplate.update(conexion, sql, 
                entidad.getNombre(), 
                entidad.getNivel(),
                entidad.isActivo() ? "ACTIVO" : "INACTIVO") > 0;
    }

    @Override
    public boolean actualizar(final Grado entidad) {
        if (conexion == null) return false;
        final String sql = "UPDATE Grado SET nombre = ?, nivel = ?, estado = ? WHERE idGrado = ?";
        return JdbcTemplate.update(conexion, sql, 
                entidad.getNombre(), 
                entidad.getNivel(), 
                entidad.isActivo() ? "ACTIVO" : "INACTIVO",
                entidad.getId()) > 0;
    }

    @Override
    public boolean eliminar(final Object id) {
        if (conexion == null) return false;
        final String sql = "DELETE FROM Grado WHERE idGrado = ?";
        return JdbcTemplate.update(conexion, sql, id) > 0;
    }

    @Override
    public Grado buscarPorNombreYNivel(final String nombre, final String nivel) {
        if (conexion == null) return null;
        final String sql = "SELECT idGrado, nombre, nivel, estado FROM Grado WHERE nombre = ? AND nivel = ?";
        return JdbcTemplate.queryForObject(conexion, sql, RowMappers.GRADO_ROW_MAPPER, nombre, nivel);
    }

    @Override
    public List<Grado> listarPorNivel(final String nivel) {
        if (conexion == null) return Collections.emptyList();
        final String sql = "SELECT idGrado, nombre, nivel, estado FROM Grado WHERE nivel = ?";
        return JdbcTemplate.query(conexion, sql, RowMappers.GRADO_ROW_MAPPER, nivel);
    }
}