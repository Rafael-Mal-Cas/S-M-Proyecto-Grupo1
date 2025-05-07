package servlets;

import javax.servlet.*;
import javax.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Esta clase es un listener que se ejecuta automáticamente cuando se inicializa
 * el contexto de la aplicación web. Su propósito es asegurarse de que exista
 * una base mínima de usuarios en la tabla `usuarios` al arrancar la aplicación.
 */
@WebListener
public class InicializadorUsuarios implements ServletContextListener {

    /**
     * Este método se ejecuta al iniciar el contexto de la aplicación.
     * Comprueba si existen usuarios en la base de datos y, si no hay ninguno,
     * inserta tres usuarios por defecto: ana, jose y maria.
     *
     * @param sce Evento del contexto del servlet, no se usa directamente.
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try (Connection conn = utils.DBConnection.getConnection()) {
            // Crear una sentencia SQL para contar cuántos usuarios hay en la tabla
            PreparedStatement checkStmt = conn.prepareStatement(
                "SELECT COUNT(*) AS total FROM testweb.usuarios"
            );
            
            // Ejecutar la consulta
            ResultSet rs = checkStmt.executeQuery();
            int total = 0;

            // Obtener el número total de usuarios existentes
            if (rs.next()) {
                total = rs.getInt("total");
            }

            // Cerrar recursos
            rs.close();
            checkStmt.close();

            // Si no hay usuarios, insertar algunos por defecto
            if (total == 0) {
                // Sentencia SQL para insertar tres usuarios con contraseñas encriptadas (bcrypt)
                String sqlInsert = 
                    "INSERT INTO testweb.usuarios (username, password) VALUES " +
                    "('ana', '$2a$10$Hd3Yt5NfI1XulCF6wZULpOe6PZtJ0vDhS9xfkWOrwWGaY/NuBfG4O'), " +
                    "('jose', '$2a$10$yxPb5OTh9W0T8gDaNkU1aeC9UjAqtKqGUpfTy0T.BAEKfTrbn.z8i'), " +
                    "('maria', '$2a$10$sFz5DdV3bZwv7z5sO6JMpOEzYmKz9W5WbA7a1wMjTfPVzIuGcvE1O');";

                // Ejecutar la inserción
                PreparedStatement insertStmt = conn.prepareStatement(sqlInsert);
                insertStmt.executeUpdate();
                insertStmt.close();
            }

        } catch (Exception e) {
            // Imprimir cualquier error que ocurra durante el proceso de inicialización
            e.printStackTrace();
        }
    }

    /**
     * Este método se ejecuta cuando se destruye el contexto de la aplicación.
     * En este caso, no se necesita realizar ninguna acción.
     *
     * @param sce Evento del contexto del servlet.
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // No se requiere lógica de limpieza al destruir el contexto
    }
}

