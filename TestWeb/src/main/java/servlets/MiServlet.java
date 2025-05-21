package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import utils.DatabaseConnection;

@WebServlet("/MiServlet")
public class MiServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Logger con Log4j2 — ajusta el nombre de la clase si quieres
    private static final Logger logger = LogManager.getLogger(MiServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            logger.info("Iniciando doGet - obteniendo conexión...");
            conn = DatabaseConnection.getConnection();

            String sql = "SELECT nombre FROM usuarios_simplificados WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, 1);

            rs = stmt.executeQuery();

            if (rs.next()) {
                String nombre = rs.getString("nombre");
                logger.info("Nombre encontrado: {}", nombre);
                request.setAttribute("miDato", nombre);
            } else {
                logger.warn("No se encontró el usuario con ID = 1");
                request.setAttribute("miDato", "No se encontró el usuario con ID = 1");
            }

        } catch (SQLException e) {
            logger.error("Error al acceder a la base de datos (GET)", e);
            request.setAttribute("error", "Error al acceder a la base de datos");
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
                logger.info("Recursos cerrados correctamente (GET).");
            } catch (SQLException e) {
                logger.error("Error cerrando recursos (GET)", e);
            }
        }

        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        String nuevoNombre = request.getParameter("miDato");
        logger.info("Nuevo nombre recibido desde formulario: {}", nuevoNombre);

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
                logger.info("Nombre actualizado correctamente.");
                request.setAttribute("miDato", nuevoNombre);
            } else {
                logger.warn("No se pudo actualizar el usuario.");
                request.setAttribute("error", "No se pudo actualizar el usuario");
            }

        } catch (SQLException e) {
            logger.error("Error al guardar en la base de datos (POST)", e);
            request.setAttribute("error", "Error al guardar en la base de datos");
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
                logger.info("Recursos cerrados correctamente (POST).");
            } catch (SQLException e) {
                logger.error("Error cerrando recursos (POST)", e);
            }
        }

        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}
