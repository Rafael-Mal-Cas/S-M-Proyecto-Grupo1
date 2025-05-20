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


@WebServlet("/exportCatalogo")
public class ExportCatalogoCSV extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        resp.setContentType("text/csv; charset=UTF-8");
        resp.setHeader("Content-Disposition", "attachment; filename=\"BMW_catalogo.csv\"");

        try (PrintWriter out = resp.getWriter();
             Connection conn = DatabaseConnection.getConnection();
             PreparedStatement st = conn.prepareStatement(
                 "SELECT marca, modelo, anio, color, motor, combustible, precio, imagen " +
                 "FROM bmw_cars ORDER BY id"
             );
             ResultSet rs = st.executeQuery()) {

            // Cabecera CSV
            out.println("marca,modelo,anio,color,motor,combustible,precio,imagen");

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
            }

        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                           "Error exportando CSV");
            e.printStackTrace();
        }
    }
}
