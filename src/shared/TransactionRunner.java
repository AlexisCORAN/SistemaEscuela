/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shared;
import java.sql.Connection;
import java.sql.SQLException;
import config.ConexionDB;
/**
 *
 * @author Alexis
 */
public final class TransactionRunner {

    private TransactionRunner() {}

    @FunctionalInterface
    public interface Operacion<T> {
        T ejecutar(Connection conn);
    }

    public static <T> T ejecutar(Operacion<T> operacion, T valorSiFalla) {
        Connection conn = null;
        try {
            conn = ConexionDB.getInstance().getConexion();
            conn.setAutoCommit(false);

            T resultado = operacion.ejecutar(conn);

            conn.commit();
            return resultado;

        } catch (IllegalArgumentException | IllegalStateException e) {
            rollback(conn);
            throw e;
        } catch (SQLException e) {
            System.err.println("Error transaccional: " + e.getMessage());
            rollback(conn);
            return valorSiFalla;
        } finally {
            cerrarConexion(conn);
        }
    }

    private static void rollback(Connection conn) {
        try { if (conn != null) conn.rollback(); }
        catch (SQLException ex) { System.err.println("Error en rollback: " + ex.getMessage()); }
    }

    private static void cerrarConexion(Connection conn) {
        if (conn != null) {
            try {
                conn.setAutoCommit(true);
                conn.close(); 
            } catch (SQLException ex) {
                System.err.println("Error cerrando la conexión: " + ex.getMessage());
            }
        }
    }
}