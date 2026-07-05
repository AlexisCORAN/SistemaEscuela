/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package notas.dao;

import java.util.List;
import notas.model.Evaluacion;
import shared.CrudDAO;

/**
 *
 * @author Alexis
 */
public interface IEvaluacionDAO extends CrudDAO<Evaluacion> {
    
    
    List<Evaluacion> listarPorRegistroBimestral(Integer idRegistroBimestral);
    boolean insertarConCabecera(Evaluacion entidad, Integer idRegistroBimestral);
}