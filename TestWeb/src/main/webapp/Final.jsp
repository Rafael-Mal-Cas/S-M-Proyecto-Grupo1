<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
    <title>Final - Login Exitoso</title>
</head>
<body>
    <h1>Bienvenido a la página final, <%= session.getAttribute("username") %>!</h1>
    <p>Has iniciado sesión correctamente.</p>
    <a href="logout.jsp">Cerrar sesión</a>
</body>
</html>

