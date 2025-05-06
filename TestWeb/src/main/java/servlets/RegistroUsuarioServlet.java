package servlets;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;

public class RegistroUsuarioServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        String usuario = request.getParameter("usuario");
        String clave = request.getParameter("clave");

        // Hashear la contraseña antes de almacenarla
        String hashedPassword = BCrypt.hashpw(clave, BCrypt.gensalt());

        Connection con = null;
        PreparedStatement ps = null;

        try {
            // 1. Cargar driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. Conexión a la base de datos
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/TesWeb", "usuario", "hashedPassword");

            // 3. Preparar la consulta para insertar el nuevo usuario
            ps = con.prepareStatement("INSERT INTO usuarios (usuario, clave) VALUES (?, ?)");
            ps.setString(1, usuario);
            ps.setString(2, hashedPassword);

            // 4. Ejecutar la inserción
            ps.executeUpdate();

            // Redirigir al usuario a la página de inicio de sesión (o donde desees)
            response.sendRedirect("login.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp"); // Redirigir a una página de error si ocurre algo
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
