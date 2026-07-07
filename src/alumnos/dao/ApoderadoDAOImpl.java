/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package alumnos.dao;

import java.sql.Connection;
import java.util.List;
import shared.JdbcTemplate;
import alumnos.model.Apoderado;
/**
 *
 * @author Alexis
 */
public class ApoderadoDAOImpl implements IApoderadoDAO {
    
    private final Connection conexion;

    public ApoderadoDAOImpl(final Connection conexion) {
        this.conexion = conexion;
    }

    @Override
    public boolean insertar(final Apoderado a) {
        if (conexion == null) return false;

        String sql = "INSERT INTO Apoderado (dni, fechaNacimiento, nombres, apellidos, parentesco, telefono, correo, estado) VALUES (?, ?, ?, ?, ?, ?, ?, 'ACTIVO')";
        Integer idGenerado = JdbcTemplate.ejecutarActualizacionConId(conexion, sql, 
                a.getDni(), a.getFechaNacimiento(), a.getNombres(), a.getApellidos(), 
                a.getParentesco(), a.getTelefono(), a.getCorreo());

        if (idGenerado != null) {
            a.setId(idGenerado);
            return true;
        }
        return false;
    }

    @Override
    public boolean actualizar(final Apoderado a) {
        if (conexion == null) return false;
        
        String sql = "UPDATE Apoderado SET dni = ?, fechaNacimiento = ?, nombres = ?, apellidos = ?, parentesco = ?, telefono = ?, correo = ? WHERE idApoderado = ?";
        return JdbcTemplate.update(conexion, sql, 
                a.getDni(), a.getFechaNacimiento(), a.getNombres(), a.getApellidos(), 
                a.getParentesco(), a.getTelefono(), a.getCorreo(), a.getId()) > 0;
    }
    
     @Override
    public boolean existeDni(String dni, Integer idExcluido) {
        if (conexion == null || dni == null || dni.isBlank()) return false;
        
        final String sql = "SELECT COUNT(idApoderado) FROM Apoderado WHERE dni = ? AND idApoderado != ISNULL(?, -1)";
        Integer count = JdbcTemplate.queryForObject(conexion, sql, (rs) -> rs.getInt(1), dni, idExcluido);
        
        return count != null && count > 0;
    }

    @Override
    public List<Apoderado> listarTodos() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Apoderado obtenerPorId(Object id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean eliminar(Object id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    

}