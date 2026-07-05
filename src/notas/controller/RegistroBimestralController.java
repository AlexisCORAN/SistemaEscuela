/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package notas.controller;

import config.ConexionDB;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import notas.dao.EvaluacionDAOImpl;
import notas.dao.IEvaluacionDAO;
import notas.dao.IRegistroBimestralDAO;
import notas.dao.RegistroBimestralDAOImpl;
import notas.model.Evaluacion;
import notas.model.RegistroBimestral;
import shared.TipoEvaluacion;

/**
 *
 * @author Alexis
 */
public class RegistroBimestralController {

    public RegistroBimestralController() {
    }

    public List<RegistroBimestral> obtenerRegistros() {
        try {
            final Connection conn = ConexionDB.getInstance().getConexion();
            final IRegistroBimestralDAO registroDAO = new RegistroBimestralDAOImpl(conn);
            return registroDAO.listarTodos();
        } catch (final Exception e) {
            System.err.println("Error al listar registros bimestrales: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public RegistroBimestral obtenerRegistroPorId(final Integer id) {
        if (id == null) return null;
        try {
            final Connection conn = ConexionDB.getInstance().getConexion();
            final IRegistroBimestralDAO registroDAO = new RegistroBimestralDAOImpl(conn);
            return registroDAO.obtenerPorId(id);
        } catch (final Exception e) {
            System.err.println("Error al obtener registro por id: " + e.getMessage());
            return null;
        }
    }

    public boolean crearRegistro(final RegistroBimestral registro) {
        try {
            final Connection conn = ConexionDB.getInstance().getConexion();
            final IRegistroBimestralDAO registroDAO = new RegistroBimestralDAOImpl(conn);
            return registroDAO.insertar(registro);
        } catch (final Exception e) {
            System.err.println("Error al crear cabecera de registro: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarRegistro(final RegistroBimestral registro) {
        try {
            final Connection conn = ConexionDB.getInstance().getConexion();
            final IRegistroBimestralDAO registroDAO = new RegistroBimestralDAOImpl(conn);
            return registroDAO.actualizar(registro);
        } catch (final Exception e) {
            System.err.println("Error al actualizar cabecera de registro: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarRegistro(final RegistroBimestral registro) {
        if (registro == null || registro.getId() == null) return false;

        Connection conn = null;
        try {
            conn = ConexionDB.getInstance().getConexion();
            conn.setAutoCommit(false);

            final IRegistroBimestralDAO registroDAO = new RegistroBimestralDAOImpl(conn);
            final boolean eliminado = registroDAO.eliminar(registro.getId());

            if (!eliminado) {
                conn.rollback();
                return false;
            }

            conn.commit();
            return true;
        } catch (final Exception e) {
            System.err.println("Error transaccional al eliminar registro: " + e.getMessage());
            try {
                if (conn != null) conn.rollback();
            } catch (final Exception ex) {
                System.err.println("Error ejecutando el rollback: " + ex.getMessage());
            }
            return false;
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (final Exception ex) {
                System.err.println("Error restaurando autocommit: " + ex.getMessage());
            }
        }
    }

    public List<RegistroBimestral> cargarNotasCompletasPorMateria(final Integer idMatriculaCurso, final int bimestre) {
        if (idMatriculaCurso == null) return Collections.emptyList();

        try {
            final Connection conn = ConexionDB.getInstance().getConexion();
            final IRegistroBimestralDAO registroDAO = new RegistroBimestralDAOImpl(conn);
            final IEvaluacionDAO evaluacionDAO = new EvaluacionDAOImpl(conn);

            final List<RegistroBimestral> listaCabeceras = registroDAO.listarPorMatriculaCurso(idMatriculaCurso);
            final List<RegistroBimestral> filtrados = new ArrayList<>();

            for (final RegistroBimestral rb : listaCabeceras) {
                if (rb.getBimestre().getValorId() == bimestre) {
                    final List<Evaluacion> subNotas = evaluacionDAO.listarPorRegistroBimestral(rb.getId());
                    rb.setEvaluaciones(subNotas);
                    filtrados.add(rb);
                }
            }
            return filtrados;
        } catch (final Exception e) {
            System.err.println("Error crítico al cargar notas completas por materia: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public boolean verificarRegistroExistente(final Integer idMatriculaCurso, final int bimestre) {
        if (idMatriculaCurso == null) return false;
        try {
            final Connection conn = ConexionDB.getInstance().getConexion();
            final IRegistroBimestralDAO registroDAO = new RegistroBimestralDAOImpl(conn);
            return registroDAO.verificarExistencia(idMatriculaCurso, bimestre);
        } catch (final Exception e) {
            System.err.println("Error al verificar existencia de registro: " + e.getMessage());
            return false;
        }
    }

    public List<RegistroBimestral> obtenerHistorialPorAlumno(final Integer idAlumno) {
        if (idAlumno == null) return Collections.emptyList();

        try {
            final Connection conn = ConexionDB.getInstance().getConexion();
            final IRegistroBimestralDAO registroDAO = new RegistroBimestralDAOImpl(conn);
            final IEvaluacionDAO evaluacionDAO = new EvaluacionDAOImpl(conn);

            final List<RegistroBimestral> historial = registroDAO.listarHistorialPorAlumno(idAlumno);
            for (final RegistroBimestral rb : historial) {
                rb.setEvaluaciones(evaluacionDAO.listarPorRegistroBimestral(rb.getId()));
            }
            return historial;
        } catch (final Exception e) {
            System.err.println("Error al recuperar historial del estudiante: " + e.getMessage());
            return Collections.emptyList();
        }
    }

   
    public List<Object[]> obtenerNotasParaGrilla(final Integer idGrado, final Integer idCurso, final int bimestre) throws Exception {
        if (idGrado == null || idCurso == null || bimestre < 1) {
            throw new IllegalArgumentException("Datos de filtro inválidos para la carga de alumnos.");
        }

        final Connection conn = ConexionDB.getInstance().getConexion();
        final IRegistroBimestralDAO registroDAO = new RegistroBimestralDAOImpl(conn);
        final List<Object[]> filasBD = registroDAO.listarNotasParaTabla(idGrado, idCurso, bimestre);

        final List<Object[]> filasProcesadas = new ArrayList<>();
        for (final Object[] fila : filasBD) {
            final double practica = (double) fila[3];
            final double tarea = (double) fila[4];
            final double parcial = (double) fila[5];
            final double bimestral = (double) fila[6];
            
            final double promedio = RegistroBimestral.calcularPromedioEstatico(practica, tarea, parcial, bimestral);

            filasProcesadas.add(new Object[]{
                fila[0], 
                fila[1], 
                fila[2],
                practica, tarea, parcial, bimestral, promedio
            });
        }
        return filasProcesadas;
    }

    public List<Object[]> obtenerReporteRiesgo(final Integer idGrado, final int bimestre) throws Exception {
        if (idGrado == null || bimestre < 1) {
            throw new IllegalArgumentException("Seleccione los parámetros de reporte obligatorios.");
        }

        final Connection conn = ConexionDB.getInstance().getConexion();
        final IRegistroBimestralDAO registroDAO = new RegistroBimestralDAOImpl(conn);
        final List<Object[]> riesgoBD = registroDAO.listarAlumnosEnRiesgo(idGrado, bimestre);

        final List<Object[]> filasReporte = new ArrayList<>();
        for (final Object[] fila : riesgoBD) {
            filasReporte.add(new Object[]{
                fila[0], fila[1], fila[2],
                Math.round((double) fila[3] * 100.0) / 100.0,
                "REQUIERE REFUERZO"
            });
        }
        return filasReporte;
    }

    public List<Evaluacion> obtenerEvaluacionesPorRegistro(final Integer idRegistroBimestral) throws Exception {
        if (idRegistroBimestral == null) return Collections.emptyList();
        
        final Connection conn = ConexionDB.getInstance().getConexion();
        final IEvaluacionDAO evaluacionDAO = new EvaluacionDAOImpl(conn);
        return evaluacionDAO.listarPorRegistroBimestral(idRegistroBimestral);
    }
    
    public void procesarActualizacionMasivaNotas(final List<RegistroBimestral> modificados) throws Exception {
        if (modificados == null || modificados.isEmpty()) {
            throw new IllegalArgumentException("No hay datos para guardar.");
        }

        Connection conn = null;
        try {
            conn = ConexionDB.getInstance().getConexion();
            conn.setAutoCommit(false); // Inicia transacción

            final IRegistroBimestralDAO registroDAO = new RegistroBimestralDAOImpl(conn);
            final IEvaluacionDAO evaluacionDAO = new EvaluacionDAOImpl(conn);

            for (final RegistroBimestral cambios : modificados) {
                final RegistroBimestral registroReal = registroDAO.obtenerPorId(cambios.getId());
                
                if (registroReal != null && registroReal.isActivo()) {
                    final List<Evaluacion> evaluacionesActuales = evaluacionDAO.listarPorRegistroBimestral(registroReal.getId());
                    registroReal.setEvaluaciones(evaluacionesActuales);

                    for (final Evaluacion cambio : cambios.getEvaluaciones()) {
                        final Evaluacion existente = buscarPorTipo(evaluacionesActuales, cambio.getTipo());
                        if (existente != null) {
                            existente.setNota(cambio.getNota());
                            evaluacionDAO.actualizar(existente);
                        } else {
                            final Evaluacion nueva = new Evaluacion(null, registroReal, cambio.getTipo().name(),
                                    cambio.getTipo(), cambio.getNota(), cambio.getTipo().getPeso());
                            evaluacionDAO.insertarConCabecera(nueva, registroReal.getId());
                        }
                    }

                    registroReal.setEvaluaciones(evaluacionDAO.listarPorRegistroBimestral(registroReal.getId()));
                    final double promedio = registroReal.calcularPromedio();
                    if (registroReal.getMatriculaCurso() != null) {
                        registroDAO.actualizarNotaFinalMatriculaCurso(registroReal.getMatriculaCurso().getId(), promedio);
                    }
                }
            }

            conn.commit(); 
        } catch (final Exception e) {
            if (conn != null) conn.rollback(); 
            throw new Exception("Error transaccional al guardar notas masivas: " + e.getMessage());
        } finally {
            if (conn != null) conn.setAutoCommit(true);
        }
    }

    public void guardarEvaluacionesDetalle(final Integer idRegistro, final List<Evaluacion> evaluacionesModificadas) throws Exception {
        if (idRegistro == null || evaluacionesModificadas == null || evaluacionesModificadas.isEmpty()) {
            throw new IllegalArgumentException("Datos insuficientes para guardar.");
        }

        Connection conn = null;
        try {
            conn = ConexionDB.getInstance().getConexion();
            conn.setAutoCommit(false); 

            final IRegistroBimestralDAO registroDAO = new RegistroBimestralDAOImpl(conn);
            final IEvaluacionDAO evaluacionDAO = new EvaluacionDAOImpl(conn);

            final RegistroBimestral registroReal = registroDAO.obtenerPorId(idRegistro);
            if (registroReal == null || !registroReal.isActivo()) {
                throw new IllegalStateException("El registro está cerrado o no existe.");
            }

            final List<Evaluacion> evaluacionesActuales = evaluacionDAO.listarPorRegistroBimestral(idRegistro);
            
            for (final Evaluacion modificada : evaluacionesModificadas) {
                final Evaluacion existente = buscarPorTipo(evaluacionesActuales, modificada.getTipo());
                if (existente != null) {
                    existente.setNota(modificada.getNota());
                    evaluacionDAO.actualizar(existente);
                } else {
                    final Evaluacion nueva = new Evaluacion(null, registroReal, modificada.getTipo().name(),
                            modificada.getTipo(), modificada.getNota(), modificada.getTipo().getPeso()); // Extrae el peso del Enum
                    evaluacionDAO.insertarConCabecera(nueva, idRegistro);
                }
            }

            registroReal.setEvaluaciones(evaluacionDAO.listarPorRegistroBimestral(idRegistro));
            final double nuevoPromedio = registroReal.calcularPromedio(); // Llama al método del Dominio
            
            if (registroReal.getMatriculaCurso() != null) {
                registroDAO.actualizarNotaFinalMatriculaCurso(registroReal.getMatriculaCurso().getId(), nuevoPromedio);
            }

            conn.commit(); 
        } catch (final Exception e) {
            if (conn != null) conn.rollback(); 
            throw new Exception("Error transaccional al guardar las notas: " + e.getMessage());
        } finally {
            if (conn != null) conn.setAutoCommit(true);
        }
    }

    private Evaluacion buscarPorTipo(final List<Evaluacion> lista, final TipoEvaluacion tipo) {
        for (final Evaluacion e : lista) {
            if (e.getTipo() == tipo) return e;
        }
        return null;
    }
}