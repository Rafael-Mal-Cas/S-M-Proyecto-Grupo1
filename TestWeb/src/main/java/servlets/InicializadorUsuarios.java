package servlets;

import javax.servlet.*;
import javax.servlet.annotation.WebListener;
import java.util.*;
import modelo.User;

@WebListener
public class InicializadorUsuarios implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Crear una lista de usuarios en memoria usando la clase User
        List<User> usuarios = new ArrayList<>();
        
        // Agregar usuarios iniciales con todos los campos
        usuarios.add(new User(1, "Emily", "Johnson", "female", 
                            "emily.johnson@x.dummyjson.com", "+81 965-431-3024", 
                            "emilys", "emilyspass"));
        
        usuarios.add(new User(2, "Michael", "Williams", "male", 
                            "michael.williams@x.dummyjson.com", "+49 258-627-6644", 
                            "michaelw", "michaelwpass"));
        
        usuarios.add(new User(3, "Sophia", "Brown", "female", 
                            "sophia.brown@x.dummyjson.com", "+81 210-652-2785", 
                            "sophiab", "sophiabpass"));
        
        usuarios.add(new User(4, "James", "Davis", "male", 
                            "james.davis@x.dummyjson.com", "+49 614-958-9364", 
                            "jamesd", "jamesdpass"));
        
        usuarios.add(new User(5, "Emma", "Miller", "female", 
                            "emma.miller@x.dummyjson.com", "+91 759-776-1614", 
                            "emmaj", "emmajpass"));
        
        // Puedes agregar más usuarios aquí según sea necesario
        // ...
        
        // Almacenar los usuarios en el contexto de la aplicación
        sce.getServletContext().setAttribute("usuarios", usuarios);
        
        // También almacenamos un contador de IDs para nuevos registros
        sce.getServletContext().setAttribute("idCounter", usuarios.size());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Limpiar recursos si es necesario
        sce.getServletContext().removeAttribute("usuarios");
        sce.getServletContext().removeAttribute("idCounter");
    }
}