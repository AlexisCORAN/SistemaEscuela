/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package plan_estudios.controller;
import java.sql.Connection;
import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;
import plan_estudios.dao.IGradoDAO;
import plan_estudios.dao.GradoDAOImpl;
import plan_estudios.model.Grado;
import config.ConexionDB;

/**
 *
 * @author Alexis
 */
public class GradoController {

    public GradoController() {
    }

    public List<Grado> obtenerGrados() {
        try {
            final Connection conn = ConexionDB.getInstance().getConexion();
            final IGradoDAO gradoDAO = new GradoDAOImpl(conn);
            return gradoDAO.listarTodos();
        } catch (final Exception e) {
            System.err.println("Error al obtener grados: " + e.getMessage());
            return Collections.emptyList();
        }
    }
    
    public List<Grado> obtenerGradosActivos() {
        return obtenerGrados().stream()
                .filter(Grado::isActivo)
                .collect(Collectors.toList());
    }

    public Grado obtenerGradoPorId(final Integer id) {
        if (id == null) return null;
        try {
            final Connection conn = ConexionDB.getInstance().getConexion();
            final IGradoDAO gradoDAO = new GradoDAOImpl(conn);
            return gradoDAO.obtenerPorId(id);
        } catch (final Exception e) {
            System.err.println("Error al obtener grado por id: " + e.getMessage());
            return null;
        }
    }
}