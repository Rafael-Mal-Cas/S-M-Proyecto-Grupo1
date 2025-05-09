package servlets;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import modelo.User;

@WebServlet("/registroServer")
public class RegistroUsuarioServlet extends HttpServlet {
    private AtomicInteger idCounter;

    @Override
    public void init() throws ServletException {
        idCounter = new AtomicInteger(0);
        ServletContext context = getServletContext();
        if (context.getAttribute("usuarios") == null) {
            // Usamos CopyOnWriteArrayList para seguridad en entornos multi-hilo
            context.setAttribute("usuarios", new CopyOnWriteArrayList<User>());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Recoger y sanitizar parámetros
            String nombre = sanitizeInput(request.getParameter("nombre"));
            String apellidos = sanitizeInput(request.getParameter("apellidos"));
            String genero = sanitizeInput(request.getParameter("genero"));
            String email = sanitizeInput(request.getParameter("email")).toLowerCase();
            String numeroTelefono = sanitizeInput(request.getParameter("numeroTelefono"));
            String usuario = sanitizeInput(request.getParameter("usuario"));
            String contrasena = request.getParameter("contrasena"); // No sanitizar contraseña

            // Validación de campos obligatorios
            if (nombre == null || nombre.isEmpty() || 
                usuario == null || usuario.isEmpty() || 
                contrasena == null || contrasena.isEmpty() || 
                email == null || email.isEmpty()) {
                
                setErrorAndRedirect(request, response, "Por favor, complete los campos obligatorios.");
                return;
            }

            // Validación de formato de email
            if (!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                setErrorAndRedirect(request, response, "Por favor, ingrese un email válido.");
                return;
            }

            // Validación de fortaleza de contraseña
            if (contrasena.length() < 8) {
                setErrorAndRedirect(request, response, "La contraseña debe tener al menos 8 caracteres.");
                return;
            }

            // Obtener la lista de usuarios de forma segura
            ServletContext context = getServletContext();
            List<User> usuarios = (List<User>) context.getAttribute("usuarios");

            // Verificar unicidad de usuario y email
            if (usuarioExiste(usuarios, usuario, email)) {
                return;
            }

            // Crear y guardar nuevo usuario
            User nuevoUsuario = crearUsuario(nombre, apellidos, genero, email, 
                                          numeroTelefono, usuario, contrasena);
            usuarios.add(nuevoUsuario);

            // Éxito - redirigir al login
            request.getSession().setAttribute("registroExitoso", 
                "Registro completado con éxito. Ahora puedes iniciar sesión.");
            response.sendRedirect("login.jsp");
            
        } catch (Exception e) {
            // Loggear error
            e.printStackTrace();
            setErrorAndRedirect(request, response, "Ocurrió un error inesperado. Por favor, intente nuevamente.");
        }
    }

    // Métodos auxiliares
    private boolean usuarioExiste(List<User> usuarios, String usuario, String email) {
        return usuarios.stream()
                .anyMatch(u -> u.getUsuario().equalsIgnoreCase(usuario) || 
                       u.getEmail().equalsIgnoreCase(email));
    }

    private User crearUsuario(String nombre, String apellidos, String genero, 
                            String email, String numeroTelefono, 
                            String usuario, String contrasena) {
        return new User(
            idCounter.incrementAndGet(),
            nombre,
            apellidos,
            genero,
            email,
            numeroTelefono,
            usuario,
            contrasena // En producción, debería ser un hash de la contraseña
        );
    }

    private void setErrorAndRedirect(HttpServletRequest request, 
                                   HttpServletResponse response, 
                                   String mensaje) 
            throws ServletException, IOException {
        request.setAttribute("error", mensaje);
        request.getRequestDispatcher("Registro.jsp").forward(request, response);
    }

    private String sanitizeInput(String input) {
        if (input == null) return null;
        return input.trim().replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    }
}