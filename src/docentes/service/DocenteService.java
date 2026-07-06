/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package docentes.service;
import config.ConexionDB;
import docentes.dao.DocenteDAOImpl;
import docentes.dao.IDocenteDAO;
import docentes.model.Docente;
import java.util.Collections;
import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;
import shared.TransactionRunner;

/**
 *
 * @author Alexis
 */
public class DocenteService {

    public List<Docente> obtenerDocentes() {
        try (Connection conn = ConexionDB.getInstance().getConexion()) {
            IDocenteDAO docenteDAO = new DocenteDAOImpl(conn);
            return docenteDAO.listarTodos();
        } catch (Exception e) {
            System.err.println("Error al obtener docentes: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public Docente buscarDocentePorCodigoExacto(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            return null;
        }
        try (Connection conn = ConexionDB.getInstance().getConexion()) {
            IDocenteDAO docenteDAO = new DocenteDAOImpl(conn);
            return docenteDAO.buscarPorCodigo(codigo.trim());
        } catch (Exception e) {
            System.err.println("Error al buscar docente: " + e.getMessage());
            return null;
        }
    }
    
    public List<Docente> obtenerDocentesPorEstado(String filtroEstado) {
        if (filtroEstado == null || filtroEstado.equals("TODOS")) {
            return obtenerDocentes();
        }
        
        boolean esActivo = filtroEstado.equals("ACTIVOS");
        try (Connection conn = ConexionDB.getInstance().getConexion()) {
            IDocenteDAO docenteDAO = new DocenteDAOImpl(conn);
            return docenteDAO.listarPorEstado(esActivo);
        } catch (Exception e) {
            System.err.println("Error al filtrar docentes por estado: " + e.getMessage());
            return Collections.emptyList();
        }
    }


    public void registrarDocente(Docente docente) {
        
        validarExistenciaDnis(docente.getDni(), null);

        TransactionRunner.ejecutar(conn -> {
            IDocenteDAO docenteDAO = new DocenteDAOImpl(conn);
            generarYAsignarCodigo(docente, docenteDAO);
            if (!docenteDAO.insertar(docente)) {
                throw new RuntimeException("Error al registrar el docente en la base de datos.");
            }
            return null;
        }, null);
    }

    public void actualizarDocente(Docente docente) {
        validarExistenciaDnis(docente.getDni(), docente.getId());

        TransactionRunner.ejecutar(conn -> {
            IDocenteDAO docenteDAO = new DocenteDAOImpl(conn);
            if (!docenteDAO.actualizar(docente)) {
                throw new RuntimeException("Error al actualizar los datos del docente.");
            }
            return null;
        }, null);
    }

    private void generarYAsignarCodigo(Docente docente, IDocenteDAO docenteDAO) {
        String ultimoCod = docenteDAO.obtenerUltimoCodigo();
        int correlativo = 1;
        
        if (ultimoCod != null && ultimoCod.contains("-")) {
            String[] partes = ultimoCod.split("-");
            if (partes.length == 3) {
                correlativo = Integer.parseInt(partes[2]) + 1;
            }
        }

        docente.generarCodigoDocente(correlativo);
    }
    
    private void validarExistenciaDnis(String dniDocente, Integer idDocente) {
        try (Connection conn = ConexionDB.getInstance().getConexion()) {
            IDocenteDAO docenteDAO = new DocenteDAOImpl(conn);

            if (docenteDAO.existeDni(dniDocente, idDocente)) {
                throw new IllegalArgumentException("El DNI del docente ya está registrado."); 
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en validación de base de datos", e);
        }
    }
    
    public void procesarBajaPorCodigo(String codigoDocente) {
        TransactionRunner.ejecutar(conn -> {
            IDocenteDAO docenteDAO = new DocenteDAOImpl(conn);
            Docente docente = docenteDAO.buscarPorCodigo(codigoDocente);

            if (docente == null) {
                throw new IllegalArgumentException("No se encontró el docente con código: " + codigoDocente);
            }
            if (!docente.isActivo()) {
                throw new IllegalArgumentException("El docente ya se encuentra dado de baja.");
            }
            docente.setActivo(false); 

            if (!docenteDAO.actualizar(docente)) {
                throw new RuntimeException("Error al dar de baja en la base de datos.");
            }            
            return null;
        }, null);
    }

    public void procesarReactivacionPorCodigo(String codigoDocente) {
        TransactionRunner.ejecutar(conn -> {
            IDocenteDAO docenteDAO = new DocenteDAOImpl(conn);
            Docente docente = docenteDAO.buscarPorCodigo(codigoDocente);

            if (docente == null) {
                throw new IllegalArgumentException("No se encontró el docente con código: " + codigoDocente);
            }
            
            if (docente.isActivo()) {
                throw new IllegalArgumentException("El docente ya se encuentra activo.");
            }

            docente.setActivo(true); 

            if (!docenteDAO.actualizar(docente)) {
                throw new RuntimeException("Error al reactivar en la base de datos.");
            }
            return null;
        }, null);
    }
}
