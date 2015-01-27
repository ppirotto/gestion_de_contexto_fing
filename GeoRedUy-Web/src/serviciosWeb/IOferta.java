package serviciosWeb;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import controladores.IControladorOferta;
import controladores.IControladorUsuario;

@Path("/rest/ofertas")
public class IOferta {

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/comprarOferta")
	public String comprarOferta(@FormParam("mail") String mail,
			@FormParam("token") String token, @FormParam("idOferta") int idOferta) throws JSONException {

		InitialContext ctx;

		JSONObject respuesta = new JSONObject();

		try {
			ctx = new InitialContext();

			IControladorOferta icoferta = (IControladorOferta) ctx
					.lookup("java:global/GeoRedUy-EAR/GeoRedUy-EJB/ControladorOferta!controladores.IControladorOferta");

			IControladorUsuario icusuario = (IControladorUsuario) ctx
					.lookup("java:global/GeoRedUy-EAR/GeoRedUy-EJB/ControladorUsuario!controladores.IControladorUsuario");


			if ((mail != null) && (token != null)) {

				if (icusuario.usuarioValido(mail, token)) {
					icoferta.comprarOferta(mail, idOferta);
					respuesta.put("mensaje", "Oferta comprada correctamente");
				}else
					respuesta.put("mensaje", "Usuario invalido");
			}else
				respuesta.put("mensaje", "Parametros no especificados");

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(respuesta);

		return respuesta.toString();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/usuarioComproOferta")
	public String usuarioComproOferta(@FormParam("mail") String mail,
			@FormParam("token") String token, @FormParam("idOferta") int idOferta) throws JSONException {

		InitialContext ctx;

		JSONObject respuesta = new JSONObject();

		try {
			ctx = new InitialContext();

			IControladorOferta icoferta = (IControladorOferta) ctx
					.lookup("java:global/GeoRedUy-EAR/GeoRedUy-EJB/ControladorOferta!controladores.IControladorOferta");

			IControladorUsuario icusuario = (IControladorUsuario) ctx
					.lookup("java:global/GeoRedUy-EAR/GeoRedUy-EJB/ControladorUsuario!controladores.IControladorUsuario");


			if ((mail != null) && (token != null)) {

				if (icusuario.usuarioValido(mail, token)) {
					boolean res = icoferta.usuarioComproOferta(mail, idOferta);
					respuesta.put("resultado", res);
					respuesta.put("mensaje", "La oferta fue comprada");
				}else
					respuesta.put("mensaje", "Usuario invalido");
			}else
				respuesta.put("mensaje", "Parametros no especificados");

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(respuesta);

		return respuesta.toString();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/evaluarOferta")
	public String evaluarOferta(@FormParam("mail") String mail,
			@FormParam("token") String token, @FormParam("idOferta") int idOferta,
			@FormParam("evaluacion") int evaluacion) throws JSONException {

		InitialContext ctx;

		JSONObject respuesta = new JSONObject();

		try {
			ctx = new InitialContext();

			IControladorOferta icoferta = (IControladorOferta) ctx
					.lookup("java:global/GeoRedUy-EAR/GeoRedUy-EJB/ControladorOferta!controladores.IControladorOferta");

			IControladorUsuario icusuario = (IControladorUsuario) ctx
					.lookup("java:global/GeoRedUy-EAR/GeoRedUy-EJB/ControladorUsuario!controladores.IControladorUsuario");


			if ((mail != null) && (token != null)) {

				if (icusuario.usuarioValido(mail, token)) {
					icoferta.evaluarOferta(idOferta, evaluacion);
					respuesta.put("mensaje", "Oferta evaluada correctamente");
				}else
					respuesta.put("mensaje", "Usuario invalido");
			}else
				respuesta.put("mensaje", "Parametros no especificados");

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(respuesta);

		return respuesta.toString();
	}

}
