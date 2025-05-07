/**
 * Esta es una versión personalizada y no funcional de la anotación @WebServlet.
 * 
 * ⚠️ ADVERTENCIA: Esta anotación NO sustituye a la original de Java EE:
 * javax.servlet.annotation.WebServlet
 * 
 * Se deja aquí como ejemplo de cómo crear anotaciones personalizadas en Java,
 * pero no debe usarse para registrar Servlets reales.
 * 
 * Uso educativo únicamente.
 */
package servlets;

import java.lang.annotation.Documented;

/**
 * Anotación personalizada de ejemplo que simula @WebServlet.
 * No tiene efecto real en la configuración del servlet.
 */
@Documented
public @interface WebServlet {
    /**
     * Define el valor principal (como una URL de mapeo).
     * En la anotación real se usaría urlPatterns o value.
     *
     * @return cadena con el valor (por ejemplo, "/mipath")
     */
    String value();
}
