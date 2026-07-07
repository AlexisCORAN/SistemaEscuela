/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package docentes.dao;
import java.util.List;
import java.sql.Connection;
import shared.JdbcTemplate;
import shared.RowMappers;
import docentes.model.Docente;
import java.util.Collections;
/**
 *
 * @author Alexis
 */
public class DocenteDAOImpl implements IDocenteDAO {
    
    private final Connection conexion;

    private static final String SELECT_BASE = 
        "SELECT idDocente, codigoDocente, dni, nombres, apellidos, fechaNacimiento, " +
        "tituloProfesional, especialidadAcademica, telefono, correo, estado FROM Docente";

    public DocenteDAOImpl(final Connection conexion) {
        this.conexion = conexion;
    }

    @Override
    public List<Docente> listarTodos() {
        if (conexion == null) return Collections.emptyList();
        return JdbcTemplate.query(conexion, SELECT_BASE, RowMappers.DOCENTE_ROW_MAPPER);
    }

    @Override
    public String obtenerUltimoCodigo() {
        if (conexion == null) return null;
        final String sql = "SELECT MAX(codigoDocente) FROM Docente";
        return JdbcTemplate.queryForObject(conexion, sql, (rs) -> rs.getString(1));
    }

    @Override
    public boolean insertar(final Docente d) {
        if (conexion == null) return false;
        final String sql = "INSERT INTO Docente (codigoDocente, dni, nombres, apellidos, fechaNacimiento, tituloProfesional, especialidadAcademica, telefono, correo, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return JdbcTemplate.update(conexion, sql,
                d.getCodigoDocente(),
                d.getDni(),
                d.getNombres(), 
                d.getApellidos(), 
                d.getFechaNacimiento() != null ? java.sql.Date.valueOf(d.getFechaNacimiento()) : null, 
                d.getTituloProfesional(),
                d.getEspecialidadAcademica(), 
                d.getTelefono(),
                d.getCorreo(),
                d.isActivo() ? "ACTIVO" : "CESADO"
                ) > 0;
    }

    @Override
    public boolean actualizar(final Docente d) {
        if (conexion == null) return false;
        final String sql = "UPDATE Docente SET codigoDocente = ?, dni = ?, nombres = ?, apellidos = ?, fechaNacimiento = ?, " +
                     "tituloProfesional = ?, especialidadAcademica = ?, telefono = ?, correo = ?, estado = ? WHERE idDocente = ?";
        
        Object[] params = {
            d.getCodigoDocente(), d.getDni(), d.getNombres(), d.getApellidos(), 
            d.getFechaNacimiento() != null ? java.sql.Date.valueOf(d.getFechaNacimiento()) : null, 
            d.getTituloProfesional(), d.getEspecialidadAcademica(), d.getTelefono(), d.getCorreo(), 
            d.isActivo() ? "ACTIVO" : "CESADO", d.getId() 
        };
        return JdbcTemplate.update(conexion, sql, params) > 0;
    }

    @Override
    public boolean eliminar(final Object id) {
        if (conexion == null) return false;
        final String sql = "DELETE FROM Docente WHERE idDocente = ?";
        return JdbcTemplate.update(conexion, sql, id) > 0;
    }

    @Override
    public Docente buscarPorCodigo(final String codigoDocente) {
        if (conexion == null) return null;
        final String sql = SELECT_BASE + " WHERE codigoDocente = ?";
        return JdbcTemplate.queryForObject(conexion, sql, RowMappers.DOCENTE_ROW_MAPPER, codigoDocente);
    }

    @Override
    public List<Docente> listarPorEspecialidad(final String especialidadAcademica) {
        if (conexion == null) return Collections.emptyList();
        final String sql = SELECT_BASE + " WHERE especialidadAcademica = ?";
        return JdbcTemplate.query(conexion, sql, RowMappers.DOCENTE_ROW_MAPPER, especialidadAcademica);
    }

    @Override
    public boolean existeDni(String dni, Integer idExcluido) {
        if (conexion == null || dni == null || dni.isBlank()) return false;
        final String sql = "SELECT COUNT(idDocente) FROM Docente WHERE dni = ? AND idDocente != ISNULL(?, -1)";
        Integer count = JdbcTemplate.queryForObject(conexion, sql, (rs) -> rs.getInt(1), dni, idExcluido);
        return count != null && count > 0;
    }
    
    @Override
    public List<Docente> listarPorEstado(boolean activo) {
        if (conexion == null) return Collections.emptyList();

        final String estadoDb = activo ? "ACTIVO" : "CESADO";
        final String sql = SELECT_BASE + " WHERE estado = ?";

        return JdbcTemplate.query(conexion, sql, RowMappers.DOCENTE_ROW_MAPPER, estadoDb);
    }

    @Override
    public Docente obtenerPorId(Object id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}