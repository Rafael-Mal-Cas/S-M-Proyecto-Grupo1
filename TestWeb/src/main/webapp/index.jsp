<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Formulario y Login</title>
</head>
<body>

	<%-- Verificar si el usuario ya está logueado --%>
	<% 
        String usuario = (String) session.getAttribute("username");
        if (usuario != null) {
    %>
	<h1>
		Bienvenido,
		<%= usuario %>!
	</h1>
	<a href="logout.jsp">Cerrar sesión</a>
	<hr />
	<% } %>

	<%-- Mostrar valores del 0 al 10 --%>
	<ul>
		<% for (int i = 0; i <= 10; i++) { %>
		<li>El valor de la variable es <%= i %></li>
		<% } %>
	</ul>

	<%-- Formulario para enviar texto a MiServlet --%>
	<form action="MiServlet" method="post">
		<input type="text" placeholder="Introduce algo" id="miDato"
			name="miDato">
		<button type="submit">Enviar</button>
	</form>

	<%-- Mostrar respuesta del servlet MiServlet --%>
	<%
        String frase = (String) request.getAttribute("miDato");
    %>
	<h1><%= frase != null ? frase : "No llega nada" %></h1>

	<hr />

	<%-- Mostrar formulario de login solo si no hay sesión activa --%>
	<% if (usuario == null) { %>
	<h2>Iniciar sesión</h2>
	<form action="loginServer" method="post">
		Usuario: <input type="text" name="username" required /><br />
		Contraseña: <input type="password" name="password" required /><br /> <input
			type="submit" value="Entrar" />
	</form>

	<%-- Mostrar resultado del login --%>
	<%
            String mensaje = (String) request.getAttribute("error");
            if (mensaje != null) {
        %>
	<p style="color: red;">
		<%= mensaje %>
	</p>
	<%
            }
        %>

	<p>
		¿No tienes cuenta? <a href="Registro.jsp">Regístrate aquí</a>
	</p>
	<% } %>

</body>
</html>