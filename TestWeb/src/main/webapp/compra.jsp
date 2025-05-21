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
    
    String fotoPerfil = usuario.getImagen();
    if (fotoPerfil == null || fotoPerfil.trim().isEmpty()) {
        fotoPerfil = "Style/default-user.svg";
    }
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <title>Comprar vehículo - <%= vehiculo.getMarca() + " " + vehiculo.getModelo() %></title>
    <link rel="stylesheet" href="Style/Style_Cuenta.css" />
    <link rel="stylesheet" href="Style/Style_Info_coche.css" />
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
        <img src="<%= (vehiculo.getImagen() != null && !vehiculo.getImagen().isEmpty()) ? vehiculo.getImagen() : "Style/default-car.png" %>" 
             alt="Imagen de <%= vehiculo.getMarca() + " " + vehiculo.getModelo() %>" />
    </div>
    <div class="car-info">
        <h1><%= vehiculo.getMarca() %> <%= vehiculo.getModelo() %> (<%= vehiculo.getAnio() %>)</h1>
        <p><strong>Precio:</strong> <%= String.format("%,.2f", vehiculo.getPrecio()) %> €</p>
        <p><strong>Color:</strong> <%= vehiculo.getColor() %></p>
        <p><strong>Combustible:</strong> <%= vehiculo.getCombustible() %></p>

        <form action="<%= request.getRequestURI() + "?idVehiculo=" + vehiculo.getId() %>" method="post">
	    <div style="margin-bottom: 15px;">
	        <label for="metodoPago" style="display: block; margin-bottom: 5px; font-weight: bold; color: #444;">Método de pago:</label>
	        <select name="metodoPago" id="metodoPago" required style="width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 5px;">
	            <option value="">Seleccione un método</option>
	            <option value="tarjeta">Tarjeta de crédito</option>
	            <option value="paypal">PayPal</option>
	            <option value="transferencia">Transferencia bancaria</option>
	        </select>
	    </div>
	
	    <div id="pagoManualContainer" style="display:none; margin-bottom: 15px;">
	        <label for="pagoManual" style="display: block; margin-bottom: 5px; font-weight: bold; color: #444;">Precio a pagar (€):</label>
	        <input type="number" name="pagoManual" id="pagoManual" step="0.01" min="0" style="width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 5px;"/>
	    </div>
	
	    <button type="submit" class="btn-comprar">Confirmar compra</button>
	</form>
        <form action="comprarCoche" method="post">
		    <input type="hidden" name="accion" value="cancelar" />
		    <input type="hidden" name="idCompra" value="${compra.id}" />
		    
		    <label>Introduce tu contraseña para cancelar:</label>
		    <input type="password" name="password" required />
		    
		    <button type="submit">Cancelar compra</button>
		</form>
        

        <p style="color:red; margin-top:10px;"><%= mensaje %></p>
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
