package servlets;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

@WebServlet("/loginServer")
public class loginServer extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        String username = request.getParameter("usuario");
        String password = request.getParameter("clave");

        // Verificar el usuario y la contraseña
        boolean usuarioEncontrado = false;

        // Usar los usuarios inicializados en InicializadorUsuarios.java
        List<String[]> usuarios = (List<String[]>) getServletContext().getAttribute("usuarios");

        if (usuarios != null) {
            for (String[] usuario : usuarios) {
                // Verificar si el nombre de usuario y la contraseña coinciden
                if (usuario[0].equals(username) && usuario[1].equals(password)) {
                    usuarioEncontrado = true;
                    break;
                }
            }
        }

        if (usuarioEncontrado) {
            // Usuario encontrado, iniciar sesión
            request.getSession().setAttribute("username", username);
            response.sendRedirect("index.jsp");  // Redirigir a la página principal
        } else {
            // Usuario no encontrado o contraseña incorrecta
            request.setAttribute("error", "Nombre de usuario o contraseña incorrectos.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
