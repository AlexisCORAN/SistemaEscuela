/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;
/**
 *
 * @author Alexis
 */
public class ConexionDB {

    private static ConexionDB instancia;
    private static boolean alertaMostrada = false;

    private final String url = "jdbc:sqlserver://localhost:1433;"
                             + "databaseName=ColegioDB;"
                             + "user=sa;"
                             + "password=TestingBaseDatosUniversidad123#;"
                             + "encrypt=true;"
                             + "trustServerCertificate=true;";

    private ConexionDB() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Driver de SQL Server no encontrado.");
        }
    }

    public static synchronized ConexionDB getInstance() {
        if (instancia == null) {
            instancia = new ConexionDB();
        }
        return instancia;
    }


    public Connection getConexion() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(url);
            alertaMostrada = false; 
            return conn;
        } catch (SQLException e) {
            if (!alertaMostrada) {
                alertaMostrada = true;
                JOptionPane.showMessageDialog(null, """
                                                    No se pudo conectar a la base de datos SQL Server.
                                                    El programa no podrá guardar cambios.""", 
                    "Error de Conexión", 
                    JOptionPane.ERROR_MESSAGE);
            }
            throw e; 
        }
    }
}
