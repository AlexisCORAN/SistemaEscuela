/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package alumnos.dao;

import alumnos.model.Apoderado;
import shared.CrudDAO;

/**
 *
 * @author Alexis
 */
public interface IApoderadoDAO extends CrudDAO<Apoderado> {
    boolean existeDni(String dni, Integer idExcluido);
}
