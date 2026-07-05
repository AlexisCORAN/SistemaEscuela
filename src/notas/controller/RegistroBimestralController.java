/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package notas.controller;

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

/**
 *
 * @author Alexis
 */
public class RegistroBimestralController {

    private final IRegistroBimestralDAO registroDAO;
    private final IEvaluacionDAO evaluacionDAO;

    public RegistroBimestralController() {
        this.registroDAO = new RegistroBimestralDAOImpl();
        this.evaluacionDAO = new EvaluacionDAOImpl();
    }

    public List<RegistroBimestral> obtenerRegistros() {
        try {
            return registroDAO.listarTodos();
        } catch (Exception e) {
            System.err.println("Error al listar registros bimestrales: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public RegistroBimestral obtenerRegistroPorId(Integer id) {
        if (id == null) return null;
        return registroDAO.obtenerPorId(id);
    }

    public boolean crearRegistro(RegistroBimestral registro) {
        try {
            return registroDAO.insertar(registro);
        } catch (Exception e) {
            System.err.println("Error al crear cabecera de registro: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarRegistro(RegistroBimestral registro) {
        try {
            return registroDAO.actualizar(registro);
        } catch (Exception e) {
            System.err.println("Error al actualizar cabecera de registro: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarRegistro(RegistroBimestral registro) {
        if (registro == null || registro.getId() == null) return false;
        return registroDAO.eliminar(registro.getId());
    }

    /**
     * Orquesta la carga de las cabeceras e inyecta de manera transparente las evaluaciones individuales.
     */
    public List<RegistroBimestral> cargarNotasCompletasPorMateria(Integer idMatriculaCurso, int bimestre) {
        if (idMatriculaCurso == null) return Collections.emptyList();

        try {
            List<RegistroBimestral> listaCabeceras = registroDAO.listarPorMatriculaCurso(idMatriculaCurso);
            System.out.println("DEBUG: Se encontraron " + listaCabeceras.size() + " registros para el ID MatriculaCurso: " + idMatriculaCurso);

            List<RegistroBimestral> filtrados = new ArrayList<>();

            for (RegistroBimestral rb : listaCabeceras) {
                if (rb.getBimestre().getValorId() == bimestre) { 
                    List<Evaluacion> subNotas = evaluacionDAO.listarPorRegistroBimestral(rb.getId());
                    rb.setEvaluaciones(subNotas);
                    filtrados.add(rb);
                }
            }
            return filtrados;
        } catch (Exception e) {
            System.err.println("ERROR CRÍTICO: " + e.getMessage());
            e.printStackTrace(); 
            return Collections.emptyList();
        }
    }


    public boolean verificarRegistroExistente(Integer idMatriculaCurso, int bimestre) {
        if (idMatriculaCurso == null) return false;
        return registroDAO.verificarExistencia(idMatriculaCurso, bimestre);
    }

    public List<RegistroBimestral> obtenerHistorialPorAlumno(Integer idAlumno) {
        if (idAlumno == null) return Collections.emptyList();
        
        try {
            List<RegistroBimestral> historial = registroDAO.listarHistorialPorAlumno(idAlumno);
            for (RegistroBimestral rb : historial) {
                rb.setEvaluaciones(evaluacionDAO.listarPorRegistroBimestral(rb.getId()));
            }
            return historial;
        } catch (Exception e) {
            System.err.println("Error al recuperar historial del estudiante: " + e.getMessage());
            return Collections.emptyList();
        }
    }

   
    public void procesarCargaAlumnos(PanelNotas vista, Integer idGrado, Integer idCurso, int bimestre) {
        System.out.println("DEBUG: Solicitando carga de notas");
        System.out.println("DEBUG: Parámetros -> Grado: " + idGrado + ", Curso: " + idCurso + ", Bimestre: " + bimestre);

        if (idGrado == null || idCurso == null || bimestre < 1) {
            vista.mostrarMensaje("Datos de filtro inválidos.", "Validación", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        DefaultTableModel modelo = (DefaultTableModel) vista.getTablaNotas().getModel();
        modelo.setRowCount(0);
        vista.getIdsRegistrosCargados().clear();

        String sql = "SELECT rb.idRegistroBimestral, m.codigoMatricula, a.apellidos + ', ' + a.nombres AS alumnoNombre, " +
                     "       MAX(CASE WHEN e.tipo = 'PRACTICA' THEN e.nota ELSE 0 END) as practica, " + 
                     "       MAX(CASE WHEN e.tipo = 'TAREA' THEN e.nota ELSE 0 END) as tarea, " +
                     "       MAX(CASE WHEN e.tipo = 'PARCIAL' THEN e.nota ELSE 0 END) as parcial, " +
                     "       MAX(CASE WHEN e.tipo = 'BIMESTRAL' THEN e.nota ELSE 0 END) as bimestral " +
                     "FROM RegistroBimestral rb " +
                     "INNER JOIN MatriculaCurso mc ON rb.idMatriculaCurso = mc.idMatriculaCurso " +
                     "INNER JOIN Matricula m ON mc.idMatricula = m.idMatricula " +
                     "INNER JOIN Alumno a ON m.idAlumno = a.idAlumno " +
                     "LEFT JOIN Evaluacion e ON rb.idRegistroBimestral = e.idRegistroBimestral " +
                     "WHERE m.idGrado = ? AND mc.idCurso = ? AND rb.bimestre = ? " +
                     "GROUP BY rb.idRegistroBimestral, m.codigoMatricula, a.apellidos, a.nombres";

        try (java.sql.Connection conn = config.ConexionDB.getInstance().getConexion();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idGrado);
            ps.setInt(2, idCurso);
            ps.setInt(3, bimestre);

            try (java.sql.ResultSet rs = ps.executeQuery()) {
                boolean tieneDatos = false;
                while (rs.next()) {
                    tieneDatos = true;
                    int idRegistro = rs.getInt("idRegistroBimestral");
                    
                    double practica = rs.getDouble("practica");
                    double tarea = rs.getDouble("tarea");
                    double parcial = rs.getDouble("parcial");
                    double bimestral = rs.getDouble("bimestral");
                    double promedio = Math.round((practica * 0.2 + tarea * 0.2 + parcial * 0.3 + bimestral * 0.3) * 100.0) / 100.0;

                    modelo.addRow(new Object[] {
                        rs.getString("codigoMatricula"),
                        rs.getString("alumnoNombre"),
                        practica, tarea, parcial, bimestral, promedio
                    });
                    vista.getIdsRegistrosCargados().add(idRegistro);
                }
                
                if (!tieneDatos) {
                    System.out.println("DEBUG: La consulta NO trajo resultados. Verifica los IDs en la DB.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void procesarGeneracionReporte(PanelNotas vista, Integer idGrado, int bimestre) {
        
        if (idGrado == null || bimestre < 1) {
            vista.mostrarMensaje("Seleccione los parámetros de reporte obligatorios.", "Validación", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            DefaultTableModel modeloRiesgo = (DefaultTableModel) vista.getTablaNotasRiesgo().getModel();
            modeloRiesgo.setRowCount(0);

            String sql = "SELECT a.dni, a.apellidos + ', ' + a.nombres AS alumno, c.nombre AS curso, " +
                         "       (MAX(CASE WHEN e.tipo = 'PRACTICA' THEN e.nota ELSE 0 END)*0.2 + " +
                         "        MAX(CASE WHEN e.tipo = 'TAREA' THEN e.nota ELSE 0 END)*0.2 + " +
                         "        MAX(CASE WHEN e.tipo = 'PARCIAL' THEN e.nota ELSE 0 END)*0.3 + " +
                         "        MAX(CASE WHEN e.tipo = 'BIMESTRAL' THEN e.nota ELSE 0 END)*0.3) AS promedio " +
                         "FROM RegistroBimestral rb " +
                         "INNER JOIN MatriculaCurso mc ON rb.idMatriculaCurso = mc.idMatriculaCurso " +
                         "INNER JOIN Matricula m ON mc.idMatricula = m.idMatricula " +
                         "INNER JOIN Alumno a ON m.idAlumno = a.idAlumno " +
                         "INNER JOIN Curso c ON mc.idCurso = c.idCurso " +
                         "LEFT JOIN Evaluacion e ON rb.idRegistroBimestral = e.idRegistroBimestral " +
                         "WHERE m.idGrado = ? AND rb.bimestre = ? " +
                         "GROUP BY a.dni, a.apellidos, a.nombres, c.nombre " +
                         "HAVING (MAX(CASE WHEN e.tipo = 'PRACTICA' THEN e.nota ELSE 0 END)*0.2 + " +
                         "        MAX(CASE WHEN e.tipo = 'TAREA' THEN e.nota ELSE 0 END)*0.2 + " +
                         "        MAX(CASE WHEN e.tipo = 'PARCIAL' THEN e.nota ELSE 0 END)*0.3 + " +
                         "        MAX(CASE WHEN e.tipo = 'BIMESTRAL' THEN e.nota ELSE 0 END)*0.3) < 11.0";

            try (java.sql.Connection conn = config.ConexionDB.getInstance().getConexion();
                 java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, idGrado);
                ps.setInt(2, bimestre);

                try (java.sql.ResultSet rs = ps.executeQuery()) {
                    int cont = 0;
                    while (rs.next()) {
                        modeloRiesgo.addRow(new Object[]{
                            rs.getString("dni"),
                            rs.getString("alumno"),
                            rs.getString("curso"),
                            Math.round(rs.getDouble("promedio") * 100.0) / 100.0,
                            "REQUIERE REFUERZO"
                        });
                        cont++;
                    }
                    vista.mostrarMensaje("Reporte completado. Se hallaron " + cont + " alertas académicas.", "SAD Reportes", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (Exception e) {
            vista.mostrarMensaje("Error al generar el reporte analítico: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }


    public void procesarActualizacionMasivaNotas(List<RegistroBimestral> registrosModificados, PanelNotas vista) {
        if (registrosModificados == null || registrosModificados.isEmpty()) return;

        String sqlUpdate = "UPDATE Evaluacion SET nota = ? WHERE idRegistroBimestral = ? AND tipo = ?";
        try (java.sql.Connection conn = config.ConexionDB.getInstance().getConexion();
             java.sql.PreparedStatement ps = conn.prepareStatement(sqlUpdate)) {
            
            for (RegistroBimestral rb : registrosModificados) {
                for (Evaluacion ev : rb.getEvaluaciones()) {
                    ps.setDouble(1, ev.getNota());
                    ps.setInt(2, rb.getId());
                    ps.setString(3, ev.getTipo().toString());
                    ps.addBatch();
                }
            }
            ps.executeBatch();
            vista.mostrarMensaje("¡Calificaciones guardadas y promedios actualizados con éxito!", "Éxito", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            vista.mostrarMensaje("Error de candado transaccional al guardar: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

 
    private String obtenerNombreAlumnoPorMatriculaCurso(Integer idMatriculaCurso) {
        if (idMatriculaCurso == null) return "Estudiante no identificado";
        String sql = "SELECT a.apellidos + ', ' + a.nombres FROM MatriculaCurso mc " +
                     "INNER JOIN Matricula m ON mc.idMatricula = m.idMatricula " +
                     "INNER JOIN Alumno a ON m.idAlumno = a.idAlumno " +
                     "WHERE mc.idMatriculaCurso = ?";
        try (java.sql.Connection conn = config.ConexionDB.getInstance().getConexion();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMatriculaCurso);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al recuperar nombre del alumno: " + e.getMessage());
        }
        return "Estudiante no identificado";
    }

    private String[] obtenerDetallesAlumnoPorMatriculaCurso(Integer idMatriculaCurso) {
        String[] datos = new String[]{"", "Estudiante no identificado"}; 
        if (idMatriculaCurso == null) return datos;
        String sql = "SELECT a.dni, a.apellidos + ', ' + a.nombres FROM MatriculaCurso mc " +
                     "INNER JOIN Matricula m ON mc.idMatricula = m.idMatricula " +
                     "INNER JOIN Alumno a ON m.idAlumno = a.idAlumno " +
                     "WHERE mc.idMatriculaCurso = ?";
        try (java.sql.Connection conn = config.ConexionDB.getInstance().getConexion();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMatriculaCurso);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    datos[0] = rs.getString(1);
                    datos[1] = rs.getString(2);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al recuperar detalles del alumno para reporte: " + e.getMessage());
        }
        return datos;
    }
}