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

@WebServlet("/loginServer")
public class loginServer extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("usuario");
        String password = request.getParameter("clave");

        User usuarioAutenticado = null;

        try (Connection conn = DatabaseConnection.getConnection()) {

            String sql = "SELECT * FROM usuarios_simplificados WHERE usuario = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String hashedPasswordFromDb = rs.getString("contrasena");

                        boolean passwordMatch = false;

                        // Detectar si el hash parece bcrypt (empieza por $2a$, $2b$, $2y$)
                        if (hashedPasswordFromDb != null && hashedPasswordFromDb.startsWith("$2a$") ||
                            hashedPasswordFromDb.startsWith("$2b$") || hashedPasswordFromDb.startsWith("$2y$")) {
                            // Usar BCrypt para validar
                            passwordMatch = BCrypt.checkpw(password, hashedPasswordFromDb);
                        } else {
                            // Contraseña sin encriptar (texto plano) - comparar directamente
                            passwordMatch = password.equals(hashedPasswordFromDb);
                        }

                        if (passwordMatch) {
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
                        }
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
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