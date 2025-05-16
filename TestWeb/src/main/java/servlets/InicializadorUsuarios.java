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

@WebListener
public class InicializadorUsuarios implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        cargarUsuariosDesdeBD(sce);
    }

    private void cargarUsuariosDesdeBD(ServletContextEvent sce) {
        List<User> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios_simplificados";

        try (Connection conn = utils.DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setNombre(rs.getString("nombre"));
                user.setApellidos(rs.getString("apellidos"));
                user.setGenero(rs.getString("genero"));
                user.setEmail(rs.getString("email"));
                user.setNumeroTelefono(rs.getString("numeroTelefono"));
                user.setUsuario(rs.getString("usuario"));
                user.setContrasena(rs.getString("contrasena"));
                user.setImagen(rs.getString("imagen"));
                usuarios.add(user);
            }

            sce.getServletContext().setAttribute("usuarios", usuarios);

            int maxId = usuarios.stream()
                    .mapToInt(User::getId)
                    .max()
                    .orElse(0);
            sce.getServletContext().setAttribute("idCounter", maxId + 1);

        } catch (SQLException e) {
            throw new RuntimeException("Error al cargar usuarios desde BD externa", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        sce.getServletContext().removeAttribute("usuarios");
        sce.getServletContext().removeAttribute("idCounter");
    }
}
