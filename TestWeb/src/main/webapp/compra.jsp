<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="modelo.User" %>
<%@ page import="modelo.coche" %>
<%@ page import="controlador.CompraHandler" %>

<%
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

    coche vehiculo = null;
    String mensaje = "";
    String ruta = application.getRealPath("/WEB-INF/BMW_catalogo.csv");

    try {
        vehiculo = CompraHandler.obtenerVehiculoPorId(idVehiculoParam, ruta);
        if (vehiculo == null) {
            response.sendRedirect("catalogo.jsp");
            return;
        }

        if ("POST".equalsIgnoreCase(request.getMethod())) {
            String metodoPago = request.getParameter("metodoPago");
            String pagoManualStr = request.getParameter("pagoManual");

            mensaje = CompraHandler.procesarCompra(usuario, vehiculo, metodoPago, pagoManualStr);

            if (mensaje.isEmpty()) {
                response.sendRedirect("index.jsp?mensaje=Compra realizada correctamente");
                return;
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
        mensaje = "Error interno: " + e.getMessage();
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

        <form action="<%= request.getRequestURI() + "?idVehiculo=" + vehiculo.getId() %>" method="post">
            <label for="metodoPago">Método de pago:</label>
			<select name="metodoPago" id="metodoPago" required>
			    <option value="">Seleccione un método</option>
			    <option value="tarjeta">Tarjeta de crédito</option>
			    <option value="paypal">PayPal</option>
			    <option value="transferencia">Transferencia bancaria</option>
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
