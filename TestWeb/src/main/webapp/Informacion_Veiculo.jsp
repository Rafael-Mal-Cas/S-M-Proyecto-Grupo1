<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ page import="modelo.User" %>
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
    
    User usuario = (User) session.getAttribute("usuario");
    String fotoPerfil = usuario.getImagen();
    if (fotoPerfil == null || fotoPerfil.trim().isEmpty()) {
        fotoPerfil = "Style/default-user.svg";
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
    <link rel="icon" type="image/png" href="Style/logo_blanco.png" />
</head>
<body>
<header class="navbar">
    <div style="display: flex; align-items: center; gap: 20px;">
        <a href="index.jsp" class="logo" style="cursor: pointer;">
            <img src="Style/logo_blanco.png" style="width: 53px; height: 53px;">
        </a>
        <%-- Como el usuario ya está comprobado, mostramos el enlace --%>
        <a href="catalogo.jsp" style="color: white; text-decoration: none;">Catálogo</a>
    </div>
    <div class="user-menu">
        <img id="avatar-btn" src="<%= fotoPerfil %>" class="user-avatar" alt="Foto de perfil de <%= usuario.getUsuario() %>" onclick="toggleDropdown()" style="cursor:pointer; width: 40px; height: 40px; border-radius: 50%;">
        <div id="dropdown" class="dropdown-content">
            <span class="username"><strong><%= usuario.getUsuario() %></strong></span>
            <a href="Cuenta.jsp">Cuenta</a>
            <a href="logout.jsp">Cerrar sesión</a>
        </div>
    </div>
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
<script>
    function toggleDropdown() {
        document.getElementById("dropdown").classList.toggle("show");
    }

    window.onclick = function(event) {
        if (!event.target.matches('.user-avatar') && !event.target.matches('.user-icon')) {
            const dropdown = document.getElementById("dropdown");
            if (dropdown && dropdown.classList.contains('show')) {
                dropdown.classList.remove('show');
            }
        }
    }
</script>
</body>
</html>
