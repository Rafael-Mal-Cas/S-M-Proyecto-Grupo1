<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Registro de usuario</title>
    <link rel="stylesheet" type="text/css" href="Style/Style_index.css">
    <link rel="stylesheet" type="text/css" href="Style/Style_registro.css">
</head>
<body>
    <main class="contenido">
        <div class="card form-container">
            <h1>Registro de nuevo usuario</h1>
            
            <%-- Mostrar mensajes de error --%>
            <% if (request.getAttribute("error") != null) { %>
                <div class="error-message">
                    <%= request.getAttribute("error") %>
                </div>
            <% } %>
            
            <%-- Mostrar mensajes de éxito --%>
            <% if (session.getAttribute("registroExitoso") != null) { %>
                <div class="success-message">
                    <%= session.getAttribute("registroExitoso") %>
                </div>
                <% session.removeAttribute("registroExitoso"); %>
            <% } %>
            
            <form action="registroUsuario" method="post" onsubmit="return validarFormulario()">
                <div class="form-group">
                    <label for="nombre" class="required">Nombre:</label>
                    <input type="text" id="nombre" name="nombre" required
                           value="<%= request.getParameter("nombre") != null ? request.getParameter("nombre") : "" %>">
                </div>
                
                <div class="form-group">
                    <label for="apellidos" class="required">Apellidos:</label>
                    <input type="text" id="apellidos" name="apellidos" required
                           value="<%= request.getParameter("apellidos") != null ? request.getParameter("apellidos") : "" %>">
                </div>
                
                <div class="form-group">
                    <label for="genero" class="required">Género:</label>
                    <select id="genero" name="genero" required>
                        <option value="">Seleccione...</option>
                        <option value="male" <%= "male".equals(request.getParameter("genero")) ? "selected" : "" %>>Masculino</option>
                        <option value="female" <%= "female".equals(request.getParameter("genero")) ? "selected" : "" %>>Femenino</option>
                        <option value="other" <%= "other".equals(request.getParameter("genero")) ? "selected" : "" %>>Otro</option>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="email" class="required">Email:</label>
                    <input type="email" id="email" name="email" required
                           value="<%= request.getParameter("email") != null ? request.getParameter("email") : "" %>">
                </div>
                
                <div class="form-group">
                    <label for="numeroTelefono" class="required">Teléfono:</label>
                    <input type="tel" id="numeroTelefono" name="numeroTelefono" required
                           pattern="[+]{0,1}[0-9\\s-]{6,}" 
                           title="Formato: +[código país] [número] (ej. +34 912345678)"
                           value="<%= request.getParameter("numeroTelefono") != null ? request.getParameter("numeroTelefono") : "" %>">
                </div>
                
                <div class="form-group">
                    <label for="usuario" class="required">Nombre de usuario:</label>
                    <input type="text" id="usuario" name="usuario" required 
                           pattern="[a-zA-Z0-9]+"
                           title="Solo letras y números, sin espacios"
                           value="<%= request.getParameter("usuario") != null ? request.getParameter("usuario") : "" %>">
                </div>
                
                <div class="form-group">
                    <label for="contrasena" class="required">Contraseña:</label>
                    <input type="password" id="contrasena" name="contrasena" required
                           title="Mínimo 8 caracteres">
                </div>
                
                <div class="form-group">
                    <label for="confirmar_contrasena" class="required">Confirmar contraseña:</label>
                    <input type="password" id="confirmar_contrasena" name="confirmar_contrasena" required>
                </div>
                
                <div class="form-group">
                    <button type="submit">Registrarse</button>
                </div>
            </form>

            <p style="margin-top: 15px; text-align: center;">
                ¿Ya tienes cuenta? <a href="login.jsp">Inicia sesión</a>.
            </p>
        </div>
    </main>

    <script>
        function validarFormulario() {
            // Validar que las contraseñas coincidan
            const contrasena = document.getElementById("contrasena").value;
            const confirmacion = document.getElementById("confirmar_contrasena").value;
            
            if (contrasena !== confirmacion) {
                alert("Las contraseñas no coinciden");
                return false;
            }
            
            // Validar fortaleza de contraseña (opcional)
            if (contrasena.length < 8) {
                alert("La contraseña debe tener al menos 8 caracteres");
                return false;
            }
            
            return true;
        }
    </script>
</body>
</html>
