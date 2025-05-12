package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Configuración para BD externa
    private static final String URL = "jdbc:mysql://[IP_O_HOST_EXTERNO]:3306/[NOMBRE_BD]";
    private static final String USER = "[USUARIO_EXTERNO]";
    private static final String PASS = "[CONTRASEÑA_EXTERNA]";
    
    // Parámetros adicionales recomendados para conexión externa
    private static final String CONNECTION_PARAMS = 
        "?useSSL=true" +
        "&allowPublicKeyRetrieval=true" +
        "&autoReconnect=true" +
        "&connectTimeout=5000" +
        "&socketTimeout=30000";
    
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error al cargar el driver JDBC", e);
        }
    }
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL + CONNECTION_PARAMS, USER, PASS);
    }
}