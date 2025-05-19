package servlets;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import utils.DatabaseConnection;

@WebServlet("/comprarCoche")
public class CompraServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // ───────────── UTILIDAD DE “PAGO TEST” ─────────────
    /**
     * Simula un gateway de pago. Devuelve true = pago aceptado, false = fallido.
     * Puedes hacerla siempre true cuando pases a “modo producción”.
     */
    private boolean procesarPagoDePrueba(String metodo, int cantidadEnCentimos, String datosTarjetaEtc) {
        // Lógica de test: 80 % de probabilidades de éxito
        return new Random().nextInt(10) < 8;
    }

    // ─────────────  DO POST ─────────────
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Validar sesión
        HttpSession session = request.getSession(false);
        Integer idUsuario = (session != null) ? (Integer) session.getAttribute("idUsuario") : null;
        if (idUsuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // 2. Obtener parámetros del formulario
        int idCoche;
        int precio;            // en € (o en céntimos si prefieres)
        String metodoPago;

        try {
            idCoche    = Integer.parseInt(request.getParameter("idCoche"));
            precio     = Integer.parseInt(request.getParameter("precio"));
            metodoPago = request.getParameter("metodoPago"); // "tarjeta", "paypal", "transferencia"
        } catch (Exception e) {
            request.setAttribute("error", "Datos de compra inválidos.");
            request.getRequestDispatcher("catalogo.jsp").forward(request, response);
            return;
        }

        // 3. Reglas de negocio
        try (Connection conn = DatabaseConnection.getConnection()) {

            // 3a. ¿Existe el coche?
            if (!existeCoche(conn, idCoche)) {
                request.setAttribute("error", "El coche seleccionado no existe.");
                request.getRequestDispatcher("catalogo.jsp").forward(request, response);
                return;
            }

            // 3b. ¿Ya está vendido?
            if (cocheVendido(conn, idCoche)) {
                request.setAttribute("error", "Este coche ya ha sido vendido.");
                request.getRequestDispatcher("catalogo.jsp").forward(request, response);
                return;
            }

            // 3c. ¿Usuario ya compró un coche antes?
            if (usuarioYaTieneCoche(conn, idUsuario)) {
                request.setAttribute("error", "Solo se permite un coche por usuario.");
                request.getRequestDispatcher("catalogo.jsp").forward(request, response);
                return;
            }

            // 4. Simular pago (modo prueba)
            boolean pagoOk = procesarPagoDePrueba(metodoPago, precio * 100, "dummy-data");
            if (!pagoOk) {
                request.setAttribute("error", "Pago rechazado por el gateway de prueba.");
                request.getRequestDispatcher("catalogo.jsp").forward(request, response);
                return;
            }

            // 5. Registrar compra en BBDD
            if (registrarCompra(conn, idUsuario, idCoche, precio, metodoPago)) {
                // Guardar en sesión para mostrar en Cuenta.jsp
                session.setAttribute("cocheCompradoId", idCoche);
                session.setAttribute("compraOk", "¡Compra realizada con éxito!");

                response.sendRedirect("Cuenta.jsp");
            } else {
                request.setAttribute("error", "No se pudo registrar la compra.");
                request.getRequestDispatcher("catalogo.jsp").forward(request, response);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error de base de datos.");
            request.getRequestDispatcher("catalogo.jsp").forward(request, response);
        }
    }

    // ─────────────  MÉTODOS AUXILIARES  ─────────────
    private boolean existeCoche(Connection conn, int idCoche) throws SQLException {
        String sql = "SELECT 1 FROM coches WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCoche);
            return ps.executeQuery().next();
        }
    }

    private boolean cocheVendido(Connection conn, int idCoche) throws SQLException {
        String sql = "SELECT 1 FROM compras WHERE id_coche = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCoche);
            return ps.executeQuery().next();
        }
    }

    private boolean usuarioYaTieneCoche(Connection conn, int idUsuario) throws SQLException {
        String sql = "SELECT 1 FROM compras WHERE id_usuario = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            return ps.executeQuery().next();
        }
    }

    private boolean registrarCompra(Connection conn, int idUsuario, int idCoche,
                                    int precio, String metodoPago) throws SQLException {

        String sql = "INSERT INTO compras (id_usuario, id_coche, fecha_compra, precio, metodo_pago) "
                   + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setInt(2, idCoche);
            ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(4, precio);
            ps.setString(5, metodoPago);
            return ps.executeUpdate() == 1;
        }
    }
}