package controlador;

import modelo.catalogo;
import modelo.coche;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet("/seleccionarVehiculo")
public class SeleccionarVehiculoServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(SeleccionarVehiculoServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("idVehiculo");
        logger.info("Parámetro recibido idVehiculo: {}", idParam);

        if (idParam == null || idParam.isEmpty()) {
            logger.warn("Parámetro idVehiculo vacío o nulo, redirigiendo a catalogo.");
            response.sendRedirect("catalogo");
            return;
        }

        int idVehiculo;
        try {
            idVehiculo = Integer.parseInt(idParam);
            logger.info("idVehiculo convertido a entero: {}", idVehiculo);
        } catch (NumberFormatException e) {
            logger.error("Error convirtiendo idVehiculo a entero: {}", idParam, e);
            response.sendRedirect("catalogo");
            return;
        }

        catalogo catalogoBMW = new catalogo();
        String ruta = getServletContext().getRealPath("/WEB-INF/BMW_catalogo.csv");
        logger.info("Cargando catálogo desde ruta: {}", ruta);
        catalogoBMW.cargarDesdeCSV(ruta);
        ArrayList<coche> coches = catalogoBMW.getCoches();

        coche vehiculoSeleccionado = null;
        for (coche c : coches) {
            try {
                if (Integer.parseInt(c.getId()) == idVehiculo) {
                    vehiculoSeleccionado = c;
                    logger.info("Vehículo seleccionado: id={}, modelo={}", c.getId(), c.getModelo());
                    break;
                }
            } catch (NumberFormatException ignored) {
                logger.warn("Id de coche no es número: {}", c.getId());
            }
        }

        if (vehiculoSeleccionado == null) {
            logger.warn("No se encontró vehículo con id: {}, redirigiendo a catalogo.", idVehiculo);
            response.sendRedirect("catalogo");
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute("vehiculoSeleccionado", vehiculoSeleccionado);
        logger.info("Vehículo almacenado en sesión.");

        response.sendRedirect("Informacion_Veiculo.jsp");
    }
}
