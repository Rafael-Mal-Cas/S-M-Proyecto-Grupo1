package controlador;

import modelo.catalogo;
import modelo.coche;
import modelo.User;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CompraHandler {

    private static final Logger logger = LogManager.getLogger(CompraHandler.class);

    public static coche obtenerVehiculoPorId(String idVehiculo, String rutaCSV) throws Exception {
        logger.debug("Buscando vehículo con ID: {}", idVehiculo);
        catalogo catalogoBMW = new catalogo();
        catalogoBMW.cargarDesdeCSV(rutaCSV);
        for (coche c : catalogoBMW.getCoches()) {
            if (c.getId().equals(idVehiculo)) {
                logger.info("Vehículo encontrado: ID {}", idVehiculo);
                return c;
            }
        }
        logger.warn("Vehículo no encontrado: ID {}", idVehiculo);
        return null;
    }

    public static String procesarCompra(User usuario, coche vehiculo, String metodoPago, String pagoManualStr) {
        logger.debug("Procesando compra para usuario ID {} con método de pago: {}", usuario.getId(), metodoPago);

        if (metodoPago == null || metodoPago.isEmpty()) {
            logger.warn("Método de pago no especificado para usuario ID {}", usuario.getId());
            return "Debe seleccionar un método de pago.";
        }

        String metodoPagoEnum;
        switch (metodoPago.toLowerCase()) {
            case "tarjeta":
            case "tarjeta de crédito":
                metodoPagoEnum = "tarjeta";
                break;
            case "paypal":
                metodoPagoEnum = "paypal";
                break;
            case "transferencia":
            case "transferencia bancaria":
                metodoPagoEnum = "transferencia";
                break;
            default:
                logger.warn("Método de pago inválido: {} para usuario ID {}", metodoPago, usuario.getId());
                return "Método de pago inválido.";
        }

        double precioFinal = vehiculo.getPrecio();

        try {
            Context initCtx = new InitialContext();
            DataSource ds = (DataSource) initCtx.lookup("java:comp/env/jdbc/miBaseDatos");

            try (Connection con = ds.getConnection()) {
                String sql = "INSERT INTO compras (id_usuario, id_coche, precio, metodo_pago, fecha_compra) VALUES (?, ?, ?, ?, NOW())";
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setInt(1, usuario.getId());

                    try {
                        int idCoche = Integer.parseInt(vehiculo.getId());
                        ps.setInt(2, idCoche);
                    } catch (NumberFormatException e) {
                        logger.error("ID del coche inválido: {}", vehiculo.getId(), e);
                        return "Error: el ID del coche no es un número válido.";
                    }

                    ps.setDouble(3, precioFinal);
                    ps.setString(4, metodoPagoEnum);
                    int filas = ps.executeUpdate();

                    if (filas > 0) {
                        logger.info("Compra registrada exitosamente para usuario ID {} y coche ID {}", usuario.getId(), vehiculo.getId());
                    } else {
                        logger.warn("No se registró la compra para usuario ID {} y coche ID {}", usuario.getId(), vehiculo.getId());
                        return "Error al registrar la compra.";
                    }
                }
            }
            return ""; // Éxito
        } catch (Exception e) {
            logger.error("Excepción al registrar la compra para usuario ID " + usuario.getId(), e);
            return "Error al registrar la compra: " + e.getMessage();
        }
    }
}
