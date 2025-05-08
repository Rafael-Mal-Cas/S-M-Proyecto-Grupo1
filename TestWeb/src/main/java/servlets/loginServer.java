package servlets;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

/**
 * Servlet encargado de gestionar las peticiones de inicio de sesión.
 * Recibe los datos del formulario de login y los compara con una lista de usuarios
 * previamente almacenada en el contexto de la aplicación.
 */
@WebServlet("/loginServer")
public class loginServer extends HttpServlet {

    /**
     * Método que se ejecuta cuando se realiza una petición POST al servlet,
     * normalmente desde un formulario de inicio de sesión.
     *
     * @param request  Objeto que contiene la solicitud del cliente.
     * @param response Objeto que permite responder al cliente.
     * @throws ServletException Si ocurre un error en la ejecución del servlet.
     * @throws IOException Si ocurre un error de entrada/salida.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        // Obtener los parámetros enviados desde el formulario (nombre de usuario y contraseña)
        String username = request.getParameter("usuario");
        String password = request.getParameter("clave");

        // Variable para indicar si el usuario ha sido encontrado y autenticado correctamente
        boolean usuarioEncontrado = false;

        // Obtener la lista de usuarios almacenada en el contexto de la aplicación
        List<String[]> usuarios = (List<String[]>) getServletContext().getAttribute("usuarios");

        // Comprobar si la lista existe y buscar coincidencia con los datos introducidos
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
            // Si los datos son correctos, se guarda el nombre de usuario en la sesión
            // y se redirige al usuario a la página principal (index.jsp)
            request.getSession().setAttribute("username", username);
            response.sendRedirect("index.jsp");
        } else {
            // Si los datos son incorrectos, se establece un atributo de error
            // y se vuelve a mostrar la página de login con el mensaje correspondiente
            request.setAttribute("error", "Nombre de usuario o contraseña incorrectos.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
