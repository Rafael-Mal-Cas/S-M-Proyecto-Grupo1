package utils;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import modelo.User;
import org.mindrot.jbcrypt.BCrypt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet("/verificarClave")
public class VerificarClave extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(VerificarClave.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.debug("Inicio de verificación de contraseña");
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();

        User usuario = (User) session.getAttribute("usuario");
        if (usuario == null) {
            logger.warn("Usuario no autenticado intentando verificar contraseña");
            response.sendRedirect("login.jsp");
            return;
        }

        String passwordInput = request.getParameter("password");
        String hashGuardado = usuario.getContrasena();

        if (BCrypt.checkpw(passwordInput, hashGuardado)) {
            logger.info("Contraseña verificada correctamente para usuario ID: " + usuario.getId());
            session.setAttribute("claveVerificada", passwordInput);
            response.sendRedirect("Cuenta.jsp?status=ok");
        } else {
            logger.warn("Intento fallido de verificación de contraseña para usuario ID: " + usuario.getId());
            response.sendRedirect("Cuenta.jsp?status=error");
        }
    }
}
