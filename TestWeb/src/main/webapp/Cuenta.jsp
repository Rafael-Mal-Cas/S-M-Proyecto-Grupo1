<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="modelo.User" %>
<%@ page import="java.sql.*, javax.naming.*, javax.sql.*, modelo.coche, modelo.catalogo, java.util.*" %>
<%@ page session="true" %>

<%
User usuario = (User) session.getAttribute("usuario");
if (usuario == null) {
    response.sendRedirect("login.jsp");
    return;
}

// Foto de perfil por defecto
String fotoPerfil = usuario.getImagen();
if (fotoPerfil == null || fotoPerfil.trim().isEmpty()) {
    fotoPerfil = "Style/default-user.svg";
}
%>

<%
    // Obtener las compras del usuario
    List<String> comprasUsuario = new ArrayList<>();

    try {
        Context initCtx = new InitialContext();
        DataSource ds = (DataSource) initCtx.lookup("java:comp/env/jdbc/miBaseDatos");

        try (Connection con = ds.getConnection()) {
            String sql = "SELECT c.id_coche, c.precio, c.metodo_pago, c.fecha_compra FROM compras c WHERE c.id_usuario = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, usuario.getId());
                try (ResultSet rs = ps.executeQuery()) {

                    // Cargar catálogo desde archivo CSV
                    catalogo BMW_catalogo = new catalogo();
                    BMW_catalogo.cargarDesdeCSV(application.getRealPath("/WEB-INF/BMW_catalogo.csv"));

                    while (rs.next()) {
                        String idCoche = rs.getString("id_coche");
                        double precio = rs.getDouble("precio");
                        String metodo = rs.getString("metodo_pago");
                        Timestamp fecha = rs.getTimestamp("fecha_compra");

                        coche cocheComprado = null;

                        for (coche c : BMW_catalogo.getCoches()) {
                            if (c.getId().trim().equalsIgnoreCase(idCoche.trim())) {
                                cocheComprado = c;
                                break;
                            }
                        }

                        if (cocheComprado != null) {
                            comprasUsuario.add(
                                "<div class='compra-item'>" +
                                "<p><strong>" + cocheComprado.getModelo() + "</strong></p>" +
                                "<p>Precio: €" + String.format("%,.2f", precio) + "</p>" +
                                "<p>Método de pago: " + metodo + "</p>" +
                                "<p>Fecha: " + fecha.toString() + "</p>" +
                                "</div>"
                            );
                        } else {
                            System.out.println("WARNING - No se encontró coche con id '" + idCoche + "' en catálogo");
                        }
                    }

                }
            }
        }
    } catch (Exception e) {
        comprasUsuario.add("<p style='color:red;'>Error al cargar compras: " + e.getMessage() + "</p>");
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Tu Cuenta - <%= usuario.getNombre() %></title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="Style/Style_Cuenta.css">
    <link rel="icon" type="image/png" href="Style/logo_blanco.png" />
    <style>
        .compras-section {
            margin-top: 2rem;
            padding: 1rem;
            border-top: 2px solid #ddd;
        }
        .compra-item {
            background-color: #f9f9f9;
            border-radius: 12px;
            padding: 1rem;
            margin-bottom: 1rem;
            box-shadow: 0 1px 3px rgba(0,0,0,0.1);
        }
    </style>
</head>
<body>
<header class="navbar">
    <a href="index.jsp" class="logo" style="cursor: pointer;">
        <img src="Style/logo_blanco.png" style="width: 53px; height: 53px;">
    </a>
    <a href="catalogo.jsp" style="cursor: pointer; color: white; text-decoration: none; text-align: left;">Catálogo</a>
    
    <div class="user-menu">
        <img id="avatar-btn" src="<%= fotoPerfil %>" class="user-avatar" alt="Perfil">
        <div id="dropdown" class="dropdown-content">
            <span class="username"><strong><%= usuario.getUsuario() %></strong></span>
            <a href="logout.jsp">
                <i class="fas fa-right-from-bracket" style="margin-right: 6px;"></i> Cerrar sesión
            </a>
        </div>
    </div>
</header> 
<main class="contenido">
    <div class="card profile-card">
        <h1>Mi cuenta</h1>

        <section class="profile-section">
            <img src="<%= fotoPerfil %>" alt="Foto de perfil" class="avatar">
            <div class="profile-info">
                <h2><%= usuario.getNombre() %> <%= usuario.getApellidos() %></h2>
                <p><i class="fas fa-user"></i> @<%= usuario.getUsuario() %></p>
                <p><i class="fas fa-envelope"></i> <%= usuario.getEmail() %></p>
                <p><i class="fas fa-phone"></i> <%= usuario.getNumeroTelefono() %></p>
            </div>
        </section>

        <div class="info-grid">
            <span class="info-label">Género:</span>
            <span><%= usuario.getGenero().equals("male") ? "Masculino" : usuario.getGenero().equals("female") ? "Femenino" : "Otro" %></span>

            <span class="info-label">Contraseña:</span>
            <span>
                <input id="pwdField" type="password" value="<%= usuario.getContrasena() %>" readonly>
                <button type="button" class="eye-btn" onclick="togglePwd()">
                    <i id="eyeIcon" class="fas fa-eye"></i>
                </button>
            </span>
        </div>

        <a href="index.jsp" class="btn-primary">Volver al inicio</a>

        <section class="compras-section">
            <h2>Mis Compras</h2>
            <%
            if (comprasUsuario.isEmpty() == true) {
            %>
                <p>No has realizado ninguna compra aún.</p>
            <%
            } else {
                for (String item : comprasUsuario) {
                    out.println(item);
                }
            }
            %>
        </section>
    </div>
</main>

<script>
function togglePwd() {
    const field = document.getElementById('pwdField');
    const icon  = document.getElementById('eyeIcon');
    if (field.type === 'password') {
        field.type = 'text';
        icon.classList.replace('fa-eye','fa-eye-slash');
    } else {
        field.type = 'password';
        icon.classList.replace('fa-eye-slash','fa-eye');
    }
}
</script>

<script>
document.getElementById('avatar-btn').addEventListener('click', function (e) {
    e.stopPropagation();
    document.getElementById('dropdown').classList.toggle('show');
});
window.addEventListener('click', function () {
    const dd = document.getElementById('dropdown');
    if (dd.classList.contains('show')) {
        dd.classList.remove('show');
    }
});
</script>

</body>
</html>
