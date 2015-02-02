package servlets;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import otros.Imagen;

import controladores.IControladorEmpresa;
import controladores.IControladorSitioInteres;

/**
 * Servlet implementation class ServletImagen
 */
@WebServlet(name = "ServletImagen", urlPatterns = { "/ServletImagen" })
public class ServletImagen extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@EJB
	private IControladorEmpresa icEmpresa;

	@EJB
	private IControladorSitioInteres icSitio;

	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String tipo = request.getParameter("tipo");
		System.out.println("Tipo del servlet" + tipo);

		Imagen imagen = null;
		if (tipo != null && tipo.equals("Sitio")) {

		} else if (tipo != null ) {
			//String nombreEmpresa = request.getParameter("nombre");
			//if (nombreEmpresa != null) {

				System.out.println("nombre empresa del servlet" + tipo);
				imagen = icEmpresa.obtenerLogo(tipo);
				if (imagen != null) {
					response.reset();
					response.setContentType("image/jpeg");
					response.setContentType("image/jpg");
					response.setContentType("image/png");
					response.setHeader("Content-Length",
							String.valueOf(imagen.getImagen().length));
					response.setHeader("Content-Disposition",
							"inline; filename=\"" + imagen.getNombre() + "\"");

					ServletOutputStream outputStream = response
							.getOutputStream();
					outputStream.write(imagen.getImagen());
					outputStream.close();
				}
			//}
		}

		response.setStatus(HttpServletResponse.SC_FORBIDDEN);

	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ServletImagen() {

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

}
