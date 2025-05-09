<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Registro de usuario</title>
    <link rel="stylesheet" type="text/css" href="Style/Style_index.css">
</head>
<body>
    <main class="contenido">
        <div class="card form-container">
            <h1>Registro de nuevo usuario</h1>
            <form action="registroServer" method="post">
                <label for="usuario">Usuario:</label>
                <input type="text" id="usuario" name="usuario" required /><br/>
                <label for="clave">Contraseña:</label>
                <input type="password" id="clave" name="clave" required /><br/>
                <input type="submit" value="Registrarse">
            </form>
            <p style="margin-top: 15px;">
                ¿Ya tienes cuenta? <a href="login.jsp">Inicia sesión</a>.
            </p>
        </div>
    </main>
</body>
</html>