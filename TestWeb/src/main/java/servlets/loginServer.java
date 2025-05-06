package servlets;

import utils.DBConnection;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;

@WebServlet("/loginServer")
public class loginServer extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT password FROM testweb.usuarios WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password");

                // Verificar la contraseña utilizando BCrypt
                if (BCrypt.checkpw(password, storedHash)) {
                    // Login exitoso: guardar usuario en la sesión
                    HttpSession session = request.getSession();
                    session.setAttribute("username", username);

                    // Redirigir a Final.jsp
                    response.sendRedirect("Final.jsp");
                } else {
                    // Contraseña incorrecta
                    request.setAttribute("error", "Credenciales incorrectas");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                }
            } else {
                // Usuario no encontrado
                request.setAttribute("error", "Usuario no encontrado");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
