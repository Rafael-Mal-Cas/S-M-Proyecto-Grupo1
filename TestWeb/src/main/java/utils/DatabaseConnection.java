package utils;
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
 
/**
* Clase de utilidad para establecer una conexión directa con una base de datos MySQL.
* Usa el driver JDBC y conexión segura mediante SSL.
*/
public class DatabaseConnection {
    /**
     * Método que devuelve una conexión activa a la base de datos.
     * 
     * @return Objeto Connection para ejecutar consultas SQL.
     * @throws SQLException si hay un error al conectarse.
     */
    public static Connection getConnection() throws SQLException {
    	try {
		    Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
		    e.printStackTrace();
		}
		String url = "jdbc:mysql://mysql-b6091e0-sm-a9ae.i.aivencloud.com:27803/defaultdb?ssl-mode=REQUIRED";
		String usuario = "avnadmin";
		String contraseña = "AVNS_uEUTJpymChrFyq_8npz";
		Connection conexion = null;
 
		try {
		    conexion = DriverManager.getConnection(url, usuario, contraseña);
		} catch (SQLException e) {
		    e.printStackTrace();
		}
		return conexion;
    }
}