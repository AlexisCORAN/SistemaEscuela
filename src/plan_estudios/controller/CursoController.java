/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package plan_estudios.controller;
import java.sql.Connection;
import java.util.List;
import java.util.Collections;
import plan_estudios.dao.ICursoDAO;
import plan_estudios.dao.CursoDAOImpl;
import plan_estudios.model.Curso;
import config.ConexionDB;

/**
/**
 *
 * @author Alexis
 */
public class CursoController {

    public CursoController() {
    }

    public List<Curso> obtenerCursos() {
        try {
            final Connection conn = ConexionDB.getInstance().getConexion();
            final ICursoDAO cursoDAO = new CursoDAOImpl(conn);
            return cursoDAO.listarTodos();
        } catch (final Exception e) {
            System.err.println("Error al obtener cursos: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<Curso> buscarPorCodigo(final String criterio) {
        if (criterio == null || criterio.trim().isEmpty() || criterio.equals("Buscar por Código")) {
            return obtenerCursos();
        }
        try {
            final Connection conn = ConexionDB.getInstance().getConexion();
            final ICursoDAO cursoDAO = new CursoDAOImpl(conn);
            final Curso resultado = cursoDAO.buscarPorCodigo(criterio.trim());
            return resultado != null ? List.of(resultado) : Collections.emptyList();
        } catch (final Exception e) {
            System.err.println("Error al buscar curso: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public boolean registrarCurso(final Curso curso) {
        try {
            final Connection conn = ConexionDB.getInstance().getConexion();
            final ICursoDAO cursoDAO = new CursoDAOImpl(conn);
            
            final String ultimoCod = cursoDAO.obtenerUltimoCodigo();
            int correlativo = 1;
            if (ultimoCod != null && ultimoCod.contains("-")) {
                final String[] partes = ultimoCod.split("-");
                if (partes.length == 3) {
                    correlativo = Integer.parseInt(partes[2]) + 1;
                }
            }

            curso.generarCodigoCurso(correlativo);
            curso.setActivo(true); 
            return cursoDAO.insertar(curso);
        } catch (final Exception e) {
            System.err.println("Error al registrar curso: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarCurso(final Curso curso) {
        try {
            final Connection conn = ConexionDB.getInstance().getConexion();
            final ICursoDAO cursoDAO = new CursoDAOImpl(conn);
            return cursoDAO.actualizar(curso);
        } catch (final Exception e) {
            System.err.println("Error al actualizar curso: " + e.getMessage());
            return false;
        }
    }

    public boolean darDeBajaCurso(final Curso curso) {
        if (curso == null) return false;
        try {
            final Connection conn = ConexionDB.getInstance().getConexion();
            final ICursoDAO cursoDAO = new CursoDAOImpl(conn);
            curso.setActivo(false);
            return cursoDAO.actualizar(curso);
        } catch (final Exception e) {
            System.err.println("Error al dar de baja el curso: " + e.getMessage());
            return false;
        }
    }
}