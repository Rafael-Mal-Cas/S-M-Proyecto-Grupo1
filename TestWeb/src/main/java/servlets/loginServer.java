package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import modelo.User;
import org.mindrot.jbcrypt.BCrypt;
import utils.DatabaseConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet("/loginServer")
public class loginServer extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LogManager.getLogger(loginServer.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Obtener parámetros del formulario
        String usernameOrEmail = request.getParameter("usuario");
        String password = request.getParameter("clave");

        logger.info("Intento de inicio de sesión recibido para: {}", usernameOrEmail);

        User usuarioAutenticado = null;

        try (Connection conn = DatabaseConnection.getConnection()) {

            String sql = "SELECT * FROM usuarios_simplificados WHERE usuario = ? OR email = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, usernameOrEmail);
                stmt.setString(2, usernameOrEmail);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String hashedPasswordFromDb = rs.getString("contrasena");

                        boolean passwordMatch = false;

                        if (hashedPasswordFromDb != null &&
                            (hashedPasswordFromDb.startsWith("$2a$") ||
                             hashedPasswordFromDb.startsWith("$2b$") ||
                             hashedPasswordFromDb.startsWith("$2y$"))) {
                            passwordMatch = BCrypt.checkpw(password, hashedPasswordFromDb);
                        } else {
                            passwordMatch = password.equals(hashedPasswordFromDb);
                        }

                        if (passwordMatch == true) {
                            usuarioAutenticado = new User();
                            usuarioAutenticado.setId(rs.getInt("id"));
                            usuarioAutenticado.setNombre(rs.getString("nombre"));
                            usuarioAutenticado.setApellidos(rs.getString("apellidos"));
                            usuarioAutenticado.setGenero(rs.getString("genero"));
                            usuarioAutenticado.setEmail(rs.getString("email"));
                            usuarioAutenticado.setNumeroTelefono(rs.getString("numeroTelefono"));
                            usuarioAutenticado.setUsuario(rs.getString("usuario"));
                            usuarioAutenticado.setContrasena(hashedPasswordFromDb);
                            usuarioAutenticado.setImagen(rs.getString("imagen"));

                            logger.info("Inicio de sesión exitoso para el usuario: {}", usuarioAutenticado.getUsuario());
                        } else {
                            logger.warn("Contraseña incorrecta para usuario/email: {}", usernameOrEmail);
                        }
                    } else {
                        logger.warn("Usuario o email no encontrado: {}", usernameOrEmail);
                    }
                }
            }

        } catch (SQLException e) {
            logger.error("Error al conectar con la base de datos durante el login de '{}': {}", usernameOrEmail, e.getMessage(), e);
            request.setAttribute("error", "Error de conexión con la base de datos.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        if (usuarioAutenticado != null) {
            HttpSession session = request.getSession();
            session.setAttribute("usuario", usuarioAutenticado);
            session.setAttribute("username", usuarioAutenticado.getUsuario());
            response.sendRedirect("index.jsp");
        } else {
            request.setAttribute("error", "Nombre de usuario o contraseña incorrectos.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
