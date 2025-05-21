<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ page import="modelo.User" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%
    User usuario = (User) session.getAttribute("usuario");
    String fotoPerfil = (usuario != null && usuario.getImagen() != null && !usuario.getImagen().trim().isEmpty())
                        ? usuario.getImagen()
                        : "Style/default-user.svg";
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Formulario y Login</title>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
<link rel="stylesheet" type="text/css" href="Style/Style_index.css">
<link rel="icon" type="image/png" href="Style/logo_blanco.png" />
</head>
<body>
<header class="navbar">
    <div style="display: flex; align-items: center; gap: 20px;">
        <a href="index.jsp" class="logo" style="cursor: pointer;">
            <img src="Style/logo_blanco.png" style="width: 53px; height: 53px;">
        </a>
        <a href="catalogo.jsp" style="cursor: pointer; color: white; text-decoration: none; text-align: left;">Catálogo</a>
    </div>

    <div class="user-menu">
        <%
            // Aquí usas la imagen que calculaste arriba en fotoPerfil
        %>
		<% if (usuario != null) { %>
		    <img id="avatar-btn" src="<%= fotoPerfil %>" class="user-avatar" alt="Foto de perfil de <%= usuario.getUsuario() %>" onclick="toggleDropdown()" style="cursor:pointer; width: 40px; height: 40px; border-radius: 50%;">
		<% } else { %>
		    <a href="login.jsp" class="login-btn" style="color: white; text-decoration: none;">Iniciar sesión</a>
		<% } %>
        
        <div id="dropdown" class="dropdown-content">
            <%
                // Mostrar solo el nombre de usuario, no todo el objeto
            %>
            <% if (usuario != null) { %>
			    <span class="username"><strong><%= usuario.getUsuario() %></strong></span>
			    <a href="Cuenta.jsp">Cuenta</a>
			    <a href="logout.jsp">
			        <i class="fas fa-right-from-bracket" style="margin-right: 6px;"></i> Cerrar sesión
			    </a>
			<% } else { %>
			    <a href="login.jsp">Iniciar sesión</a>
			    <a href="registro.jsp">Registrarse</a>
			<% } %>

        </div>
    </div>
</header>

<main class="contenido">
    <%
    if (usuario != null) {
    %>
        <div class="card">
            <h1>Bienvenido, <%= usuario.getNombre() %>!</h1>
            <span>Has iniciado sesión correctamente.</span>
        </div>
    <%
    } else {
    %>
        <div class="card">
            <h1>Bienvenido a <br>Practical Cars S&amp;M</h1>
            <p>Por favor, inicia sesión o regístrate para continuar.</p>
        </div>
    <%
    }
    %>
    
</main>

<footer class="footer">
  <div class="footer-content">
    <div class="footer-section">
      <h4>Información de contacto</h4>
		<p><strong>Teléfono:</strong> <a href="tel:+34912345678" style="color: inherit; text-decoration: none;">+34 912 345 678</a></p>
		<p><strong>Correo electrónico:</strong> <a href="mailto:practicalSM@antoniolobato.es" style="color: inherit; text-decoration: none;">practicalSM@antoniolobato.es</a></p>
		<p><strong>Dirección postal:</strong> Calle Ficticia 123, 18000 Granada, España</p>
    </div>
    <div class="footer-section">
      <h4>Horario de atención</h4>
      <p>Lunes a Viernes: 9:00 - 18:00</p>
      <p>Sábados: 10:00 - 14:00</p>
    </div>
    <div class="footer-section">
      <h4>Síguenos</h4>
      <p>1ºDAM - Pracricas S&amp;M</p>
      <p>Patrocinado por CES Cristo Rey</p>
      
    </div>
  </div>
</footer>


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
