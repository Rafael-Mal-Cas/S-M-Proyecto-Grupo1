package controlador;

import modelo.coche;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/seleccionarVehiculo")
public class SeleccionarVehiculoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("idVehiculo");
        if (idParam == null) {
            response.sendRedirect("catalogo.jsp");
            return;
        }

        int idVehiculo = Integer.parseInt(idParam);

        // Aquí deberías buscar el coche por id en tu fuente de datos.
        // Por simplicidad, voy a simular una búsqueda:
        coche vehiculo = buscarVehiculoPorId(idVehiculo);

        if (vehiculo == null) {
            response.sendRedirect("catalogo.jsp");
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute("vehiculoSeleccionado", vehiculo);

        response.sendRedirect("Informacion_Veiculo.jsp");
    }

    // Simulación de búsqueda - reemplaza por tu lógica real
    private coche buscarVehiculoPorId(int idVehiculo) {
        if (idVehiculo == 1) {
            coche c = new coche();
            c.setId("1");
            c.setMarca("BMW");
            c.setModelo("X5");
            c.setAnio(2023);
            c.setPrecio(65000);
            c.setColor("Negro");
            c.setMotor("V6 Turbo");
            c.setCombustible("Gasolina");
            c.setImagen("Style/default-car.png");
            return c;
        }
        if (idVehiculo == 2) {
            return new coche("2", "Audi", "A4", 2022, "Blanco", 45000, "V4", "Diesel", "Style/audi-a4.png");
        }
        return null;
    }

}
