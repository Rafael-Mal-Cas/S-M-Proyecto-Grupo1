package servlets;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import utils.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;

@WebServlet("/comprarCoche")
public class CompraServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(CompraServlet.class);

    // ───────────── UTILIDAD DE “PAGO TEST” ─────────────
    private boolean procesarPagoDePrueba(String metodo, int cantidadEnCentimos, String datosTarjetaEtc) {
        return new Random().nextInt(10) < 8;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Integer idUsuario = (session != null) ? (Integer) session.getAttribute("idUsuario") : null;
        if (idUsuario == null) {
            logger.warn("Acceso no autorizado a /comprarCoche sin sesión activa.");
            response.sendRedirect("login.jsp");
            return;
        }

        String accion = request.getParameter("accion");
        if ("cancelar".equals(accion)) {
            cancelarCompra(request, response, idUsuario);
            return;
        }

        int idCoche;
        int precio;
        String metodoPago;

        try {
            idCoche    = Integer.parseInt(request.getParameter("idCoche"));
            precio     = Integer.parseInt(request.getParameter("precio"));
            metodoPago = request.getParameter("metodoPago");
        } catch (Exception e) {
            logger.warn("Datos de compra inválidos enviados por el usuario {}.", idUsuario);
            request.setAttribute("error", "Datos de compra inválidos.");
            request.getRequestDispatcher("catalogo.jsp").forward(request, response);
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {

            if (!existeCoche(conn, idCoche)) {
                logger.warn("El coche con ID {} no existe. Usuario: {}", idCoche, idUsuario);
                request.setAttribute("error", "El coche seleccionado no existe.");
                request.getRequestDispatcher("catalogo.jsp").forward(request, response);
                return;
            }

            if (cocheVendido(conn, idCoche)) {
                logger.warn("El coche con ID {} ya fue vendido. Usuario: {}", idCoche, idUsuario);
                request.setAttribute("error", "Este coche ya ha sido vendido.");
                request.getRequestDispatcher("catalogo.jsp").forward(request, response);
                return;
            }

            if (usuarioYaTieneCoche(conn, idUsuario)) {
                logger.info("El usuario {} ya tiene un coche y no puede comprar otro.", idUsuario);
                request.setAttribute("error", "Solo se permite un coche por usuario.");
                request.getRequestDispatcher("catalogo.jsp").forward(request, response);
                return;
            }

            boolean pagoOk = procesarPagoDePrueba(metodoPago, precio * 100, "dummy-data");
            if (!pagoOk) {
                logger.warn("Pago rechazado para el usuario {} con método {}.", idUsuario, metodoPago);
                request.setAttribute("error", "Pago rechazado por el gateway de prueba.");
                request.getRequestDispatcher("catalogo.jsp").forward(request, response);
                return;
            }

            if (registrarCompra(conn, idUsuario, idCoche, precio, metodoPago)) {
                logger.info("Compra registrada con éxito. Usuario: {}, Coche: {}", idUsuario, idCoche);
                session.setAttribute("cocheCompradoId", idCoche);
                session.setAttribute("compraOk", "¡Compra realizada con éxito!");
                response.sendRedirect("Cuenta.jsp");
            } else {
                logger.error("No se pudo registrar la compra. Usuario: {}, Coche: {}", idUsuario, idCoche);
                request.setAttribute("error", "No se pudo registrar la compra.");
                request.getRequestDispatcher("catalogo.jsp").forward(request, response);
            }

        } catch (SQLException e) {
            logger.error("Error de base de datos al procesar compra del usuario {}: {}", idUsuario, e.getMessage(), e);
            request.setAttribute("error", "Error de base de datos.");
            request.getRequestDispatcher("catalogo.jsp").forward(request, response);
        }
    }

    private void cancelarCompra(HttpServletRequest request, HttpServletResponse response, int idUsuario)
            throws IOException, ServletException {

        HttpSession session = request.getSession(false);

        int idCompra;
        String passwordInput = request.getParameter("password");

        try {
            idCompra = Integer.parseInt(request.getParameter("idCompra"));
        } catch (Exception e) {
            logger.warn("ID de compra inválido recibido durante cancelación. Usuario: {}", idUsuario);
            request.setAttribute("error", "ID de compra inválido.");
            request.getRequestDispatcher("cuenta.jsp").forward(request, response);
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {

            String hash = null;
            String sqlPwd = "SELECT contrasena FROM usuarios WHERE id = ?";

            try (PreparedStatement ps = conn.prepareStatement(sqlPwd)) {
                ps.setInt(1, idUsuario);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        hash = rs.getString("contrasena");
                    }
                }
            }

            if (hash == null || !BCrypt.checkpw(passwordInput, hash)) {
                logger.warn("Contraseña incorrecta durante intento de cancelación. Usuario: {}", idUsuario);
                request.setAttribute("error", "Contraseña incorrecta.");
                request.getRequestDispatcher("cuenta.jsp").forward(request, response);
                return;
            }

            String deleteSQL = "DELETE FROM compras WHERE id = ? AND id_usuario = ?";
            try (PreparedStatement ps = conn.prepareStatement(deleteSQL)) {
                ps.setInt(1, idCompra);
                ps.setInt(2, idUsuario);
                int filas = ps.executeUpdate();

                if (filas == 1) {
                    logger.info("Compra cancelada correctamente. Usuario: {}, Compra ID: {}", idUsuario, idCompra);
                    session.setAttribute("mensaje", "Compra cancelada correctamente.");
                } else {
                    logger.error("No se pudo cancelar la compra. Usuario: {}, Compra ID: {}", idUsuario, idCompra);
                    session.setAttribute("error", "No se pudo cancelar la compra.");
                }
            }

            response.sendRedirect("cuenta.jsp");

        } catch (SQLException e) {
            logger.error("Error SQL al cancelar compra del usuario {}: {}", idUsuario, e.getMessage(), e);
            request.setAttribute("error", "Error al cancelar la compra.");
            request.getRequestDispatcher("cuenta.jsp").forward(request, response);
        }
    }

    // ───────────── MÉTODOS AUXILIARES ─────────────
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
