package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Importación de BCrypt, aunque no se usa en este servlet actualmente
import org.mindrot.jbcrypt.BCrypt;

/**
 * Servlet básico que demuestra cómo manejar peticiones GET y POST.
 * Está mapeado a la URL "/MiServlet".
 * 
 * Nota: Aunque BCrypt está importado, no se utiliza en el código actual.
 */
@WebServlet("/MiServlet") 
public class MiServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Constructor por defecto del servlet.
     * No realiza ninguna acción adicional.
     */
    public MiServlet() {
        super();
        // Constructor generado automáticamente
    }

	/**
	 * Método que se ejecuta cuando se realiza una petición GET a este servlet.
	 * En este caso, simplemente responde con un mensaje que indica la ruta del contexto.
	 *
	 * @param request  Petición HTTP del cliente.
	 * @param response Respuesta HTTP para el cliente.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
	        throws ServletException, IOException {
		// Escribe un mensaje simple en la respuesta HTTP
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * Método que se ejecuta cuando se realiza una petición POST al servlet.
	 * Recupera un parámetro llamado "miDato", lo modifica, lo guarda como atributo
	 * y reenvía la petición a "index.jsp".
	 *
	 * @param request  Petición HTTP con los datos enviados por el cliente.
	 * @param response Respuesta HTTP al cliente.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
	        throws ServletException, IOException {
		
		// Obtener el parámetro "miDato" enviado desde un formulario
		String miDato = request.getParameter("miDato");

		// Modificar el valor del dato (concatenar un texto)
		miDato += " Modifico el dato";

		// Guardar el dato como atributo para usarlo en la JSP
		request.setAttribute("miDato", miDato);

		// Reenviar la petición a "index.jsp" con el dato modificado
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}
}
