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

        // Recogemos el parámetro 'usuario' del formulario,
        // este campo servirá tanto para el nombre de usuario como para el email
        String usernameOrEmail = request.getParameter("usuario");
        String password = request.getParameter("clave");

        User usuarioAutenticado = null;

        try (Connection conn = DatabaseConnection.getConnection()) {

            // La consulta busca en la tabla usuarios_simplificados
            // donde coincida el usuario O el email con lo que nos pase el usuario
            String sql = "SELECT * FROM usuarios_simplificados WHERE usuario = ? OR email = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                // Seteamos el mismo parámetro para usuario y email (se permite login con cualquiera)
                stmt.setString(1, usernameOrEmail);
                stmt.setString(2, usernameOrEmail);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        // Obtenemos la contraseña almacenada (hasheada o en texto plano)
                        String hashedPasswordFromDb = rs.getString("contrasena");

                        boolean passwordMatch = false;

                        // Comprobamos si la contraseña en BD está en formato bcrypt
                        // Los hashes bcrypt empiezan por $2a$, $2b$ o $2y$
                        if (hashedPasswordFromDb != null && 
                            (hashedPasswordFromDb.startsWith("$2a$") || 
                             hashedPasswordFromDb.startsWith("$2b$") || 
                             hashedPasswordFromDb.startsWith("$2y$"))) {
                            // Si está hasheada, usamos BCrypt para validar la contraseña
                            passwordMatch = BCrypt.checkpw(password, hashedPasswordFromDb);
                        } else {
                            // Si la contraseña está en texto plano (no recomendado),
                            // comparamos directamente las cadenas
                            passwordMatch = password.equals(hashedPasswordFromDb);
                        }

                        if (passwordMatch) {
                            // Creamos el objeto User con los datos recuperados para usar en la sesión
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
            // Capturamos errores de base de datos y mostramos mensaje en login.jsp
            e.printStackTrace();
            request.setAttribute("error", "Error de conexión con la base de datos.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        if (usuarioAutenticado != null) {
            // Si el login es correcto, creamos sesión y guardamos el usuario autenticado
            HttpSession session = request.getSession();
            session.setAttribute("usuario", usuarioAutenticado);
            session.setAttribute("username", usuarioAutenticado.getUsuario());
            // Redirigimos a la página principal
            response.sendRedirect("index.jsp");
        } else {
            // Si no se autentica, mostramos mensaje de error en login.jsp
            request.setAttribute("error", "Nombre de usuario o contraseña incorrectos.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
