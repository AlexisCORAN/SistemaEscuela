/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package notas.dao;
import java.util.List;
import notas.model.RegistroBimestral;
import shared.CrudDAO;
/**
 *
 * @author Alexis
 */
public interface IRegistroBimestralDAO extends CrudDAO<RegistroBimestral> {

    List<RegistroBimestral> listarPorMatriculaCurso(Integer idMatriculaCurso);
    boolean verificarExistencia(Integer idMatriculaCurso, int bimestre);
    List<RegistroBimestral> listarHistorialPorAlumno(Integer idAlumno);
    boolean actualizarNotaFinalMatriculaCurso(Integer idMatriculaCurso, double notaFinal);
    List<Object[]> listarNotasParaTabla(int idGrado, int idCurso, int bimestre);
    List<Object[]> listarAlumnosEnRiesgo(int idGrado, int bimestre);
}