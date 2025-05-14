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

        <% 
        String usuario = (String) session.getAttribute("username");
        if (usuario != null) { 
	    %>
	        <h1>Bienvenido, <%= usuario %>!</h1>
	        <p>Has iniciado sesión correctamente.</p>
	        <a href="logout.jsp">Cerrar sesión</a>
	    <% 
	        } else { 
	    %>
	        <h1>No has iniciado sesión</h1>
	        <p><a href="login.jsp">Iniciar sesión</a> o <a href="Registro.jsp">registrarte</a>.</p>
	    <% 
	        }
	    %>

</body>
</html>
