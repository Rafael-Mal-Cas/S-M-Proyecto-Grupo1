package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // URL real del servidor MySQL con puerto y base de datos
    private static final String URL = "jdbc:mysql://mysql-b6091e0-sm-a9ae.i.aivencloud.com:27803/defaultdb";
    private static final String USER = "avnadmin";
    private static final String PASS = ""; 
    
    // Parámetros para usar conexión segura con SSL
    private static final String CONNECTION_PARAMS = 
        "?useSSL=true" +
        "&requireSSL=true" +
        "&verifyServerCertificate=true" +
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
