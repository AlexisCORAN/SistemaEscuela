/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package alumnos.dao;
import java.sql.Connection;
import java.util.List;
import shared.JdbcTemplate;
import shared.RowMappers;
import alumnos.model.Alumno;
import java.util.Collections;

/**
 *
 * @author Alexis
 */

public class AlumnoDAOImpl implements IAlumnoDAO {
    
    private final Connection conexion;
    
    private static final String SELECT_BASE = 
        "SELECT A.idAlumno, A.codigoEstudiante, A.dni, A.fechaNacimiento, A.nombres, A.apellidos, A.estado, " +
        "P.idApoderado, P.dni AS apoderadoDni, P.nombres AS apoderadoNombres, " +
        "P.apellidos AS apoderadoApellidos, P.parentesco AS apoderadoParentesco, " +
        "P.telefono AS apoderadoTelefono, P.correo AS apoderadoCorreo, " +
        "P.fechaNacimiento AS apoderadoFechaNacimiento, P.estado AS apoderadoEstado, " +
        "ISNULL(G.nombre + ' ' + G.nivel, 'No Matriculado') AS gradoNombre " +
        "FROM Alumno A " +
        "LEFT JOIN Apoderado P ON A.idApoderado = P.idApoderado " +
        "LEFT JOIN Matricula M ON A.idAlumno = M.idAlumno AND M.estado = 'VIGENTE' " +
        "LEFT JOIN Grado G ON M.idGrado = G.idGrado";

    public AlumnoDAOImpl(final Connection conexion) {
        this.conexion = conexion;
    }

    @Override
    public List<Alumno> listarTodos() {
        if (conexion == null) return Collections.emptyList();
        return JdbcTemplate.query(conexion, SELECT_BASE, RowMappers.ALUMNO_ROW_MAPPER);
    }

    
    @Override
    public String obtenerUltimoCodigo() {
        if (conexion == null) return null;
        
        final String sql = "SELECT MAX(codigoEstudiante) FROM Alumno";
        return JdbcTemplate.queryForObject(conexion, sql, (rs) -> rs.getString(1));
    }

    @Override
    public boolean insertar(final Alumno a) {
        if (conexion == null) return false;

        if (a.getApoderado() == null || a.getApoderado().getId() == null) {
            throw new IllegalStateException("No se puede registrar un alumno sin un Apoderado previamente guardado en la BD.");
        }

        final String sql = "INSERT INTO Alumno (codigoEstudiante, dni, fechaNacimiento, nombres, apellidos, estado, idApoderado) VALUES (?, ?, ?, ?, ?, ?, ?)";
        return JdbcTemplate.update(conexion, sql,
                a.getCodigoEstudiante(),
                a.getDni(),
                a.getFechaNacimiento(),
                a.getNombres(),
                a.getApellidos(),
                a.isActivo() ? "ACTIVO" : "RETIRADO",
                a.getApoderado().getId()
        ) > 0;
    }

    @Override
    public boolean actualizar(final Alumno a) {
        if (conexion == null) return false;

        final String sql = "UPDATE Alumno SET codigoEstudiante = ?, dni = ?, fechaNacimiento = ?, nombres = ?, " +
                     "apellidos = ?, estado = ?, idApoderado = ? WHERE idAlumno = ?";
        return JdbcTemplate.update(conexion, sql,
                a.getCodigoEstudiante(),
                a.getDni(),
                a.getFechaNacimiento(),
                a.getNombres(),
                a.getApellidos(),
                a.isActivo() ? "ACTIVO" : "RETIRADO",
                a.getApoderado().getId(), 
                a.getId()
        ) > 0;
    }

    @Override
    public boolean eliminar(final Object id) {
        if (conexion == null) return false;

        final String sql = "DELETE FROM Alumno WHERE idAlumno = ?";
        return JdbcTemplate.update(conexion, sql, id) > 0;
    }

    @Override
    public Alumno buscarPorCodigo(final String codigoEstudiante) {
        if (conexion == null) return null;
        
        final String sql = SELECT_BASE + " WHERE A.codigoEstudiante = ?";
        return JdbcTemplate.queryForObject(conexion, sql, RowMappers.ALUMNO_ROW_MAPPER, codigoEstudiante);
    }
    
    @Override
    public Alumno buscarPorDni(final String codigoEstudiante) {
        if (conexion == null) return null;
        
        final String sql = SELECT_BASE + " WHERE A.dni = ?";
        return JdbcTemplate.queryForObject(conexion, sql, RowMappers.ALUMNO_ROW_MAPPER, codigoEstudiante);
    }
    

    @Override
    public List<Alumno> listarPorEstado(boolean activo) {
        if (conexion == null) return Collections.emptyList();

        final String estadoDb = activo ? "ACTIVO" : "RETIRADO";
        final String sql = SELECT_BASE + " WHERE A.estado = ?";

        return JdbcTemplate.query(conexion, sql, RowMappers.ALUMNO_ROW_MAPPER, estadoDb);
    }

    @Override
    public boolean existeDni(String dni, Integer idExcluido) {
        if (conexion == null || dni == null || dni.isBlank()) return false;
        
        final String sql = "SELECT COUNT(idAlumno) FROM Alumno WHERE dni = ? AND idAlumno != ISNULL(?, -1)";
        Integer count = JdbcTemplate.queryForObject(conexion, sql, (rs) -> rs.getInt(1), dni, idExcluido);
        
        return count != null && count > 0;
    }

    @Override
    public Alumno obtenerPorId(Object id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}