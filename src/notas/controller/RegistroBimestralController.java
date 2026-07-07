/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package notas.controller;
import java.util.List;
import notas.model.Evaluacion;
import notas.model.RegistroBimestral;
import notas.service.RegistroBimestralService;


/**
 *
 * @author Alexis
 */
public class RegistroBimestralController {

    private final RegistroBimestralService notasService;

    public RegistroBimestralController() {
        this.notasService = new RegistroBimestralService();
    }

    public RegistroBimestral obtenerRegistroPorId(final Integer id) {
        if (id == null) return null;
        try {
            return notasService.obtenerRegistroPorId(id);
        } catch (final Exception e) {
            System.err.println("Error al obtener registro por id: " + e.getMessage());
            return null;
        }
    }

    public List<Object[]> obtenerNotasParaGrilla(final Integer idGrado, final Integer idCurso, final int bimestre) throws Exception {
        return notasService.obtenerNotasParaGrilla(idGrado, idCurso, bimestre);
    }

    public List<Object[]> obtenerReporteRiesgo(final Integer idGrado, final int bimestre) throws Exception {
        return notasService.obtenerReporteRiesgo(idGrado, bimestre);
    }

    public List<Evaluacion> obtenerEvaluacionesPorRegistro(final Integer idRegistroBimestral) throws Exception {
        return notasService.obtenerEvaluacionesPorRegistro(idRegistroBimestral);
    }

    public void guardarEvaluacionesDetalle(final Integer idRegistro, final List<Evaluacion> evaluacionesModificadas) throws Exception {
        notasService.guardarEvaluacionesDetalle(idRegistro, evaluacionesModificadas);
    }


    public void cerrarBimestre(final Integer idRegistro) throws Exception {
        notasService.cerrarBimestre(idRegistro);
    }
    
    public void eliminarEvaluacion(final Integer idEvaluacion, final Integer idRegistro) throws Exception {
        notasService.eliminarEvaluacion(idEvaluacion, idRegistro);
    }
}