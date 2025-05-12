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
            // 1. Obtener conexión a la base de datos
            conn = DatabaseConnection.getConnection();
            
            // 2. Preparar consulta SQL para buscar el usuario
            String sql = "SELECT * FROM usuarios WHERE usuario = ? AND contrasena = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            // 3. Ejecutar consulta
            rs = stmt.executeQuery();
            
            // 4. Si hay resultados, el usuario existe y las credenciales son correctas
            if (rs.next()) {
                usuarioAutenticado = new User();
                usuarioAutenticado.setUsuario(rs.getString("usuario"));
                usuarioAutenticado.setContrasena(rs.getString("contrasena"));
                // Agrega aquí otros campos que necesites del usuario
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error de conexión con la base de datos.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        } finally {
            // 5. Cerrar recursos
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (usuarioAutenticado != null) {
            // Autenticación exitosa
            HttpSession session = request.getSession();
            session.setAttribute("usuario", usuarioAutenticado);
            session.setAttribute("username", usuarioAutenticado.getUsuario());
            
            // Redirigir a página principal
            response.sendRedirect("index.jsp");
        } else {
            // Autenticación fallida
            request.setAttribute("error", "Nombre de usuario o contraseña incorrectos.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}