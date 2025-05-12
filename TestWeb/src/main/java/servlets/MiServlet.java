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
            // Obtener conexión a la base de datos
            conn = DatabaseConnection.getConnection();
            
            // Consulta para obtener datos
            String sql = "SELECT dato FROM mi_tabla WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, 1); // Ejemplo con ID fijo, puedes parametrizarlo
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                String datoDesdeDB = rs.getString("dato");
                request.setAttribute("miDato", datoDesdeDB);
            } else {
                request.setAttribute("miDato", "No se encontraron datos");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al acceder a la base de datos");
        } finally {
            // Cerrar recursos
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
        
        String miDato = request.getParameter("miDato");
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            // Obtener conexión
            conn = DatabaseConnection.getConnection();
            
            // Actualizar dato en la base de datos
            String sql = "UPDATE mi_tabla SET dato = ? WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, miDato + " Modificado y guardado en DB");
            stmt.setInt(2, 1); // ID del registro a actualizar
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                request.setAttribute("miDato", miDato + " Modificado y guardado en DB");
            } else {
                request.setAttribute("error", "No se pudo actualizar el registro");
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