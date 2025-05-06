<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<title>Login</title>
</head>
<body>
	<h1>Iniciar Sesión</h1>
	<% if (request.getAttribute("error") != null) { %>
	<p style="color: red;"><%= request.getAttribute("error") %></p>
	<% } %>
	<h2>Iniciar sesión</h2>
	<form action="loginServer" method="post">
		Usuario: <input type="text" name="username" required /><br />
		Contraseña: <input type="password" name="password" required /><br /> <input
			type="submit" value="Entrar" />
	</form>
	<p>
		¿No tienes cuenta? <a href="Registro.jsp">Regístrate aquí</a>.
	</p>
</body>
</html>