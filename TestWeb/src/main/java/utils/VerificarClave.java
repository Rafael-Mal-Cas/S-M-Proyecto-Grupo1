package utils;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import modelo.User;
import org.mindrot.jbcrypt.BCrypt;

@WebServlet("/verificarClave")
public class VerificarClave extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();

        // Recuperar el usuario autenticado
        User usuario = (User) session.getAttribute("usuario");
        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Obtener la contraseña introducida
        String passwordInput = request.getParameter("password");
        // Contraseña encriptada guardada (desde BD en el objeto `User`)
        String hashGuardado = usuario.getContrasena(); // debe ser el hash en BCrypt

        // Comparar usando BCrypt
        if (BCrypt.checkpw(passwordInput, hashGuardado)) {
            // Coincide -> almacenar temporalmente la contraseña desencriptada en sesión
            session.setAttribute("claveVerificada", passwordInput);
            response.sendRedirect("Cuenta.jsp?status=ok");
        } else {
            // No coincide -> error
            response.sendRedirect("Cuenta.jsp?status=error");
        }
    }
}
