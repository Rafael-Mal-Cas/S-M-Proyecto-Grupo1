<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Iniciar Sesión</title>
    <link rel="stylesheet" type="text/css" href="Style/Style_index.css">
    <link rel="icon" type="image/png" href="Style/logo_blanco.png" />
</head>
<body>
    <main class="contenido">
        <div class="card form-container">
            <h1>Iniciar Sesión</h1>
            <%
                // Mostrar error si existe
                String error = (String) request.getAttribute("error");
                if (error != null) {
            %>
                <p style="color: red; margin-top: 10px;"><%= error %></p>
            <%
                }
            %>
            <form action="loginServer" method="post">
                <label for="usuario">Usuario/Email:</label>
                <input type="text" id="usuario" name="usuario" required /><br/>
                <label for="clave">Contraseña:</label>
                <input type="password" id="clave" name="clave" required /><br/>
                <input type="submit" value="Entrar">
            </form>
            <p style="margin-top: 15px;">
                ¿No tienes cuenta? <a href="Registro.jsp">Regístrate aquí</a>.
            </p>
        </div>
    </main>
</body>
</html>