/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package docentes.controller;
import java.util.List;
import docentes.model.Docente;
import docentes.service.DocenteService;
/**
 *
 * @author Alexis
 */

public class DocenteController {

    private final DocenteService docenteService;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(DocenteController.class.getName());

    public DocenteController() {
        this.docenteService = new DocenteService();
    }

    public List<Docente> obtenerDocentes() {
        return docenteService.obtenerDocentes();
    }
    
    public List<Docente> obtenerDocentesPorEstado(String filtroEstado) {
        return docenteService.obtenerDocentesPorEstado(filtroEstado);
    }

    public String registrarDocente(Docente docente) {        
        try {
            docenteService.registrarDocente(docente);
            return null;
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        } catch (RuntimeException e) {
            return "Ocurrió un error en la base de datos: " + e.getMessage();
        }
    }

    public String actualizarDocente(Docente docente) {
        
         try {
            docenteService.actualizarDocente(docente);
            return null;
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        } catch (RuntimeException e) {
            return "Ocurrió un error en la base de datos: " + e.getMessage();
        }
    }
    
    public String procesarBajaPorCodigo(String codigo) {
        try {
            docenteService.procesarBajaPorCodigo(codigo);
            return null; 
        } catch (IllegalArgumentException e) {
            return e.getMessage(); 
        } catch (RuntimeException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error al dar de baja al docente con código " + codigo, e);
            return "Error del sistema: " + e.getMessage();
        }
    }

    public String procesarReactivacionPorCodigo(String codigo) {
        try {
            docenteService.procesarReactivacionPorCodigo(codigo);
            return null; 
        } catch (IllegalArgumentException e) {
            return e.getMessage(); 
        } catch (RuntimeException e) {
            logger.log(java.util.logging.Level.SEVERE, "Error al reactivar al docente con código " + codigo, e);
            return "Error del sistema: " + e.getMessage();
        }
    }
    
    public Docente obtenerDocenteParaEdicion(String codigoDocente) {
        Docente original = docenteService.buscarDocentePorCodigoExacto(codigoDocente);
        return crearCopiaDocente(original);

    }

    private Docente crearCopiaDocente(Docente original) {
        return (original != null) ? new Docente(original) : null;
    }
    
     public List<Docente> buscarDocentesPorCodigoBusqueda(String codigo) {
        Docente docente = docenteService.buscarDocentePorCodigoExacto(codigo);
        if (docente != null) {
            return java.util.Collections.singletonList(docente);
        }
        return java.util.Collections.emptyList();
    }

}