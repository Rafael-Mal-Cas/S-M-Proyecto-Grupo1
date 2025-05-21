package servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.DatabaseConnection;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet("/exportCatalogo")
public class ExportCatalogoCSV extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(ExportCatalogoCSV.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        logger.info("Inicio de exportación de catálogo a CSV");

        resp.setContentType("text/csv; charset=UTF-8");
        resp.setHeader("Content-Disposition", "attachment; filename=\"BMW_catalogo.csv\"");

        try (PrintWriter out = resp.getWriter();
             Connection conn = DatabaseConnection.getConnection();
             PreparedStatement st = conn.prepareStatement(
                 "SELECT marca, modelo, anio, color, motor, combustible, precio, imagen " +
                 "FROM bmw_cars ORDER BY id"
             );
             ResultSet rs = st.executeQuery()) {

            logger.info("Consulta SQL ejecutada correctamente");

            // Cabecera CSV
            out.println("marca,modelo,anio,color,motor,combustible,precio,imagen");

            int registros = 0;
            while (rs.next()) {
                out.printf("%s,%s,%d,%s,%s,%s,%.2f,%s%n",
                        rs.getString("marca"),
                        rs.getString("modelo"),
                        rs.getInt("anio"),
                        rs.getString("color"),
                        rs.getString("motor"),
                        rs.getString("combustible"),
                        rs.getDouble("precio"),
                        rs.getString("imagen"));
                registros++;
            }

            logger.info("Exportación completada. Registros exportados: {}", registros);

        } catch (SQLException e) {
            logger.error("Error al exportar el catálogo a CSV", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                           "Error exportando CSV");
        }
    }
}
