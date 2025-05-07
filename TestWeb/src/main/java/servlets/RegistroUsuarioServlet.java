package servlets;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Servlet que gestiona el registro de nuevos usuarios.
 * Recibe datos desde un formulario (POST), encripta la contraseña y la guarda en la base de datos.
 */
public class RegistroUsuarioServlet extends HttpServlet {

    /**
     * Este método se ejecuta cuando se recibe una petición POST desde un formulario de registro.
     * Toma el nombre de usuario y la contraseña, la encripta con BCrypt y guarda el nuevo usuario
     * en la base de datos.
     *
     * @param request  Petición HTTP con los datos del formulario.
     * @param response Respuesta HTTP al cliente.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        // Obtener parámetros enviados desde el formulario
        String usuario = request.getParameter("usuario");
        String clave = request.getParameter("clave");

        // Encriptar la contraseña usando BCrypt antes de almacenarla
        String hashedPassword = BCrypt.hashpw(clave, BCrypt.gensalt());

        Connection con = null;
        PreparedStatement ps = null;

        try {
            // 1. Cargar el driver JDBC de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. Establecer la conexión con la base de datos
            // NOTA: ¡Reemplaza "usuario" y "hashedPassword" por tu usuario y contraseña reales!
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/TesWeb", "usuario", "hashedPassword"
            );

            // 3. Crear la consulta SQL para insertar el nuevo usuario
            ps = con.prepareStatement(
                "INSERT INTO usuarios (usuario, clave) VALUES (?, ?)"
            );
            ps.setString(1, usuario);
            ps.setString(2, hashedPassword);

            // 4. Ejecutar la inserción
            ps.executeUpdate();

            // 5. Redirigir al login tras el registro exitoso
            response.sendRedirect("login.jsp");

        } catch (Exception e) {
            // En caso de error, imprimir traza y redirigir a una página de error
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        } finally {
            // Cerrar recursos para evitar fugas de memoria
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
