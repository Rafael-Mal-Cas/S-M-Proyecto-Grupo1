<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="modelo.catalogo" %>
<%@ page import="modelo.coche" %>
<%@ page import="modelo.User" %>
<%@ page import="java.sql.*" %>
<%@ page import="javax.naming.*" %>
<%@ page import="javax.sql.DataSource" %>
<%@ page session="true" %>
<%
    // Validar usuario logueado
    User usuario = (User) session.getAttribute("usuario");
    if (usuario == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String idVehiculoParam = request.getParameter("idVehiculo");
    if (idVehiculoParam == null) {
        response.sendRedirect("catalogo.jsp");
        return;
    }

    // Cargar catálogo (simulado con CSV)
    catalogo catalogoBMW = new catalogo();
    String ruta = application.getRealPath("/WEB-INF/BMW_catalogo.csv");
    catalogoBMW.cargarDesdeCSV(ruta);

    // Buscar coche por id
    coche vehiculo = null;
    for (coche c : catalogoBMW.getCoches()) {
        if (c.getId().equals(idVehiculoParam)) {
            vehiculo = c;
            break;
        }
    }

    if (vehiculo == null) {
        response.sendRedirect("catalogo.jsp");
        return;
    }

    String mensaje = "";

    if ("POST".equalsIgnoreCase(request.getMethod())) {
        // Procesar compra
        String metodoPago = request.getParameter("metodoPago");
        String pagoManualStr = request.getParameter("pagoManual");

        double precioFinal = vehiculo.getPrecio(); // precio por defecto

        if (metodoPago == null || metodoPago.isEmpty()) {
            mensaje = "Debe seleccionar un método de pago.";
        }

        // Si hay pago manual (opcional)
        if (metodoPago != null && metodoPago.equals("Pago manual")) {
            if (pagoManualStr == null || pagoManualStr.trim().isEmpty()) {
                mensaje = "Debe introducir un precio para el pago manual.";
            } else {
                try {
                    precioFinal = Double.parseDouble(pagoManualStr);
                    if (precioFinal <= 0) {
                        mensaje = "El precio debe ser mayor que cero.";
                    }
                } catch (NumberFormatException e) {
                    mensaje = "Precio manual inválido.";
                }
            }
        }

        if (mensaje.isEmpty()) {
            // Insertar la compra en la BD
            try {
                Context initCtx = new InitialContext();
                DataSource ds = (DataSource) initCtx.lookup("java:comp/env/jdbc/tuDataSource");

                try (Connection con = ds.getConnection()) {
                    String sql = "INSERT INTO compras (usuario_id, coche_id, precio, metodo_pago, fecha_compra) VALUES (?, ?, ?, ?, NOW())";
                    try (PreparedStatement ps = con.prepareStatement(sql)) {
                        ps.setInt(1, usuario.getId()); // debe existir getId()
                        ps.setString(2, vehiculo.getId());
                        ps.setDouble(3, precioFinal);
                        ps.setString(4, metodoPago);
                        ps.executeUpdate();
                    }
                }

                // Redirigir a página principal con mensaje de éxito
                response.sendRedirect("index.jsp?mensaje=Compra realizada correctamente");
                return;

            } catch (Exception e) {
                e.printStackTrace();
                mensaje = "Error al registrar la compra: " + e.getMessage();
            }
        }
    }
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <title>Comprar vehículo - <%= vehiculo.getMarca() + " " + vehiculo.getModelo() %></title>
    <link rel="stylesheet" href="Style/Style_Cuenta.css" />
    <link rel="stylesheet" href="Style/Style_Info_coche.css" />
</head>
<body>
<header class="navbar">
    <div class="logo">Venta de coches</div>
</header>

<main class="car-detail-container">
    <div class="car-image">
        <img src="<%= (vehiculo.getImagen() != null && !vehiculo.getImagen().isEmpty()) ? vehiculo.getImagen() : "Style/default-car.png" %>" 
             alt="Imagen de <%= vehiculo.getMarca() + " " + vehiculo.getModelo() %>" />
    </div>
    <div class="car-info">
        <h1><%= vehiculo.getMarca() %> <%= vehiculo.getModelo() %> (<%= vehiculo.getAnio() %>)</h1>
        <p><strong>Precio:</strong> <%= String.format("%,.2f", vehiculo.getPrecio()) %> €</p>
        <p><strong>Color:</strong> <%= vehiculo.getColor() %></p>
        <p><strong>Combustible:</strong> <%= vehiculo.getCombustible() %></p>

        <form action="<%= request.getContextPath() %>/compra.jsp?idVehiculo=<%= vehiculo.getId() %>" method="post">
		    <label for="metodoPago">Método de pago:</label>
		    <select name="metodoPago" id="metodoPago" required>
		        <option value="">Seleccione un método</option>
		        <option value="Tarjeta de crédito">Tarjeta de crédito</option>
		        <option value="PayPal">PayPal</option>
		        <option value="Transferencia bancaria">Transferencia bancaria</option>
		        <option value="Pago manual">Pago manual (introducir precio)</option>
		    </select>
		
		    <div id="pagoManualContainer" style="display:none; margin-top:10px;">
		        <label for="pagoManual">Precio a pagar (€):</label>
		        <input type="number" name="pagoManual" id="pagoManual" step="0.01" min="0" />
		    </div>
		
		    <button type="submit" style="margin-top:15px;">Confirmar compra</button>
		</form>

        <p style="color:red; margin-top:10px;"><%= mensaje %></p>
    </div>
</main>

<script>
    const metodoPagoSelect = document.getElementById('metodoPago');
    const pagoManualContainer = document.getElementById('pagoManualContainer');
    const pagoManualInput = document.getElementById('pagoManual');

    metodoPagoSelect.addEventListener('change', function() {
        if (this.value === 'Pago manual') {
            pagoManualContainer.style.display = 'block';
            pagoManualInput.setAttribute('required', 'required');
        } else {
            pagoManualContainer.style.display = 'none';
            pagoManualInput.removeAttribute('required');
            pagoManualInput.value = '';
        }
    });
</script>

</body>
</html>
