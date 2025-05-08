<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
    <title>Login</title>
</head>
<body>
    <h1>Iniciar Sesión</h1>

    <% 
        // Mostrar error si existe
        String error = (String) request.getAttribute("error");
        if (error != null) { 
    %>
        <p style="color: red;"><%= error %></p>
    <% 
        }
    %>

    <h2>Iniciar sesión</h2>
    <form action="loginServer" method="post">
        Usuario: <input type="text" name="usuario" required /><br/>
        Contraseña: <input type="password" name="clave" required /><br/>
        <button type="submit">Entrar</button>
    </form>

    <p>
        ¿No tienes cuenta? <a href="Registro.jsp">Regístrate aquí</a>.
    </p>

</body>
</html>
