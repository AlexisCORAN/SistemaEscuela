/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package plan_estudios.service;
import config.ConexionDB;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.Collections;
import java.util.List;
import plan_estudios.dao.*;
import plan_estudios.model.*;
import shared.TransactionRunner;

/**
 *
 * @author Alexis
 */
public class PlanEstudiosService {

    public List<Curso> obtenerCursos() {
        try (Connection conn = ConexionDB.getInstance().getConexion()) {
            ICursoDAO cursoDAO = new CursoDAOImpl(conn);
            return cursoDAO.listarTodos();
        } catch (Exception e) {
            System.err.println("Error al obtener los cursos: " + e.getMessage());
            return Collections.emptyList();
        }
    }
    
    public Curso buscarCursoPorCodigoExacto(String codigo) {
        if (codigo == null || codigo.isBlank()) return null;

        try (Connection conn = ConexionDB.getInstance().getConexion()) {
            ICursoDAO cursoDAO = new CursoDAOImpl(conn);
            return cursoDAO.buscarPorCodigo(codigo.trim());
        } catch (SQLException e) {
            System.err.println("Error al buscar curso exacto: " + e.getMessage());
            return null;
        }
    }

    public List<Curso> obtenerCursosPorEstado(String filtroEstado) {
        if (filtroEstado == null || filtroEstado.equals("TODOS")) {
            return obtenerCursos();
        }
        boolean esActivo = filtroEstado.equals("ACTIVOS");
        try (Connection conn = ConexionDB.getInstance().getConexion()) {
            ICursoDAO cursoDAO = new CursoDAOImpl(conn);
            return cursoDAO.listarPorEstado(esActivo);
        } catch (Exception e) {
            System.err.println("Error al filtrar cursos por estado: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public void registrarCurso(Curso curso) {
        TransactionRunner.ejecutar(conn -> {
            ICursoDAO cursoDAO = new CursoDAOImpl(conn);
            generarYAsignarCodigo(curso, cursoDAO);
            curso.setActivo(true);
            
            if (!cursoDAO.insertar(curso)) {
                throw new RuntimeException("Error al registrar el curso en la base de datos.");
            }
            return null;
        }, null);
    }

    public void actualizarCurso(Curso curso) {
        TransactionRunner.ejecutar(conn -> {
            ICursoDAO cursoDAO = new CursoDAOImpl(conn);
            if (!cursoDAO.actualizar(curso)) {
                throw new RuntimeException("Error al actualizar el curso.");
            }
            return null;
        }, null);
    }

    public void procesarBajaPorCodigo(String codigoCurso) {
        TransactionRunner.ejecutar(conn -> {
            ICursoDAO cursoDAO = new CursoDAOImpl(conn);
            Curso curso = cursoDAO.buscarPorCodigo(codigoCurso);

            if (curso == null) {
                throw new IllegalArgumentException("No se encontró el curso con código: " + codigoCurso);
            }
            if (!curso.isActivo()) {
                throw new IllegalArgumentException("El curso ya se encuentra dado de baja.");
            }
            curso.setActivo(false); 

            if (!cursoDAO.actualizar(curso)) {
                throw new RuntimeException("Error al dar de baja en la base de datos.");
            }            
            return null;
        }, null);
    }

    public void procesarReactivacionPorCodigo(String codigoCurso) {
        TransactionRunner.ejecutar(conn -> {
            ICursoDAO cursoDAO = new CursoDAOImpl(conn);
            Curso curso = cursoDAO.buscarPorCodigo(codigoCurso);

            if (curso == null) {
                throw new IllegalArgumentException("No se encontró el curso con código: " + codigoCurso);
            }
            
            if (curso.isActivo()) {
                throw new IllegalArgumentException("El curso ya se encuentra activo.");
            }

            curso.setActivo(true); 

            if (!cursoDAO.actualizar(curso)) {
                throw new RuntimeException("Error al reactivar en la base de datos.");
            }
            return null;
        }, null);
    }

    public List<Grado> obtenerGradosActivos() {
        try (Connection conn = ConexionDB.getInstance().getConexion()) {
            IGradoDAO gradoDAO = new GradoDAOImpl(conn);
            return gradoDAO.listarTodos().stream()
                    .filter(Grado::isActivo)
                    .toList();
        } catch (Exception e) {
            System.err.println("Error al obtener grados: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public Grado obtenerGradoPorId(Integer id) {
        if (id == null) return null;
        try (Connection conn = ConexionDB.getInstance().getConexion()) {
            return new GradoDAOImpl(conn).obtenerPorId(id);
        } catch (Exception e) {
            System.err.println("Error al obtener grado: " + e.getMessage());
            return null;
        }
    }

    private void generarYAsignarCodigo(Curso curso, ICursoDAO cursoDAO) {
        String ultimoCod = cursoDAO.obtenerUltimoCodigo();
        int correlativo = 1;
        
        if (ultimoCod != null && ultimoCod.contains("-")) {
            String[] partes = ultimoCod.split("-");
            if (partes.length == 3) {
                correlativo = Integer.parseInt(partes[2]) + 1;
            }
        }
        curso.generarCodigoCurso(correlativo);
    }
    
    public List<Grado> obtenerGradosConCursos() {
        try (Connection conn = ConexionDB.getInstance().getConexion()) {
            final IGradoDAO gradoDAO = new GradoDAOImpl(conn);
            final ICursoDAO cursoDAO = new CursoDAOImpl(conn);

            List<Grado> listaGrados = gradoDAO.listarTodos().stream()
                    .filter(Grado::isActivo)
                    .toList();
            
            if (listaGrados.isEmpty()) {
                return listaGrados;
            }

            List<Curso> cursosActivos = cursoDAO.listarPorEstado(true);

            for (Grado grado : listaGrados) {
                for (Curso curso : cursosActivos) {
                    if (curso.getGradoAsignado() != null && curso.getGradoAsignado().getId().equals(grado.getId())) {
                        grado.agregarCurso(curso);
                    }
                }
            }
            
            return listaGrados;
        } catch (final Exception e) {
            System.err.println("Error en PlanEstudiosService al procesar mallas: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}