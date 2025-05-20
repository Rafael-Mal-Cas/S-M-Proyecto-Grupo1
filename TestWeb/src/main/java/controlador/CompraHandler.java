package controlador;

import modelo.catalogo;
import modelo.coche;
import modelo.User;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class CompraHandler {

    public static coche obtenerVehiculoPorId(String idVehiculo, String rutaCSV) throws Exception {
        catalogo catalogoBMW = new catalogo();
        catalogoBMW.cargarDesdeCSV(rutaCSV);
        for (coche c : catalogoBMW.getCoches()) {
            if (c.getId().equals(idVehiculo)) {
                return c;
            }
        }
        return null;
    }

    public static String procesarCompra(User usuario, coche vehiculo, String metodoPago, String pagoManualStr) {
        if (metodoPago == null || metodoPago.isEmpty()) {
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

                    // Verificar que el ID del coche es numérico
                    try {
                        int idCoche = Integer.parseInt(vehiculo.getId());
                        ps.setInt(2, idCoche);
                    } catch (NumberFormatException e) {
                        return "Error: el ID del coche no es un número válido.";
                    }

                    ps.setDouble(3, precioFinal);
                    ps.setString(4, metodoPagoEnum);
                    ps.executeUpdate();
                }
            }
            return ""; // Éxito
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al registrar la compra: " + e.getMessage();
        }
    }
}
