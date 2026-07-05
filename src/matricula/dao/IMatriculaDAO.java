/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package matricula.dao;
import java.util.List;
import matricula.model.Matricula;
import plan_estudios.model.Grado;
import shared.CrudDAO;
/**
 *
 * @author Alexis
 */
public interface IMatriculaDAO extends CrudDAO<Matricula> {
    Matricula obtenerMatriculaActiva(String codigoEstudiante, int añoEscolar);
    List<Matricula> listarPorGradoAño(int idGrado, int añoEscolar);
    List<Grado> listarGradosConCursos();
    boolean anularMatricula(Integer idMatricula);
    String obtenerUltimoCodigo();
}
