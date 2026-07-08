/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package notas.service;
import config.ConexionDB;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.sql.SQLException;
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

public class RegistroBimestralService {

    public RegistroBimestralService() {
    }

    public RegistroBimestral obtenerRegistroPorId(Integer id) throws Exception {
        if (id == null) return null;

        try (Connection conn = ConexionDB.getInstance().getConexion()) {
            IRegistroBimestralDAO registroDAO = new RegistroBimestralDAOImpl(conn);
            return registroDAO.obtenerPorId(id);
        } catch (SQLException e) {
            throw new Exception("Error al obtener el registro por ID", e);
        }
    }

    public List<Object[]> obtenerNotasParaGrilla(Integer idGrado, Integer idCurso, int bimestre) throws Exception {
        if (idGrado == null || idCurso == null || bimestre < 1) {
            throw new IllegalArgumentException("Datos de filtro inválidos.");
        }

        try (Connection conn = ConexionDB.getInstance().getConexion()) {
            IRegistroBimestralDAO registroDAO = new RegistroBimestralDAOImpl(conn);
            List<Object[]> filasBD = registroDAO.listarNotasParaTabla(idGrado, idCurso, bimestre);

            if (filasBD.isEmpty()) return new ArrayList<>();

            return procesarFilasParaGrilla(filasBD);
        } catch (SQLException e) {
            throw new Exception("Error al obtener notas para la grilla", e);
        }
    }

    private List<Object[]> procesarFilasParaGrilla(List<Object[]> filasBD) {
        List<Object[]> filasProcesadas = new ArrayList<>();

        List<Integer> idsProcesados = new ArrayList<>();
        for (Object[] fila : filasBD) {
            Integer idReg = (Integer) fila[0];
            if (!idsProcesados.contains(idReg)) {
                idsProcesados.add(idReg);
            }
        }

        for (Integer idReg : idsProcesados) {
            String codigoMatricula = "";
            String alumnoNombre = "";
            String estado = "ABIERTO"; 
            List<Evaluacion> evalsAlumno = new ArrayList<>();

            for (Object[] fila : filasBD) {
                if (idReg.equals(fila[0])) {
                    codigoMatricula = (String) fila[1];
                    alumnoNombre = (String) fila[2];
                    estado = (String) fila[5]; 

                    String tipoStr = (String) fila[3];
                    Double nota = (Double) fila[4];

                    if (tipoStr != null && !"SIN NOTAS".equals(tipoStr) && nota != null) {
                        TipoEvaluacion tipoEnum = TipoEvaluacion.valueOf(tipoStr.trim().toUpperCase());
                        evalsAlumno.add(new Evaluacion(null, null, "Nota " + tipoEnum.name(), tipoEnum, nota, tipoEnum.getPeso()));
                    }
                }
            }

            RegistroBimestral rbTemp = new RegistroBimestral();
            rbTemp.setId(idReg); 

            double promPractica = rbTemp.obtenerNotaConsolidadaPorTipo(TipoEvaluacion.PRACTICA, evalsAlumno);
            double promTarea = rbTemp.obtenerNotaConsolidadaPorTipo(TipoEvaluacion.TAREA, evalsAlumno);
            double parcial = rbTemp.obtenerNotaConsolidadaPorTipo(TipoEvaluacion.PARCIAL, evalsAlumno);
            double bimestral = rbTemp.obtenerNotaConsolidadaPorTipo(TipoEvaluacion.BIMESTRAL, evalsAlumno);
            double promedioFinal = rbTemp.calcularPromedio(evalsAlumno);

            filasProcesadas.add(new Object[]{
                idReg,             
                codigoMatricula,   
                alumnoNombre,      
                promPractica,      
                promTarea,         
                parcial,           
                bimestral,         
                promedioFinal,     
                estado             
            });
        }

        return filasProcesadas;
    }

    public void guardarEvaluacionesDetalle(Integer idRegistro, List<Evaluacion> evaluacionesModificadas) throws Exception {
        if (idRegistro == null || idRegistro == 0) {
            throw new IllegalArgumentException("No se puede guardar notas en un registro bimestral inexistente o con ID 0.");
        }
        if (evaluacionesModificadas == null || evaluacionesModificadas.isEmpty()) {
            throw new IllegalArgumentException("Datos insuficientes para guardar.");
        }

        try (Connection conn = ConexionDB.getInstance().getConexion()) {
            conn.setAutoCommit(false);

            try {
                IRegistroBimestralDAO registroDAO = new RegistroBimestralDAOImpl(conn);
                IEvaluacionDAO evaluacionDAO = new EvaluacionDAOImpl(conn);

                RegistroBimestral registroReal = registroDAO.obtenerPorId(idRegistro);
                if (registroReal == null) throw new IllegalStateException("El registro especificado no existe.");
                if (!registroReal.isActivo()) throw new IllegalStateException("El bimestre está cerrado.");

                for (Evaluacion modificada : evaluacionesModificadas) {
                    if (modificada.getId() != null) {
                        evaluacionDAO.actualizar(modificada);
                    } else {
                        evaluacionDAO.insertarConCabecera(modificada, idRegistro);
                    }
                }

                List<Evaluacion> listaActualizada = evaluacionDAO.listarPorRegistroBimestral(idRegistro);
                double nuevoPromedio = registroReal.calcularPromedio(listaActualizada);

                if (registroReal.getMatriculaCurso() != null) {
                    registroDAO.actualizarNotaFinalMatriculaCurso(registroReal.getMatriculaCurso().getId(), nuevoPromedio);
                }

                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw new Exception("Error transaccional al guardar las notas: " + e.getMessage(), e);
            }
        }
    }

    public void eliminarEvaluacion(Integer idEvaluacion, Integer idRegistro) throws Exception {
        if (idEvaluacion == null || idRegistro == null) {
            throw new IllegalArgumentException("IDs inválidos para la eliminación.");
        }

        try (Connection conn = ConexionDB.getInstance().getConexion()) {
            conn.setAutoCommit(false);

            try {
                IRegistroBimestralDAO registroDAO = new RegistroBimestralDAOImpl(conn);
                IEvaluacionDAO evaluacionDAO = new EvaluacionDAOImpl(conn);

                RegistroBimestral registroReal = registroDAO.obtenerPorId(idRegistro);

                if (registroReal == null) {
                    throw new IllegalStateException("El registro especificado no existe.");
                }

                if (!registroReal.isActivo()) {
                    throw new IllegalStateException("No se pueden eliminar notas de un bimestre cerrado.");
                }

                evaluacionDAO.eliminar(idEvaluacion);

                List<Evaluacion> listaActualizada = evaluacionDAO.listarPorRegistroBimestral(idRegistro);
                double nuevoPromedio = registroReal.calcularPromedio(listaActualizada);

                if (registroReal.getMatriculaCurso() != null) {
                    registroDAO.actualizarNotaFinalMatriculaCurso(registroReal.getMatriculaCurso().getId(), nuevoPromedio);
                }

                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw new Exception("Error al eliminar la evaluación: " + e.getMessage(), e);
            }
        } 
    }

    public List<Evaluacion> obtenerEvaluacionesPorRegistro(Integer idRegistroBimestral) throws Exception {
        if (idRegistroBimestral == null) {
            return Collections.emptyList();
        }

        try (Connection conn = ConexionDB.getInstance().getConexion()) {
            IEvaluacionDAO evaluacionDAO = new EvaluacionDAOImpl(conn);
            return evaluacionDAO.listarPorRegistroBimestral(idRegistroBimestral);
        } catch (SQLException e) {
            throw new Exception("Error al obtener las evaluaciones del registro: " + idRegistroBimestral, e);
        }
    }

    
    public void cerrarBimestre(Integer idRegistro) throws Exception {
        if (idRegistro == null) {
            throw new IllegalArgumentException("ID de registro no proporcionado.");
        }

        try (Connection conn = ConexionDB.getInstance().getConexion()) {
            conn.setAutoCommit(false);

            try {
                IRegistroBimestralDAO registroDAO = new RegistroBimestralDAOImpl(conn);
                RegistroBimestral registroReal = registroDAO.obtenerPorId(idRegistro);

                if (registroReal != null) {
                    registroReal.cerrarRegistro();
                    registroDAO.actualizar(registroReal);
                }

                conn.commit(); 
            } catch (Exception e) {
                conn.rollback();
                throw new Exception("Error al cerrar el bimestre: " + e.getMessage(), e);
            }
        }
    }
    

    public List<Object[]> obtenerReporteRiesgo(Integer idGrado, int bimestre) throws Exception {
        if (idGrado == null || bimestre < 1) {
            throw new IllegalArgumentException("Parámetros obligatorios faltantes.");
        }

        try (Connection conn = ConexionDB.getInstance().getConexion()) {
            IRegistroBimestralDAO registroDAO = new RegistroBimestralDAOImpl(conn);
            List<Object[]> riesgoBD = registroDAO.listarAlumnosEnRiesgo(idGrado, bimestre);

            List<Object[]> filasReporte = new ArrayList<>();
            for (Object[] fila : riesgoBD) {
                filasReporte.add(new Object[]{
                    fila[0], fila[1], fila[2],
                    Math.round((double) fila[3] * 100.0) / 100.0,
                    "REQUIERE REFUERZO"
                });
            }
            return filasReporte;
        } catch (SQLException e) {
            throw new Exception("Error al generar el reporte de riesgo: " + e.getMessage(), e);
        }
    }
    
    
}