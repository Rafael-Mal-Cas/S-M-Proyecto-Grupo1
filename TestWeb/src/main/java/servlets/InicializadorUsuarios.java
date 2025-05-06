package servlets;

import javax.servlet.*;
import javax.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebListener
public class InicializadorUsuarios implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try (Connection conn = utils.DBConnection.getConnection()) {
            // Comprobar si ya existen usuarios
            PreparedStatement checkStmt = conn.prepareStatement(
                "SELECT COUNT(*) AS total FROM testweb.usuarios"
            );
            ResultSet rs = checkStmt.executeQuery();
            int total = 0;
            if (rs.next()) {
                total = rs.getInt("total");
            }
            rs.close();
            checkStmt.close();

            // Insertar usuarios si no existen
            if (total == 0) {
                String sqlInsert = 
                    "INSERT INTO testweb.usuarios (username, password) VALUES " +
                    "('ana', '$2a$10$Hd3Yt5NfI1XulCF6wZULpOe6PZtJ0vDhS9xfkWOrwWGaY/NuBfG4O'), " +
                    "('jose', '$2a$10$yxPb5OTh9W0T8gDaNkU1aeC9UjAqtKqGUpfTy0T.BAEKfTrbn.z8i'), " +
                    "('maria', '$2a$10$sFz5DdV3bZwv7z5sO6JMpOEzYmKz9W5WbA7a1wMjTfPVzIuGcvE1O');";

                PreparedStatement insertStmt = conn.prepareStatement(sqlInsert);
                insertStmt.executeUpdate();
                insertStmt.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // No necesitas hacer nada aquí
    }
}
