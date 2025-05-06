<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
    <title>Cerrar Sesión</title>
</head>
<body>
    <%
        session.invalidate();  // Cierra la sesión
        response.sendRedirect("login.jsp");  // Redirige a login
    %>
</body>
</html>
