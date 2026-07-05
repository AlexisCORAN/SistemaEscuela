/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package docentes.dao;
import java.util.List;
import docentes.model.Docente;
import shared.CrudDAO;
/**
 *
 * @author Alexis
 */
public interface IDocenteDAO extends CrudDAO<Docente>{
    String obtenerUltimoCodigo();
    Docente buscarPorCodigo(String codigo);
    List<Docente> listarPorEspecialidad(String especialidadAcademica);
    List<Docente> listarActivos();

}
