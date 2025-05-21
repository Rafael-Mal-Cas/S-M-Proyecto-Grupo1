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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebListener
public class InicializadorUsuarios implements ServletContextListener {

    private static final Logger logger = LogManager.getLogger(InicializadorUsuarios.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("Inicialización del contexto: cargando usuarios desde base de datos");
        cargarUsuariosDesdeBD(sce);
        logger.info("Carga de usuarios completada");
    }

    private void cargarUsuariosDesdeBD(ServletContextEvent sce) {
        List<User> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios_simplificados";

        try (Connection conn = utils.DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            logger.info("Ejecutando consulta SQL para cargar usuarios");

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

            logger.info("Usuarios cargados en contexto. Total usuarios: {}", usuarios.size());

        } catch (SQLException e) {
            logger.error("Error al cargar usuarios desde BD externa", e);
            throw new RuntimeException("Error al cargar usuarios desde BD externa", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Destruyendo contexto: limpiando atributos de usuarios");
        sce.getServletContext().removeAttribute("usuarios");
        sce.getServletContext().removeAttribute("idCounter");
    }
}
