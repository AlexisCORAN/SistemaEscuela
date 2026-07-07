/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package matricula.dao;
import java.util.List;
import matricula.model.Matricula;
import shared.CrudDAO;
/**
 *
 * @author Alexis
 */
public interface IMatriculaDAO extends CrudDAO<Matricula> {
    Matricula obtenerMatriculaActiva(String codigoEstudiante, int anioEscolar);
    List<Matricula> listarPorGradoAnio(int idGrado, int anioEscolar);
    boolean anularMatricula(Integer idMatricula);
    String obtenerUltimoCodigo();
    List<Matricula> buscarPorCriterioMatricula(String criterio);
    List<Matricula> listarPorEstado(String estadoDb);
    Integer obtenerIdMatriculaCurso(String codigoMatricula, Integer idCurso);
    
}
