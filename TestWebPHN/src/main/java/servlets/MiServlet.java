package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Servlet simple que recibe datos desde una petición POST,
 * los modifica y los reenvía a una página JSP.
 */
@WebServlet("/MiServlet") 
public class MiServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * Constructor del servlet. No realiza ninguna acción adicional.
     */
    public MiServlet() {
        super();
    }

	/**
	 * Método que responde a las peticiones GET.
	 * En este caso, devuelve un mensaje simple con el contexto de la aplicación.
	 *
	 * @param request  Solicitud del cliente.
	 * @param response Respuesta que se enviará al cliente.
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * Método que responde a las peticiones POST.
	 * Toma un parámetro llamado "miDato", lo modifica agregando texto adicional,
	 * lo almacena como atributo en la solicitud, y redirige la solicitud a una página JSP.
	 *
	 * @param request  Solicitud del cliente.
	 * @param response Respuesta que se enviará al cliente.
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		// Obtener el parámetro "miDato" enviado desde el formulario o cliente
		String miDato = request.getParameter("miDato");
		
		// Modificar el dato recibido agregando un texto adicional
		miDato += " Modifico el dato";
		
		// Guardar el dato modificado como atributo en la solicitud
		request.setAttribute("miDato", miDato);
		
		// Reenviar la solicitud y la respuesta a la página index.jsp
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}
}
