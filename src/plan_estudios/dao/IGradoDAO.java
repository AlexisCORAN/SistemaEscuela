/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package plan_estudios.dao;
import java.util.List;
import plan_estudios.model.Grado;
import shared.CrudDAO;

/**
 *
 * @author Alexis
 */
public interface IGradoDAO extends CrudDAO<Grado> {

    Grado buscarPorNombreYNivel(String nombre, String nivel);

    List<Grado> listarPorNivel(String nivel);
}