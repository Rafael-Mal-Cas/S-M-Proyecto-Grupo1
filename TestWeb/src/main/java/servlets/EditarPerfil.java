package servlets;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import modelo.User;
import utils.DatabaseConnection;

@WebServlet("/editarPerfil")
@MultipartConfig(maxFileSize = 1024 * 1024 * 5) // Máximo 5MB
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

        String nombre = request.getParameter("nombre");
        String apellidos = request.getParameter("apellidos");
        String email = request.getParameter("email");
        String telefono = request.getParameter("telefono");
        String genero = request.getParameter("genero");

        Part imagenPart = request.getPart("imagen");
        String nombreImagen = usuario.getImagen();

        if (imagenPart != null && imagenPart.getSize() > 0) {
            String nombreArchivo = Paths.get(imagenPart.getSubmittedFileName()).getFileName().toString();
            String ruta = getServletContext().getRealPath("/imagenes/perfil");
            File directorio = new File(ruta);
            if (!directorio.exists()) {
                directorio.mkdirs();
            }

            String rutaCompleta = ruta + File.separator + nombreArchivo;
            imagenPart.write(rutaCompleta);
            nombreImagen = "imagenes/perfil/" + nombreArchivo;
        }

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

            usuario.setNombre(nombre);
            usuario.setApellidos(apellidos);
            usuario.setEmail(email);
            usuario.setNumeroTelefono(telefono);
            usuario.setGenero(genero);
            usuario.setImagen(nombreImagen);

            session.setAttribute("usuario", usuario);

            // ───────────── REGISTRO EN LOG ─────────────
            String rutaLog = getServletContext().getRealPath("/logs/acciones.log");
            File archivoLog = new File(rutaLog);
            archivoLog.getParentFile().mkdirs();

            try (FileWriter writer = new FileWriter(archivoLog, true)) {
                String linea = String.format("Usuario con ID %d editó su perfil (%s)%n",
                        usuario.getId(), LocalDateTime.now());
                writer.write(linea);
            } catch (IOException ex) {
                ex.printStackTrace(); // Solo log en consola si hay error
            }

            response.sendRedirect(request.getContextPath() + "/Cuenta.jsp");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/error.jsp");
        }
    }
}
