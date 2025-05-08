<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Formulario de Registro</title>
</head>
<body>
    <h2>Registro de nuevo usuario</h2>
    <form action="registroServer" method="post">
        Usuario: <input type="text" name="usuario" required /><br/>
        Contraseña: <input type="password" name="clave" required /><br/>
        <input type="submit" value="Registrar" />
    </form>

</body>
</html>
