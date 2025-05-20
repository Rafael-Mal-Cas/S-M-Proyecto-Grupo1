package servlets;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import modelo.User;
import utils.DatabaseConnection;

@WebServlet("/editarPerfil")
@MultipartConfig(maxFileSize = 1024 * 1024 * 5) // Máximo 5MB para subir imagen
public class EditarPerfil extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        User usuario = (User) session.getAttribute("usuario");

        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Obtener datos del formulario
        String nombre = request.getParameter("nombre");
        String apellidos = request.getParameter("apellidos");
        String email = request.getParameter("email");
        String telefono = request.getParameter("telefono");
        String genero = request.getParameter("genero");

        // Procesar la imagen (si se ha subido una nueva)
        Part imagenPart = request.getPart("imagen");
        String nombreImagen = usuario.getImagen(); // Por defecto, mantiene la actual

        if (imagenPart != null && imagenPart.getSize() > 0) {
            String nombreArchivo = Paths.get(imagenPart.getSubmittedFileName()).getFileName().toString();
            String ruta = getServletContext().getRealPath("/imagenes/perfil");
            File directorio = new File(ruta);
            if (!directorio.exists()) {
                directorio.mkdirs();
            }

            String rutaCompleta = ruta + File.separator + nombreArchivo;
            imagenPart.write(rutaCompleta);

            // Guardamos la ruta relativa para la base de datos y para mostrarla
            nombreImagen = "imagenes/perfil/" + nombreArchivo;
        }

        // Actualizar datos en base de datos
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "UPDATE usuarios_simplificados SET nombre=?, apellidos=?, email=?, numeroTelefono=?, genero=?, imagen=? WHERE id=?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, nombre);
                stmt.setString(2, apellidos);
                stmt.setString(3, email);
                stmt.setString(4, telefono);
                stmt.setString(5, genero);
                stmt.setString(6, nombreImagen);
                stmt.setInt(7, usuario.getId());
                stmt.executeUpdate();
            }

            // Actualizamos el usuario en sesión con los datos nuevos
            usuario.setNombre(nombre);
            usuario.setApellidos(apellidos);
            usuario.setEmail(email);
            usuario.setNumeroTelefono(telefono);
            usuario.setGenero(genero);
            usuario.setImagen(nombreImagen);

            session.setAttribute("usuario", usuario);

            // Redirigimos a la página de perfil para ver los cambios
            response.sendRedirect(request.getContextPath() + "/Cuenta.jsp");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/error.jsp");
        }
    }
}
