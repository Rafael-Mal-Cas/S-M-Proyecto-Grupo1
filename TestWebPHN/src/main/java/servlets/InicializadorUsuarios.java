package servlets;

import javax.servlet.*;
import javax.servlet.annotation.WebListener;
import java.util.*;

/**
 * Clase que actúa como un listener para el contexto de la aplicación web.
 * Su función principal es inicializar una lista de usuarios cuando se inicia la aplicación.
 */
@WebListener
public class InicializadorUsuarios implements ServletContextListener {

    /**
     * Método que se ejecuta automáticamente cuando se inicia la aplicación web.
     * Aquí se crea una lista de usuarios y se almacena en el contexto de la aplicación.
     *
     * @param sce Evento que representa el inicio del contexto de la aplicación.
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Crear una lista en memoria para almacenar los usuarios.
        // Cada usuario está representado por un array de String: [nombre, contraseña].
        List<String[]> usuarios = new ArrayList<>();
        
        // Agregar algunos usuarios iniciales (nombre de usuario y contraseña).
        // Estas contraseñas están representadas aquí como texto plano (por motivos educativos).
        usuarios.add(new String[] {"ana", "NuBG4O"});
        usuarios.add(new String[] {"jose", "qGUpfT"});
        usuarios.add(new String[] {"maria", "$5szY9"});
        
        // Guardar la lista de usuarios en el contexto de la aplicación.
        // Esto permite que cualquier servlet o JSP pueda acceder a estos datos.
        sce.getServletContext().setAttribute("usuarios", usuarios);
    }

    /**
     * Método que se ejecuta automáticamente cuando se destruye el contexto de la aplicación.
     * En este caso no se requiere realizar ninguna acción.
     *
     * @param sce Evento que representa la destrucción del contexto de la aplicación.
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // No se realiza ninguna acción al destruir el contexto
    }
}
