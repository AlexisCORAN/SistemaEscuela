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
import javax.swing.table.DefaultTableModel;
import notas.dao.EvaluacionDAOImpl;
import notas.dao.IEvaluacionDAO;
import notas.dao.IRegistroBimestralDAO;
import notas.dao.RegistroBimestralDAOImpl;
import notas.model.Evaluacion;
import notas.model.RegistroBimestral;
import notas.view.PanelNotas;
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

    public void procesarCargaAlumnos(final PanelNotas vista, final Integer idGrado, final Integer idCurso, final int bimestre) {
        if (idGrado == null || idCurso == null || bimestre < 1) {
            vista.mostrarMensaje("Datos de filtro inválidos.", "Validación", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        final DefaultTableModel modelo = (DefaultTableModel) vista.getTablaNotas().getModel();
        modelo.setRowCount(0);
        vista.getIdsRegistrosCargados().clear();

        try {
            final Connection conn = ConexionDB.getInstance().getConexion();
            final IRegistroBimestralDAO registroDAO = new RegistroBimestralDAOImpl(conn);
            final List<Object[]> filas = registroDAO.listarNotasParaTabla(idGrado, idCurso, bimestre);

            for (final Object[] fila : filas) {
                final int idRegistro = (int) fila[0];
                final String codigoMatricula = (String) fila[1];
                final String alumnoNombre = (String) fila[2];
                final double practica = (double) fila[3];
                final double tarea = (double) fila[4];
                final double parcial = (double) fila[5];
                final double bimestral = (double) fila[6];
                final double promedio = calcularPromedioPonderado(practica, tarea, parcial, bimestral);

                modelo.addRow(new Object[]{codigoMatricula, alumnoNombre, practica, tarea, parcial, bimestral, promedio});
                vista.getIdsRegistrosCargados().add(idRegistro);
            }
        } catch (final Exception e) {
            System.err.println("Error al cargar alumnos para la grilla de notas: " + e.getMessage());
            vista.mostrarMensaje("Error al cargar la grilla de notas: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    public void procesarGeneracionReporte(final PanelNotas vista, final Integer idGrado, final int bimestre) {
        if (idGrado == null || bimestre < 1) {
            vista.mostrarMensaje("Seleccione los parámetros de reporte obligatorios.", "Validación", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            final DefaultTableModel modeloRiesgo = (DefaultTableModel) vista.getTablaNotasRiesgo().getModel();
            modeloRiesgo.setRowCount(0);

            final Connection conn = ConexionDB.getInstance().getConexion();
            final IRegistroBimestralDAO registroDAO = new RegistroBimestralDAOImpl(conn);
            final List<Object[]> riesgo = registroDAO.listarAlumnosEnRiesgo(idGrado, bimestre);

            for (final Object[] fila : riesgo) {
                modeloRiesgo.addRow(new Object[]{
                    fila[0], fila[1], fila[2],
                    Math.round((double) fila[3] * 100.0) / 100.0,
                    "REQUIERE REFUERZO"
                });
            }
            vista.mostrarMensaje("Reporte completado. Se hallaron " + riesgo.size() + " alertas académicas.", "SAD Reportes", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        } catch (final Exception e) {
            vista.mostrarMensaje("Error al generar el reporte analítico: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean procesarActualizacionMasivaNotas(final List<RegistroBimestral> modificados, final PanelNotas vista) {
        if (modificados == null || modificados.isEmpty()) return false;

        Connection conn = null;
        try {
            conn = ConexionDB.getInstance().getConexion();
            conn.setAutoCommit(false);

            final IRegistroBimestralDAO registroDAO = new RegistroBimestralDAOImpl(conn);
            final IEvaluacionDAO evaluacionDAO = new EvaluacionDAOImpl(conn);

            for (final RegistroBimestral cambios : modificados) {
                final RegistroBimestral registroReal = registroDAO.obtenerPorId(cambios.getId());
                if (registroReal == null) continue;

                if (!registroReal.isActivo()) {
                    continue;
                }

                final List<Evaluacion> evaluacionesActuales = evaluacionDAO.listarPorRegistroBimestral(registroReal.getId());
                registroReal.setEvaluaciones(evaluacionesActuales);

                for (final Evaluacion cambio : cambios.getEvaluaciones()) {
                    final Evaluacion existente = buscarPorTipo(evaluacionesActuales, cambio.getTipo());
                    if (existente != null) {
                        existente.setNota(cambio.getNota());
                        evaluacionDAO.actualizar(existente);
                    } else {
                        final Evaluacion nueva = new Evaluacion(null, registroReal, cambio.getTipo().name(),
                                cambio.getTipo(), cambio.getNota(), obtenerPesoPorTipo(cambio.getTipo()));
                        evaluacionDAO.insertarConCabecera(nueva, registroReal.getId());
                    }
                }

              
                registroReal.setEvaluaciones(evaluacionDAO.listarPorRegistroBimestral(registroReal.getId()));
                final double promedio = registroReal.calcularPromedio();
                if (registroReal.getMatriculaCurso() != null) {
                    registroDAO.actualizarNotaFinalMatriculaCurso(registroReal.getMatriculaCurso().getId(), promedio);
                }
            }

            conn.commit();
            vista.mostrarMensaje("¡Calificaciones guardadas y promedios actualizados con éxito!", "Éxito", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            return true;

        } catch (final Exception e) {
            System.err.println("Error transaccional al guardar notas: " + e.getMessage());
            try {
                if (conn != null) conn.rollback();
            } catch (final Exception ex) {
                System.err.println("Error ejecutando el rollback: " + ex.getMessage());
            }
            vista.mostrarMensaje("Error al guardar las calificaciones: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (final Exception ex) {
                System.err.println("Error restaurando autocommit: " + ex.getMessage());
            }
        }
    }

    private Evaluacion buscarPorTipo(final List<Evaluacion> lista, final TipoEvaluacion tipo) {
        for (final Evaluacion e : lista) {
            if (e.getTipo() == tipo) return e;
        }
        return null;
    }

    
    public double obtenerPesoPorTipo(final TipoEvaluacion tipo) {
        switch (tipo) {
            case PRACTICA: return 0.2;
            case TAREA: return 0.2;
            case PARCIAL: return 0.3;
            case BIMESTRAL: return 0.3;
            default: return 0.25;
        }
    }

    private double calcularPromedioPonderado(final double practica, final double tarea, final double parcial, final double bimestral) {
        return Math.round((practica * 0.2 + tarea * 0.2 + parcial * 0.3 + bimestral * 0.3) * 100.0) / 100.0;
    }
}