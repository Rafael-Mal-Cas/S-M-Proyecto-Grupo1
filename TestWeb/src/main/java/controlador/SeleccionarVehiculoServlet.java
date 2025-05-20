package controlador;

import modelo.catalogo;
import modelo.coche;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/seleccionarVehiculo")
public class SeleccionarVehiculoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("idVehiculo");
        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect("catalogo");
            return;
        }

        int idVehiculo;
        try {
            idVehiculo = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            response.sendRedirect("catalogo");
            return;
        }

        // Cargar catálogo desde CSV para buscar el coche
        catalogo catalogoBMW = new catalogo();
        String ruta = getServletContext().getRealPath("/WEB-INF/BMW_catalogo.csv");
        catalogoBMW.cargarDesdeCSV(ruta);
        ArrayList<coche> coches = catalogoBMW.getCoches();

        coche vehiculoSeleccionado = null;
        for (coche c : coches) {
            try {
                if (Integer.parseInt(c.getId()) == idVehiculo) {
                    vehiculoSeleccionado = c;
                    break;
                }
            } catch (NumberFormatException ignored) {
            }
        }

        if (vehiculoSeleccionado == null) {
            response.sendRedirect("catalogo");
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute("vehiculoSeleccionado", vehiculoSeleccionado);

        response.sendRedirect("Informacion_Veiculo.jsp");
    }
}
