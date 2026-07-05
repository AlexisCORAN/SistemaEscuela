/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package docentes.controller;
import java.sql.Connection;
import java.util.List;
import java.util.Collections;
import docentes.dao.IDocenteDAO;
import docentes.dao.DocenteDAOImpl;
import docentes.model.Docente;
import config.ConexionDB;
/**
 *
 * @author Alexis
 */

public class DocenteController {

    public DocenteController() {
    }

    public List<Docente> obtenerDocentes() {
        try {
            final Connection conn = ConexionDB.getInstance().getConexion();
            final IDocenteDAO docenteDAO = new DocenteDAOImpl(conn);
            return docenteDAO.listarTodos();
        } catch (final Exception e) {
            System.err.println("Error al obtener docentes: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<Docente> buscarPorCodigo(final String codigo) {
        if (codigo == null || codigo.trim().isEmpty() || codigo.equals("Buscar por Código")) {
            return obtenerDocentes();
        }
        try {
            final Connection conn = ConexionDB.getInstance().getConexion();
            final IDocenteDAO docenteDAO = new DocenteDAOImpl(conn);
            final Docente doc = docenteDAO.buscarPorCodigo(codigo.trim());
            return doc != null ? List.of(doc) : Collections.emptyList();
        } catch (final Exception e) {
            System.err.println("Error al buscar docente: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public boolean registrarDocente(final Docente docente) {
        try {
            if (docente.getDni().length() != 8) {
                throw new IllegalArgumentException("El DNI debe tener exactamente 8 dígitos.");
            }
            
            final Connection conn = ConexionDB.getInstance().getConexion();
            final IDocenteDAO docenteDAO = new DocenteDAOImpl(conn);
            
            final String ultimoCod = docenteDAO.obtenerUltimoCodigo();
            int correlativo = 1;
            if (ultimoCod != null && ultimoCod.contains("-")) {
                final String[] partes = ultimoCod.split("-");
                if (partes.length == 3) {
                    correlativo = Integer.parseInt(partes[2]) + 1;
                }
            }

            docente.generarCodigoDocente(correlativo);
            docente.setActivo(true);
            
            return docenteDAO.insertar(docente);
        } catch (final Exception e) {
            System.err.println("Error al registrar docente: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarDocente(final Docente docente) {
        try {
            if (docente.getDni().length() != 8) {
                throw new IllegalArgumentException("El DNI debe tener exactamente 8 dígitos.");
            }
            final Connection conn = ConexionDB.getInstance().getConexion();
            final IDocenteDAO docenteDAO = new DocenteDAOImpl(conn);
            return docenteDAO.actualizar(docente);
        } catch (final Exception e) {
            System.err.println("Error al actualizar docente: " + e.getMessage());
            return false;
        }
    }

    public boolean procesarBajaDocente(final Docente docente) {
        if (docente == null) return false;
        try {
                final Connection conn = ConexionDB.getInstance().getConexion();
            final IDocenteDAO docenteDAO = new DocenteDAOImpl(conn);
            docente.setActivo(false);
            return docenteDAO.actualizar(docente);
        } catch (final Exception e) {
            System.err.println("Error al procesar baja: " + e.getMessage());
            return false;
        }
    }
}