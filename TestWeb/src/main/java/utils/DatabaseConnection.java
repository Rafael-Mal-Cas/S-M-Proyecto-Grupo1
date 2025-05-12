package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // 1. Parámetros de conexión
    private static final String URL = "jdbc:mysql://localhost:3306/nombre_bd";
    private static final String USER = "tu_usuario";
    private static final String PASS = "tu_contraseña";
    
    // 2. Bloque estático para registrar el driver (opcional en JDBC 4.0+)
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al cargar el driver JDBC", e);
        }
    }
    
    // 3. Método para obtener conexión
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
    
    // 4. Método para cerrar conexión (sobrecargado para diferentes casos)
    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void close(AutoCloseable... resources) {
        for (AutoCloseable resource : resources) {
            if (resource != null) {
                try {
                    resource.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}