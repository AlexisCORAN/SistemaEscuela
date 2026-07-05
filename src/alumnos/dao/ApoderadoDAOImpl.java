/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package alumnos.dao;

import java.sql.Connection;
import java.util.List;
import java.util.Collections;
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
    public boolean eliminar(final Object id) {
        throw new UnsupportedOperationException("Operación de eliminación no soportada aún para Apoderado.");
    }

    @Override
    public Apoderado obtenerPorId(final Object id) {
        throw new UnsupportedOperationException("Operación de búsqueda no soportada aún para Apoderado.");
    }

    @Override
    public List<Apoderado> listarTodos() {
        return Collections.emptyList();
    }
}