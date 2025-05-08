package servlets;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

@WebServlet("/registroServer")
public class RegistroUsuarioServlet extends HttpServlet {

    private static final String FILE_PATH = "C:/ruta/completa/usuarios.txt"; // Asegúrate de usar una ruta válida y accesible para el servidor.

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        String username = request.getParameter("usuario");
        String password = request.getParameter("clave");

        // Verificar que el usuario y la contraseña no estén vacíos
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            request.setAttribute("error", "Por favor, complete todos los campos.");
            request.getRequestDispatcher("Registro.jsp").forward(request, response);
            return;
        }

        // Leer los usuarios existentes para verificar si ya existe el nombre de usuario
        boolean usuarioExistente = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes[0].equals(username)) {
                    usuarioExistente = true;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Si el archivo no existe, lo creamos automáticamente
            if (!(new File(FILE_PATH).exists())) {
                try (FileWriter writer = new FileWriter(FILE_PATH, true)) {
                    writer.write("");  // Crear archivo vacío si no existe
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        // Si el usuario ya existe, redirigir al formulario de registro con error
        if (usuarioExistente) {
            request.setAttribute("error", "El nombre de usuario ya está registrado.");
            request.getRequestDispatcher("Registro.jsp").forward(request, response);
            return;
        }

        // Escribir el nuevo usuario en el archivo
        try (FileWriter writer = new FileWriter(FILE_PATH, true)) {
            writer.write(username + "," + password + "\n");
            System.out.println("Usuario registrado: " + username);  // Agregar depuración
        } catch (IOException e) {
            e.printStackTrace();
            request.setAttribute("error", "Ocurrió un error al registrar el usuario.");
            request.getRequestDispatcher("Registro.jsp").forward(request, response);
            return;
        }

        // Volver al login con mensaje de éxito
        request.setAttribute("registroExitoso", "Usuario registrado correctamente. Ahora puedes iniciar sesión.");
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
}
