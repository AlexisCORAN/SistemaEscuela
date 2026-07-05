/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package alumnos.dao;
import java.util.List;
import alumnos.model.Alumno;
import shared.CrudDAO;

/**
 *
 * @author Alexis
 */

public interface IAlumnoDAO extends CrudDAO<Alumno> {
    Alumno buscarPorCodigo(String codigoEstudiante);
    String obtenerUltimoCodigo();
    List<Alumno> listarPorEstado(boolean activo);
    boolean existeDni(String dni, Integer idExcluido);
}