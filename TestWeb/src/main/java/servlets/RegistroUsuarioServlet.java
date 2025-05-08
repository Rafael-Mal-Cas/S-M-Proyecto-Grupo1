package servlets;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;

@WebServlet("/registroServer")
public class RegistroUsuarioServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        String username = request.getParameter("usuario");
        String password = request.getParameter("clave");

        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            request.setAttribute("error", "Por favor, complete todos los campos.");
            request.getRequestDispatcher("Registro.jsp").forward(request, response);
            return;
        }

        // Obtener la lista de usuarios del contexto
        ServletContext context = getServletContext();
        List<String[]> usuarios = (List<String[]>) context.getAttribute("usuarios");

        // Verificar si el usuario ya existe
        for (String[] usuario : usuarios) {
            if (usuario[0].equals(username)) {
                request.setAttribute("error", "El nombre de usuario ya está registrado.");
                request.getRequestDispatcher("Registro.jsp").forward(request, response);
                return;
            }
        }

        // Registrar el nuevo usuario en la lista
        usuarios.add(new String[]{username, password});
        context.setAttribute("usuarios", usuarios);  // opcional, por seguridad

        // Redirigir al login con mensaje
        request.setAttribute("registroExitoso", "Usuario registrado correctamente. Ahora puedes iniciar sesión.");
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
}
