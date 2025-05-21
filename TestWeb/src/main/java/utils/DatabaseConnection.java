package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Clase de utilidad para establecer una conexión directa con una base de datos MySQL.
 * Usa el driver JDBC y conexión segura mediante SSL.
 */
public class DatabaseConnection {

    private static final Logger logger = LogManager.getLogger(DatabaseConnection.class);

    /**
     * Método que devuelve una conexión activa a la base de datos.
     * 
     * @return Objeto Connection para ejecutar consultas SQL.
     * @throws SQLException si hay un error al conectarse.
     */
    public static Connection getConnection() throws SQLException {
        try {
            logger.debug("Cargando driver JDBC MySQL");
            Class.forName("com.mysql.cj.jdbc.Driver");
            logger.debug("Driver JDBC MySQL cargado correctamente");
        } catch (ClassNotFoundException e) {
            logger.error("Error al cargar el driver JDBC MySQL", e);
            // Aunque no se lanza SQLException, puedes considerar propagar o manejar el error aquí
        }

        String url = "jdbc:mysql://mysql-b6091e0-sm-a9ae.i.aivencloud.com:27803/defaultdb?ssl-mode=REQUIRED";
        String usuario = "avnadmin";
        String contraseña = "AVNS_uEUTJpymChrFyq_8npz";

        Connection conexion = null;

        try {
            logger.info("Intentando conexión a base de datos: " + url);
            conexion = DriverManager.getConnection(url, usuario, contraseña);
            logger.info("Conexión a base de datos establecida correctamente");
        } catch (SQLException e) {
            logger.error("Error al establecer conexión con la base de datos", e);
            throw e; // Propagamos la excepción para que sea manejada por quien llama
        }

        return conexion;
    }
}
