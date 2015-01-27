package serviciosWeb;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.core.util.Base64;

import controladores.IControladorSitioInteres;
import controladores.IControladorUsuario;

@Path("/rest/sitiointeres")
public class ISitioInteres {

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/listarsitios")
	public String listarSitioInteresCercanos(@FormParam("latitud") int latitud,
			@FormParam("longitud") int longitud) throws JSONException {

		System.out.println("listarsitios");
		InitialContext ctx;

		JSONObject respuesta = new JSONObject();

		try {
			ctx = new InitialContext();
			IControladorSitioInteres icsitio = (IControladorSitioInteres) ctx
					.lookup("java:global/GeoRedUy-EAR/GeoRedUy-EJB/ControladorSitioInteres!controladores.IControladorSitioInteres");

			JSONArray jsitios = icsitio.listarSitiosInteresCercanos(latitud,
					longitud);

			respuesta.put("lista", jsitios);

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(respuesta);

		return respuesta.toString();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/checkin")
	public String checkIn(@FormParam("comentario") String comentario,
			@FormParam("mail") String mail, @FormParam("token") String token,
			@FormParam("sitio") String sitio, @FormParam("foto") String foto)
			throws JSONException {

		System.out.println("checkin");
		InitialContext ctx;

		JSONObject respuesta = new JSONObject();

		try {
			ctx = new InitialContext();
			IControladorSitioInteres icsitio = (IControladorSitioInteres) ctx
					.lookup("java:global/GeoRedUy-EAR/GeoRedUy-EJB/ControladorSitioInteres!controladores.IControladorSitioInteres");

			IControladorUsuario icusuario = (IControladorUsuario) ctx
					.lookup("java:global/GeoRedUy-EAR/GeoRedUy-EJB/ControladorUsuario!controladores.IControladorUsuario");

			
			if ((comentario != null) && (mail != null)&& (token != null) && (sitio != null)) {
				if (icusuario.usuarioValido(mail, token)) {
					byte[] f = null;
					if (foto!=null)
						f = Base64.decode(foto);
					boolean ret = icsitio.altaCheckIn(comentario, mail, token,
							sitio, f);
					if (ret)
						respuesta.put("mensaje", "Check-In creado con exito");
					else
						respuesta.put("mensaje", "Check-In no creado");
				}
			} else {
				respuesta.put("mensaje", "Parametros no especificados");
			}

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return respuesta.toString();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/listarultimoscheckins")
	public String listarUltimosCkeckInsAmigos(@FormParam("mail") String mail,@FormParam("token") String token,
			@FormParam("principio") int principio,@FormParam("fin") int fin)
					throws JSONException {

		System.out.println("listarcheckins");
		InitialContext ctx;

		JSONObject respuesta = new JSONObject();

		try {
			ctx = new InitialContext();
			IControladorSitioInteres icsitio = (IControladorSitioInteres) ctx
					.lookup("java:global/GeoRedUy-EAR/GeoRedUy-EJB/ControladorSitioInteres!controladores.IControladorSitioInteres");

			IControladorUsuario icusuario = (IControladorUsuario) ctx
					.lookup("java:global/GeoRedUy-EAR/GeoRedUy-EJB/ControladorUsuario!controladores.IControladorUsuario");

			JSONArray jcheckins = null;
			if ((mail != null) && (token != null)) {
				if (icusuario.usuarioValido(mail, token)){
					jcheckins = icsitio.listarUltimosCkeckInsAmigos(mail,principio,fin);
				}
				respuesta.put("lista", jcheckins);
			}
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return respuesta.toString();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/agregarComentarioCheckIn")
	public String agregarComentarioCheckIn(@FormParam("mail") String mail,
			@FormParam("token") String token, @FormParam("comentario") String comentario, 
			@FormParam("idCheckIn") int idCheckIn) throws JSONException {

		System.out.println("agregarComentarioCheckIn");
		InitialContext ctx;

		JSONObject respuesta = new JSONObject();

		try {
			ctx = new InitialContext();
			IControladorSitioInteres icsitio = (IControladorSitioInteres) ctx
					.lookup("java:global/GeoRedUy-EAR/GeoRedUy-EJB/ControladorSitioInteres!controladores.IControladorSitioInteres");

			IControladorUsuario icusuario = (IControladorUsuario) ctx
					.lookup("java:global/GeoRedUy-EAR/GeoRedUy-EJB/ControladorUsuario!controladores.IControladorUsuario");

			if ((mail != null) && (token != null)) {
				if (icusuario.usuarioValido(mail, token)){
					icsitio.agregarComentarioCheckIn(mail, idCheckIn, comentario);
					respuesta.put("mensaje", "Comentario creado correctamente");
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
	@Path("/verdatoscheckin")
	public String verDatosCheckIn(@FormParam("mail") String mail,
			@FormParam("token") String token, @FormParam("idCheckIn") int idCheckIn) throws JSONException {

		System.out.println("verdatoscheckin");
		InitialContext ctx;

		JSONObject respuesta = new JSONObject();

		try {
			ctx = new InitialContext();
			IControladorSitioInteres icsitio = (IControladorSitioInteres) ctx
					.lookup("java:global/GeoRedUy-EAR/GeoRedUy-EJB/ControladorSitioInteres!controladores.IControladorSitioInteres");

			IControladorUsuario icusuario = (IControladorUsuario) ctx
					.lookup("java:global/GeoRedUy-EAR/GeoRedUy-EJB/ControladorUsuario!controladores.IControladorUsuario");

			if ((mail != null) && (token != null)) {
				if (icusuario.usuarioValido(mail, token)){
					JSONObject ob = icsitio.verInfoCheckIn(idCheckIn);
					respuesta.put("checkin", ob);
					respuesta.put("mensaje", "Informacion enviada");
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
	@Path("/verInfoSitioInteres")
	public String verInfoSitioInteres(@FormParam("mail") String mail,
			@FormParam("token") String token, @FormParam("nombreSitio") String nombreSitio) throws JSONException {

		System.out.println("verInfoSitioInteres");
		InitialContext ctx;

		JSONObject respuesta = new JSONObject();

		try {
			ctx = new InitialContext();
			IControladorSitioInteres icsitio = (IControladorSitioInteres) ctx
					.lookup("java:global/GeoRedUy-EAR/GeoRedUy-EJB/ControladorSitioInteres!controladores.IControladorSitioInteres");

			IControladorUsuario icusuario = (IControladorUsuario) ctx
					.lookup("java:global/GeoRedUy-EAR/GeoRedUy-EJB/ControladorUsuario!controladores.IControladorUsuario");

			if ((mail != null) && (token != null) && (nombreSitio!=null)) {
				if (icusuario.usuarioValido(mail, token)){
					JSONObject ob = icsitio.verInfoSitioInteres(nombreSitio);
					respuesta.put("sitio", ob);
					respuesta.put("mensaje", "Informacion enviada");
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
