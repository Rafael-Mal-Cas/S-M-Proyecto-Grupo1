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
import utils.DatabaseConnection;

@WebServlet("/loginServer")
public class loginServer extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("usuario");
        String password = request.getParameter("clave");

        User usuarioAutenticado = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM usuarios WHERE usuario = ? AND contrasena = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            rs = stmt.executeQuery();

            if (rs.next()) {
                usuarioAutenticado = new User();
                usuarioAutenticado.setId(rs.getInt("id"));
                usuarioAutenticado.setNombre(rs.getString("nombre"));
                usuarioAutenticado.setApellidos(rs.getString("apellidos"));
                usuarioAutenticado.setGenero(rs.getString("genero"));
                usuarioAutenticado.setEmail(rs.getString("email"));
                usuarioAutenticado.setNumeroTelefono(rs.getString("numeroTelefono"));
                usuarioAutenticado.setUsuario(rs.getString("usuario"));
                usuarioAutenticado.setContrasena(rs.getString("contrasena"));
                usuarioAutenticado.setImagen(rs.getString("imagen")); // si lo usas en el futuro
            }

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error de conexión con la base de datos.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
