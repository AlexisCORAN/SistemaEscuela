/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package notas.dao;
import java.sql.Connection;
import java.util.List;
import notas.model.Evaluacion;
import shared.JdbcTemplate;
import shared.RowMappers;
import java.util.Collections;
/**
 *
 * @author Alexis
 */

public class EvaluacionDAOImpl implements IEvaluacionDAO {
    
    private final Connection conexion;

    public EvaluacionDAOImpl(Connection conexion) {
        this.conexion = conexion;
    }

    @Override
    public boolean insertar(Evaluacion entidad) {
        if (conexion == null || entidad == null || entidad.getRegistroBimestral() == null) return false;
        String sql = "INSERT INTO Evaluacion (idRegistroBimestral, nombre, tipo, nota, peso) VALUES (?, ?, ?, ?, ?)";
        return JdbcTemplate.update(conexion, sql, 
                entidad.getRegistroBimestral().getId(), 
                entidad.getNombre(), 
                entidad.getTipo().name(), 
                entidad.getNota(), 
                entidad.getPeso()) > 0;
    }
    
    @Override
    public List<Evaluacion> listarTodos() {
        if (conexion == null) return Collections.emptyList();
        String sql = "SELECT idEvaluacion, idRegistroBimestral, nombre, tipo, nota, peso FROM Evaluacion";
        return JdbcTemplate.query(conexion, sql, RowMappers.EVALUACION_ROW_MAPPER);
    }

    @Override
    public Evaluacion obtenerPorId(Object id) {
        if (conexion == null || id == null) return null;
        String sql = "SELECT idEvaluacion, idRegistroBimestral, nombre, tipo, nota, peso FROM Evaluacion WHERE idEvaluacion = ?";
        return JdbcTemplate.queryForObject(conexion, sql, RowMappers.EVALUACION_ROW_MAPPER, id);
    }

    @Override
    public boolean actualizar(Evaluacion entidad) {
        if (conexion == null || entidad == null || entidad.getId() == null) return false;
        String sql = "UPDATE Evaluacion SET nombre = ?, tipo = ?, nota = ?, peso = ? WHERE idEvaluacion = ?";
        return JdbcTemplate.update(conexion, sql, entidad.getNombre(), entidad.getTipo().name(), entidad.getNota(), entidad.getPeso(), entidad.getId()) > 0;
    }

    @Override
    public boolean eliminar(Object id) {
        if (conexion == null || id == null) return false;
        String sql = "DELETE FROM Evaluacion WHERE idEvaluacion = ?";
        return JdbcTemplate.update(conexion, sql, id) > 0;
    }

    @Override
    public List<Evaluacion> listarPorRegistroBimestral(Integer idRegistroBimestral) {
        if (conexion == null || idRegistroBimestral == null) return Collections.emptyList();
        String sql = "SELECT idEvaluacion, idRegistroBimestral, nombre, tipo, nota, peso FROM Evaluacion WHERE idRegistroBimestral = ?";
        return JdbcTemplate.query(conexion, sql, RowMappers.EVALUACION_ROW_MAPPER, idRegistroBimestral);
    }
     @Override
    public boolean insertarConCabecera(Evaluacion entidad, Integer idRegistroBimestral) {
        if (conexion == null || entidad == null || idRegistroBimestral == null) return false;
        String sql = "INSERT INTO Evaluacion (idRegistroBimestral, nombre, tipo, nota, peso) VALUES (?, ?, ?, ?, ?)";
        return JdbcTemplate.update(conexion, sql, idRegistroBimestral, entidad.getNombre(), entidad.getTipo().name(), entidad.getNota(), entidad.getPeso()) > 0;
    }
}
    
   
