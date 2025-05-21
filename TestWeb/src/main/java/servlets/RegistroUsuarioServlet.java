package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.mindrot.jbcrypt.BCrypt;
import utils.DatabaseConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet("/registroUsuario")
public class RegistroUsuarioServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(RegistroUsuarioServlet.class);
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombre = request.getParameter("nombre");
        String apellidos = request.getParameter("apellidos");
        String genero = request.getParameter("genero");
        String numeroTelefono = request.getParameter("numeroTelefono");
        String username = request.getParameter("usuario");
        String email = request.getParameter("email");
        String password = request.getParameter("contrasena");
        String confirmPassword = request.getParameter("confirmar_contrasena");

        // Validación de campos
        if (nombre == null || nombre.trim().isEmpty() ||
            apellidos == null || apellidos.trim().isEmpty() ||
            genero == null || genero.trim().isEmpty() ||
            numeroTelefono == null || numeroTelefono.trim().isEmpty() ||
            username == null || username.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {

            logger.warn("Registro fallido: campos vacíos detectados");
            request.setAttribute("error", "Todos los campos son obligatorios.");
            request.getRequestDispatcher("Registro.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            logger.warn("Registro fallido: contraseñas no coinciden para el usuario: " + username);
            request.setAttribute("error", "Las contraseñas no coinciden.");
            request.getRequestDispatcher("Registro.jsp").forward(request, response);
            return;
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (usuarioExiste(conn, username, email)) {
                logger.warn("Intento de registro con usuario/email duplicado: " + username + ", " + email);
                request.setAttribute("error", "El nombre de usuario o email ya está en uso.");
                request.getRequestDispatcher("Registro.jsp").forward(request, response);
                return;
            }

            boolean exito = registrarUsuario(conn, nombre, apellidos, genero, numeroTelefono, username, email, hashedPassword);
            if (exito) {
                logger.info("Nuevo usuario registrado correctamente: " + username);
                request.getSession().setAttribute("registroExitoso", "Usuario registrado correctamente.");
                response.sendRedirect("Registro.jsp");
            } else {
                logger.error("Error al registrar el usuario: " + username);
                request.setAttribute("error", "Error al registrar el usuario.");
                request.getRequestDispatcher("Registro.jsp").forward(request, response);
            }

        } catch (SQLException e) {
            logger.error("Excepción SQL durante el registro del usuario " + username + ": " + e.getMessage());
            request.setAttribute("error", "Error de base de datos: " + e.getMessage());
            request.getRequestDispatcher("Registro.jsp").forward(request, response);
        }
    }

    private boolean usuarioExiste(Connection conn, String username, String email) throws SQLException {
        String sql = "SELECT id FROM usuarios_simplificados WHERE usuario = ? OR email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, email);
            return stmt.executeQuery().next();
        }
    }

    private boolean registrarUsuario(Connection conn, String nombre, String apellidos, String genero,
                                     String numeroTelefono, String username, String email, String hashedPassword)
            throws SQLException {
        String sql = "INSERT INTO usuarios_simplificados (nombre, apellidos, genero, numeroTelefono, usuario, email, contrasena) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            stmt.setString(2, apellidos);
            stmt.setString(3, genero);
            stmt.setString(4, numeroTelefono);
            stmt.setString(5, username);
            stmt.setString(6, email);
            stmt.setString(7, hashedPassword);
            return stmt.executeUpdate() > 0;
        }
    }
}
