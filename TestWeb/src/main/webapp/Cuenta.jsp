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
<%
String foto = usuario.getImagen();                // puede ser null o ""
if (foto == null || foto.trim().isEmpty()) {
    // ícono «user» gratuito de un CDN (o cualquier PNG/SVG tuyo en la carpeta /Style)
    foto = "https://cdn.jsdelivr.net/gh/twitter/twemoji@latest/assets/svg/1f464.svg";
}
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Tu Cuenta - <%= usuario.getNombre() %></title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="Style/Style_Cuenta.css">
    <link rel="icon" type="image/png" href="Style/logo.png" />
</head>
<body>
<header class="navbar">
    <div class="logo">Venta de coches</div>

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
    <div class="card profile-card">
    <h1>Mi cuenta</h1>

    <!-- FOTO + DATOS PRINCIPALES -->
    <section class="profile-section">
        <img src="<%= foto %>" alt="Foto de perfil" class="avatar">

        <div class="profile-info">
            <h2><%= usuario.getNombre() %> <%= usuario.getApellidos() %></h2>
            <p><i class="fas fa-user"></i> @<%= usuario.getUsuario() %></p>
            <p><i class="fas fa-envelope"></i> <%= usuario.getEmail() %></p>
            <p><i class="fas fa-phone"></i> <%= usuario.getNumeroTelefono() %></p>
        </div>
    </section>

    <!-- GRID CON DETALLES -->
    <div class="info-grid">
        <span class="info-label">Género:</span>
        <span><%= usuario.getGenero().equals("male") ? "Masculino"
               : usuario.getGenero().equals("female") ? "Femenino" : "Otro" %></span>

        <span class="info-label">Contraseña:</span>
        <span>
            <input id="pwdField" type="password" value="<%= usuario.getContrasena() %>" readonly>
            <button type="button" class="eye-btn" onclick="togglePwd()">
                <i id="eyeIcon" class="fas fa-eye"></i>
            </button>
        </span>
    </div>

    <a href="index.jsp" class="btn-primary">Volver al inicio</a>
</div>
</main>
<script>
function togglePwd(){
    const field = document.getElementById('pwdField');
    const icon  = document.getElementById('eyeIcon');
    if(field.type === 'password'){
        field.type = 'text';
        icon.classList.replace('fa-eye','fa-eye-slash');
    }else{
        field.type = 'password';
        icon.classList.replace('fa-eye-slash','fa-eye');
    }
}
</script>

<script>

function toggleDropdown(){
    document.getElementById("dropdown").classList.toggle("show");
}

function togglePassword(){
    const masked=document.getElementById("pwdMasked");
    const real=document.getElementById("pwdReal");
    const toggle=document.querySelector(".password-toggle");
    const isHidden=real.style.display==="none";
    real.style.display=isHidden?"inline":"none";
    masked.style.display=isHidden?"none":"inline";
    toggle.textContent=isHidden?"Ocultar":"Mostrar";
}

/* Cerrar dropdown fuera */
window.onclick=function(e){
    if(!e.target.matches('.user-icon')){
        const dd=document.getElementById("dropdown");
        if(dd && dd.classList.contains("show")) dd.classList.remove("show");
    }
}
</script>

</body>
</html>