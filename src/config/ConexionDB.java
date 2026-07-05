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
    private Connection conexion;
    private static boolean alertaMostrada = false;
    
    private final String url = "jdbc:sqlserver://localhost:1433;"
                             + "databaseName=ColegioDB;"
                             + "user=sa;"
                             + "password=TestingBaseDatosUniversidad123#;"
                             + "encrypt=true;"
                             + "trustServerCertificate=true;";

    private ConexionDB() {
        try {
            this.conexion = DriverManager.getConnection(url);
            System.out.println("¡Conexión exitosa a ColegioDB en Docker!");
            alertaMostrada = false; 
            
        } catch (SQLException e) {
            this.conexion = null;

            if (!alertaMostrada) {
                alertaMostrada = true; // Marcamos que ya se mostró una vez
                
                JOptionPane.showMessageDialog(null, """
                                                    No se pudo conectar a la base de datos SQL Server.
                                                    El programa iniciara en el modo desconectado (ciertas funciones no estaran disponibles).              
                                                   """, 
                    "Error de Conexión", 
                    JOptionPane.WARNING_MESSAGE
                );
            }
        }
    }

    public static synchronized ConexionDB getInstance() {
        if (instancia == null || estaDesconectado()) {
            instancia = new ConexionDB();
        }
        return instancia;
    }

    private static boolean estaDesconectado() {
        if (instancia == null) {
            return true;
        }
        try {
            return instancia.conexion == null || instancia.conexion.isClosed();
        } catch (SQLException e) {
            return true;
        }
    }

    public Connection getConexion() {
        return conexion;
    }
}
