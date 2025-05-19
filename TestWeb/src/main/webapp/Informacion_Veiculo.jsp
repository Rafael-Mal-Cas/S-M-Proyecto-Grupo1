<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="modelo.coche" %>
<%@ page session="true" %>
<%
    coche vehiculo = (coche) session.getAttribute("vehiculoSeleccionado");
    if (vehiculo == null) {
        response.sendRedirect("catalogo.jsp");
        return;
    }
%>
<%
    String imagen = vehiculo.getImagen();
    if (imagen == null || imagen.trim().isEmpty()) {
        imagen = "Style/default-car.png";
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Detalle Vehículo - <%= vehiculo.getMarca() + " " + vehiculo.getModelo() %></title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="Style/Style_Cuenta.css">
    <link rel="stylesheet" type="text/css" href="Style/Style_Info_coche.css">
    <link rel="icon" type="image/png" href="Style/logo.png" />
</head>
<body>
<header class="navbar">
    <div class="logo">Venta de coches</div>
</header>

<main class="car-detail-container">
    <div class="car-image">
        <img src="<%= imagen %>" alt="Imagen de <%= vehiculo.getMarca() + " " + vehiculo.getModelo() %>">
    </div>
    <div class="car-info">
        <h1><%= vehiculo.getMarca() %> <%= vehiculo.getModelo() %> (<%= vehiculo.getAnio() %>)</h1>
        <p><strong>Precio:</strong> $<%= String.format("%,.2f", vehiculo.getPrecio()) %></p>
        <p><strong>Color:</strong> <%= vehiculo.getColor() %></p>
        <p><strong>Combustible:</strong> <%= vehiculo.getCombustible() %></p>

        <form action="compra.jsp" method="get">
		    <input type="hidden" name="idVehiculo" value="<%= vehiculo.getId() %>">
		    <button type="submit" class="btn-comprar">Comprar</button>
		</form>
    </div>
</main>
</body>
</html>
