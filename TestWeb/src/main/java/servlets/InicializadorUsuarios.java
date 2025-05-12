package servlets;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import modelo.User;

@SuppressWarnings("unused")
@WebListener
public class InicializadorUsuarios implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Solo necesitamos cargar los usuarios existentes desde la BD
        cargarUsuariosDesdeBD(sce);
    }

    private void cargarUsuariosDesdeBD(ServletContextEvent sce) {
        List<User> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios"; // Ajusta según tu esquema de BD
        
        try (Connection conn = utils.DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                User user = new User(
                    rs.getInt("id"),
                    rs.getString("nombre"),       // Ajusta nombres de columnas
                    rs.getString("apellido"),    // según tu esquema de BD
                    rs.getString("genero"),
                    rs.getString("email"),
                    rs.getString("telefono"),
                    rs.getString("username"),
                    rs.getString("password")
                );
                usuarios.add(user);
            }
            
            // Almacenar en el contexto de la aplicación
            sce.getServletContext().setAttribute("usuarios", usuarios);
            
            // Configurar contador de IDs (usando el máximo ID existente + 1)
            int maxId = usuarios.stream()
                              .mapToInt(User::getId)
                              .max()
                              .orElse(0);
            sce.getServletContext().setAttribute("idCounter", maxId + 1);
            
        } catch (SQLException e) {
            // Manejo mejorado de errores
            throw new RuntimeException("Error al cargar usuarios desde BD externa", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Limpiar recursos
        sce.getServletContext().removeAttribute("usuarios");
        sce.getServletContext().removeAttribute("idCounter");
    }
}