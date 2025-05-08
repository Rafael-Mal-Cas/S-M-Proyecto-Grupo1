<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
    <title>Cerrar Sesión</title>
</head>
<body>
	<%@ page contentType="text/html;charset=UTF-8" language="java" %>
	<%
	    session.invalidate(); // Invalida la sesión
	    response.sendRedirect("index.jsp?logout=1"); // Redirige con indicador de logout
	%>
</body>
</html>
