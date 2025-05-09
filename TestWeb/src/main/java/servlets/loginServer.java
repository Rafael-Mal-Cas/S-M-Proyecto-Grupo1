package servlets;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import modelo.User;

@WebServlet("/loginServer")
public class loginServer extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("usuario");
        String password = request.getParameter("clave");

        // Obtener la lista de usuarios del contexto
        ServletContext context = getServletContext();
        List<User> usuarios = (List<User>) context.getAttribute("usuarios");

        // Verificar credenciales
        User usuarioAutenticado = null;
        
        if (usuarios != null && username != null && password != null) {
            for (User usuario : usuarios) {
                if (usuario != null && username.equals(usuario.getUsuario())) {
                    // Encontrado el usuario, ahora verificar contraseña
                    if (password.equals(usuario.getContrasena())) {
                        usuarioAutenticado = usuario;
                    }
                    break; // Salir del bucle una vez encontrado el usuario
                }
            }
        }

        if (usuarioAutenticado != null) {
            // Autenticación exitosa
            HttpSession session = request.getSession();
            session.setAttribute("usuario", usuarioAutenticado);
            session.setAttribute("username", usuarioAutenticado.getUsuario());
            
            // Redirigir a página principal
            response.sendRedirect("index.jsp");
        } else {
            // Autenticación fallida
            request.setAttribute("error", "Nombre de usuario o contraseña incorrectos.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}