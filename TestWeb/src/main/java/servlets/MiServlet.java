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
import utils.DatabaseConnection;

@WebServlet("/MiServlet")
public class MiServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();

            // Obtener el nombre del usuario con ID = 1
            String sql = "SELECT nombre FROM usuarios WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, 1);

            rs = stmt.executeQuery();

            if (rs.next()) {
                String nombre = rs.getString("nombre");
                request.setAttribute("miDato", nombre);
            } else {
                request.setAttribute("miDato", "No se encontró el usuario con ID = 1");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al acceder a la base de datos");
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        String nuevoNombre = request.getParameter("miDato");
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();

            String sql = "UPDATE usuarios SET nombre = ? WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, nuevoNombre);
            stmt.setInt(2, 1);

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                request.setAttribute("miDato", nuevoNombre);
            } else {
                request.setAttribute("error", "No se pudo actualizar el usuario");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al guardar en la base de datos");
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}
