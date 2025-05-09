<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%
String usuario = (String) session.getAttribute("username");
String password = (String) session.getAttribute("password"); // solo si se guardó en login
if (usuario == null) {
    response.sendRedirect("login.jsp");
    return;
}
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Mi Cuenta</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
	<link rel="stylesheet" type="text/css" href="Style/Style_index.css">
</head>
<body>

    <header class="navbar">
        <div class="logo">MiSitio</div>
        <div class="user-menu">
            <i class="fas fa-user-circle user-icon" onclick="toggleDropdown()"></i>
            <div id="dropdown" class="dropdown-content">
                <span class="username"><strong><%= usuario %></strong></span>
                <a href="cuenta.jsp">Cuenta</a>
                <a href="logout.jsp">Cerrar sesión</a>
            </div>
        </div>
    </header>

    <main class="contenido">
        <div class="card">
            <h1>Perfil de Usuario</h1>
            <p><strong>Nombre de usuario:</strong> <%= usuario %></p>

          <p><strong>Contraseña:</strong> 
                <span id="censurada">********</span>
                <span id="real" style="display: none;"><%= password %></span>
            </p> 

           <!-- <button onclick="mostrarCampo()" id="btnMostrar">Mostrar contraseña</button> 

            <div id="campoVerificacion" style="display: none; margin-top: 10px;">
                <input type="password" id="inputVerif" placeholder="Introduce tu contraseña">
                <button onclick="verificar()">Ver</button>
                <p id="msg" style="color: red; font-size: 0.9em;"></p>
            </div> -->

            <a href="index.jsp">Volver al inicio</a>
        </div>
    </main>

    <script>
        const realPass = "<%= password %>";

        function mostrarCampo() {
            document.getElementById("campoVerificacion").style.display = "block";
            document.getElementById("btnMostrar").style.display = "none";
        }

        function verificar() {
            const input = document.getElementById("inputVerif").value;
            const msg = document.getElementById("msg");

            if (input === realPass) {
                document.getElementById("censurada").style.display = "none";
                document.getElementById("real").style.display = "inline";
                msg.textContent = "";
                document.getElementById("campoVerificacion").style.display = "none";
            } else {
                msg.textContent = "Contraseña incorrecta.";
            }
        }
    </script>

</body>
</html>

