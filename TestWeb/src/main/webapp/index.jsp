<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Formulario y Login</title>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
<link rel="stylesheet" type="text/css" href="Style/Style_index.css">
</head>
<body>
    <header class="navbar">
        <div class="logo">MiSitio</div>
        <div class="user-menu">
            <i class="fas fa-user-circle user-icon" onclick="toggleDropdown()"></i>
            <div id="dropdown" class="dropdown-content">
                <%
                String usuario = (String) session.getAttribute("username");
                if (usuario != null) {
                %>
                    <span class="username"><strong><%= usuario %></strong></span>
                    <a href="Cuenta.jsp">Cuenta</a>
                    <a href="logout.jsp">Cerrar sesión</a>
                <%
                } else {
                %>
                    <a href="login.jsp">Iniciar sesión</a>
                    <a href="Registro.jsp">Registrarse</a>
                <%
                }
                %>
            </div>
        </div>
    </header>
    <main class="contenido">
        <%
        if (usuario != null) {
        %>
            <div class="card">
                <h1>Bienvenido, <%= usuario %>!</h1>
                <span>Has iniciado sesión correctamente.</span>
            </div>
        <%
        } else {
        %>
            <div class="card">
                <h1>Bienvenido a MiSitio</h1>
                <p>Por favor, inicia sesión o regístrate para continuar.</p>
            </div>
        <%
        }
        %>
    </main>
    <script>
        function toggleDropdown() {
            document.getElementById("dropdown").classList.toggle("show");
        }

        window.onclick = function(event) {
            if (!event.target.matches('.user-icon')) {
                const dropdown = document.getElementById("dropdown");
                if (dropdown && dropdown.classList.contains('show')) {
                    dropdown.classList.remove('show');
                }
            }
        }
    </script>

</body>
</html>
