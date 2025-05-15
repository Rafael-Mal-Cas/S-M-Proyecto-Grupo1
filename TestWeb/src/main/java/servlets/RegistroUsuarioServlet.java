package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;
import utils.DatabaseConnection;

@WebServlet("/RegisterServlet")
public class RegistroUsuarioServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Obtener parámetros del formulario
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String genero = request.getParameter("genero");
        String telefono = request.getParameter("telefono");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // 2. Validaciones básicas
        if (nombre == null || nombre.trim().isEmpty() ||
            apellido == null || apellido.trim().isEmpty() ||
            genero == null || genero.trim().isEmpty() ||
            telefono == null || telefono.trim().isEmpty() ||
            username == null || username.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {

            request.setAttribute("error", "Todos los campos son obligatorios.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Las contraseñas no coinciden.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (usuarioExiste(conn, username, email)) {
                request.setAttribute("error", "El nombre de usuario o email ya está en uso.");
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }

            if (registrarUsuario(conn, nombre, apellido, genero, telefono, username, email, hashedPassword)) {
                response.sendRedirect("login.jsp");
            } else {
                request.setAttribute("error", "Error al registrar el usuario.");
                request.getRequestDispatcher("register.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error de base de datos: " + e.getMessage());
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }

    private boolean usuarioExiste(Connection conn, String username, String email) throws SQLException {
        String sql = "SELECT id FROM usuarios WHERE usuario = ? OR email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, email);
            return stmt.executeQuery().next();
        }
    }

    private boolean registrarUsuario(Connection conn, String nombre, String apellido, String genero,
                                     String telefono, String username, String email, String hashedPassword) 
            throws SQLException {
        String sql = "INSERT INTO usuarios (nombre, apellido, genero, telefono, usuario, email, contrasena, fecha_registro) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            stmt.setString(2, apellido);
            stmt.setString(3, genero);
            stmt.setString(4, telefono);
            stmt.setString(5, username);
            stmt.setString(6, email);
            stmt.setString(7, hashedPassword);
            return stmt.executeUpdate() > 0;
        }
    }
}
