/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package plan_estudios.dao;
import java.util.List;
import plan_estudios.model.Curso;
import shared.CrudDAO;

/**
 *
 * @author Alexis
 */
public interface ICursoDAO extends CrudDAO<Curso>{
    String obtenerUltimoCodigo();
    Curso buscarPorCodigo(String codigoCurso);
    List<Curso> listarPorGrado(Integer idGrado);
    List<Curso> listarPorDocente(Integer idDocente);
    Curso buscarPorNombre(String nombre);
}
