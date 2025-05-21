<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="modelo.User" %>
<%
    User usuario = (User) session.getAttribute("usuario");
    if (usuario == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Editar Perfil - <%= usuario.getNombre() %></title>
    <link rel="stylesheet" href="Style/Style_Perfil.css"> <!-- o la hoja que uses -->
</head>
<body>
<main class="contenido">
    <form action="<%= request.getContextPath() %>/editarPerfil" method="post" enctype="multipart/form-data" class="form-card">

        <h1>Editar mi perfil</h1>

        <label>Nombre:</label>
        <input type="text" name="nombre" value="<%= usuario.getNombre() %>" required>

        <label>Apellidos:</label>
        <input type="text" name="apellidos" value="<%= usuario.getApellidos() %>" required>

        <label>Email:</label>
        <input type="email" name="email" value="<%= usuario.getEmail() %>" required>

        <label>Teléfono:</label>
        <input type="text" name="telefono" value="<%= usuario.getNumeroTelefono() %>" required>

        <label>Género:</label>
        <select name="genero">
            <option value="male" <%= usuario.getGenero().equals("male") ? "selected" : "" %>>Masculino</option>
            <option value="female" <%= usuario.getGenero().equals("female") ? "selected" : "" %>>Femenino</option>
            <option value="other" <%= usuario.getGenero().equals("other") ? "selected" : "" %>>Otro</option>
        </select>

        <label>Foto de perfil:</label>
        <input type="file" name="imagen">

        <button type="submit" class="btn-primary">Guardar cambios</button>
    </form>
</main>
</body>
</html>
