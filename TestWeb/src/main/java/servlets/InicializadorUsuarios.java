package servlets;

import javax.servlet.*;
import javax.servlet.annotation.WebListener;
import java.util.*;

@WebListener
public class InicializadorUsuarios implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Crear una lista de usuarios en memoria
        List<String[]> usuarios = new ArrayList<>();
        
        // Agregar algunos  usuarios iniciales (puedes cambiar esto o agregar más)
        usuarios.add(new String[] {"ana", "NuBG4O"});
        usuarios.add(new String[] {"jose", "qGUpfT"});
        usuarios.add(new String[] {"maria", "$5szY9"});
        
        // Almacenar los usuarios en el contexto de la aplicación
        sce.getServletContext().setAttribute("usuarios", usuarios);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // No es necesario hacer nada aquí en este caso
    }
}
