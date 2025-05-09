<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="modelo.User" %>
<%@ page session="true" %>
<%
User usuario = (User) session.getAttribute("usuario");
if (usuario == null) {
    response.sendRedirect("login.jsp");
    return;
}
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Mi Cuenta - <%= usuario.getNombre() %></title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="Style/Style_index.css">
    <link rel="stylesheet" type="text/css" href="Style/Style_registro.css">
</head>
<body>
    <header class="navbar">
        <div class="logo">MiSitio</div>
        <div class="user-menu">
            <i class="fas fa-user-circle user-icon" onclick="toggleDropdown()"></i>
            <div id="dropdown" class="dropdown-content">
                <span class="username"><strong><%= usuario.getUsuario() %></strong></span>
                <a href="cuenta.jsp">Cuenta</a>
                <a href="logout.jsp">Cerrar sesión</a>
            </div>
        </div>
    </header>
    <main class="contenido">
        <div class="card">
            <h1>Perfil de Usuario</h1>
            
            <div class="profile-section">

                <div class="profile-info">
                    <h2><%= usuario.getNombre() %> <%= usuario.getApellidos() %></h2>
                    <p><i class="fas fa-user"></i> @<%= usuario.getUsuario() %></p>
                    <p><i class="fas fa-envelope"></i> <%= usuario.getEmail() %></p>
                    <p><i class="fas fa-phone"></i> <%= usuario.getNumeroTelefono() %></p>
                </div>
            </div>
            
            <div class="info-grid">
                <div class="info-label">ID de usuario:</div>
                <div><%= usuario.getId() %></div>
                
                <div class="info-label">Género:</div>
                <div><%= usuario.getGenero().equals("male") ? "Masculino" : usuario.getGenero().equals("female") ? "Femenino" : "Otro" %></div>
                
                <div class="info-label">Contraseña:</div>
                <div>
                    <span id="censurada">********</span>
                    <span id="real" style="display: none;"><%= usuario.getContrasena() %></span>
                    <span class="password-toggle" onclick="togglePassword()">Mostrar</span>
                </div>
            </div>

            <div style="margin-top: 2rem;">
                <a href="editarCuenta.jsp" class="button">Editar perfil</a>
                <a href="index.jsp" class="button secondary">Volver al inicio</a>
            </div>
        </div>
    </main>

    <script>
        function toggleDropdown() {
            document.getElementById("dropdown").classList.toggle("show");
        }

        function togglePassword() {
            const censurada = document.getElementById("censurada");
            const real = document.getElementById("real");
            const toggle = document.querySelector(".password-toggle");
            
            if (censurada.style.display === "none") {
                censurada.style.display = "inline";
                real.style.display = "none";
                toggle.textContent = "Mostrar";
            } else {
                censurada.style.display = "none";
                real.style.display = "inline";
                toggle.textContent = "Ocultar";
            }
        }

        // Cerrar el dropdown si se hace clic fuera de él
        window.onclick = function(event) {
            if (!event.target.matches('.user-icon')) {
                const dropdowns = document.getElementsByClassName("dropdown-content");
                for (let i = 0; i < dropdowns.length; i++) {
                    const openDropdown = dropdowns[i];
                    if (openDropdown.classList.contains('show')) {
                        openDropdown.classList.remove('show');
                    }
                }
            }
        }
    </script>
</body>
</html>