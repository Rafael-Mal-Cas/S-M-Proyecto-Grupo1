package servlets;

import utils.DBConnection;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Servlet que gestiona el proceso de inicio de sesión de los usuarios.
 * Se activa cuando se envía un formulario a la ruta "/loginServer".
 */
@WebServlet("/loginServer")
public class loginServer extends HttpServlet {

    /**
     * Este método se ejecuta cuando se realiza una petición POST al servlet,
     * normalmente desde un formulario de login.
     *
     * @param request  Objeto que contiene los datos enviados por el cliente.
     * @param response Objeto para enviar respuestas al cliente.
     * @throws ServletException En caso de error con el servlet.
     * @throws IOException      En caso de error de entrada/salida.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Obtener el nombre de usuario y la contraseña desde el formulario
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try (Connection conn = DBConnection.getConnection()) {
            // Consulta SQL para obtener el hash de la contraseña del usuario ingresado
            String sql = "SELECT password FROM testweb.usuarios WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Recuperar el hash almacenado de la base de datos
                String storedHash = rs.getString("password");

                // Comparar la contraseña ingresada con el hash usando BCrypt
                if (BCrypt.checkpw(password, storedHash)) {
                    // Si la verificación es correcta, crear sesión para el usuario
                    HttpSession session = request.getSession();
                    session.setAttribute("username", username);

                    // Redirigir al usuario a la página final
                    response.sendRedirect("Final.jsp");
                } else {
                    // Contraseña incorrecta: volver al login con mensaje de error
                    request.setAttribute("error", "Credenciales incorrectas");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                }
            } else {
                // Usuario no encontrado en la base de datos
                request.setAttribute("error", "Usuario no encontrado");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            // En caso de error con la base de datos, redirigir a una página de error
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
