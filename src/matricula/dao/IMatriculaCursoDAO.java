/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package matricula.dao;

import java.util.List;
import matricula.model.MatriculaCurso;
import shared.CrudDAO;

/**
 *
 * @author Alexis
 */
public interface IMatriculaCursoDAO extends CrudDAO<MatriculaCurso> {
    void insertarCursosDeMatricula(int idMatricula, List<MatriculaCurso> cursos);
    List<MatriculaCurso> buscarCursosPorMatricula(int idMatricula);
}