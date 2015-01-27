package serviciosWeb;

import java.io.IOException;

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

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

import controladores.IControladorUsuario;

@Path("/rest/usuarios")
public class IUsuario {

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/verperfil")
	public String verPerfil(@FormParam("mail") String mail,
			@FormParam("token") String token,
			@FormParam("mailContacto") String mailContacto)
			throws JSONException {

		
		System.out.println("verperfil");
		InitialContext ctx;

		JSONObject respuesta = new JSONObject();

		try {
			ctx = new InitialContext();
			IControladorUsuario icusuario = (IControladorUsuario) ctx
					.lookup("java:global/GeoRedUy-EAR/GeoRedUy-EJB/ControladorUsuario!controladores.IControladorUsuario");

			JSONObject usuario = null;
			if ((mail != null) && (token != null) && (mailContacto != null)) {

				if (icusuario.usuarioValido(mail, token))
					usuario = icusuario.verPerfil(mail, token, mailContacto);
			}
			respuesta.put("perfil", usuario);

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(respuesta);

		return respuesta.toString();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/nuevasolicitud")
	public String nuevaSolicitud(@FormParam("mail") String mail,
			@FormParam("token") String token,
			@FormParam("mailContacto") String mailContacto)
			throws JSONException {

		System.out.println("nuevasolicitud");
		InitialContext ctx;

		JSONObject respuesta = new JSONObject();

		try {
			ctx = new InitialContext();
			IControladorUsuario icusuario = (IControladorUsuario) ctx
					.lookup("java:global/GeoRedUy-EAR/GeoRedUy-EJB/ControladorUsuario!controladores.IControladorUsuario");

			boolean res = false;

			if ((mail != null) && (token != null) && (mailContacto != null)) {

				if (icusuario.usuarioValido(mail, token)) {
					res = icusuario.nuevaSolicitud(mail, token, mailContacto);
					if (icusuario.obtenerTokenCloud(mailContacto, token)!= null){
						Sender sender = new Sender(PuntoEntradaWS.getApikeycloud());
						Message message = new Message.Builder()
								.addData("titulo", "Nueva solicitud de amistad")
								.addData("descripcion",
										icusuario.obtenerNombre(mail, token)+ " quiere ser tu amigo!")
								.addData("tipo", "SolicitudDeAmistad")
								.addData("id", mail)
								.build();
	
						try {
							sender.send(message, icusuario.obtenerTokenCloud(mailContacto, token), 1);
						} catch (IOException e) {
	
							e.printStackTrace();
						}
					}
				}
			}
			respuesta.put("resultado", res);

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(respuesta);

		return respuesta.toString();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/aceptarsolicitud")
	public String aceptarSolitud(@FormParam("mail") String mail,
			@FormParam("token") String token,
			@FormParam("mailContacto") String mailContacto)
			throws JSONException {

		System.out.println("aceptarsolicitud");
		InitialContext ctx;

		JSONObject respuesta = new JSONObject();

		try {
			ctx = new InitialContext();
			IControladorUsuario icusuario = (IControladorUsuario) ctx
					.lookup("java:global/GeoRedUy-EAR/GeoRedUy-EJB/ControladorUsuario!controladores.IControladorUsuario");

			boolean res = false;
			if ((mail != null) && (token != null) && (mailContacto != null)) {
				if (icusuario.usuarioValido(mail, token)){
					res = icusuario.aceptarSolicitud(mail, token, mailContacto);
					if (icusuario.obtenerTokenCloud(mailContacto, token)!= null){
						Sender sender = new Sender(PuntoEntradaWS.getApikeycloud());
						Message message = new Message.Builder()
								.addData("titulo", "Solicitud de amistad aceptada")
								.addData("descripcion",
										icusuario.obtenerNombre(mail, token)+ " acepto tu solicitud de amistad!")
								.addData("tipo", "SolicitudDeAmistadAceptada")
								.addData("id", mail)
								.build();
		
						try {
							sender.send(message, icusuario.obtenerTokenCloud(mailContacto, token), 1);
						} catch (IOException e) {
		
							e.printStackTrace();
						}
					}
				}
			}
			respuesta.put("resultado", res);

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(respuesta);

		return respuesta.toString();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/rechazarsolicitud")
	public String rechazarSolitud(@FormParam("rechazante") String rechazante,
			@FormParam("token") String token,
			@FormParam("emisor") String emisor)
			throws JSONException {

		System.out.println("rechazarsolicitud");
		InitialContext ctx;

		JSONObject respuesta = new JSONObject();

		try {
			ctx = new InitialContext();
			IControladorUsuario icusuario = (IControladorUsuario) ctx
					.lookup("java:global/GeoRedUy-EAR/GeoRedUy-EJB/ControladorUsuario!controladores.IControladorUsuario");

			boolean res = false;
			if ((rechazante != null) && (token != null) && (emisor != null)) {
				if (icusuario.usuarioValido(rechazante, token))
					res = icusuario.rechazarSolicitud(rechazante, emisor);
			}
			respuesta.put("resultado", res);

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(respuesta);

		return respuesta.toString();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/listarusuarios")
	public String listarUsuarios(@FormParam("mail") String mail,
			@FormParam("token") String token, @FormParam("cadena") String cadena) throws JSONException {

		InitialContext ctx;

		JSONObject respuesta = new JSONObject();

		try {
			ctx = new InitialContext();
			IControladorUsuario icusuario = (IControladorUsuario) ctx
					.lookup("java:global/GeoRedUy-EAR/GeoRedUy-EJB/ControladorUsuario!controladores.IControladorUsuario");

			JSONArray jusuarios = null;
			if ((mail != null) && (token != null) && (cadena != null))
				if (icusuario.usuarioValido(mail, token))
					jusuarios = icusuario.listarUsuarios(mail, cadena);

			respuesta.put("lista", jusuarios);

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(respuesta);

		return respuesta.toString();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/listarcontactos")
	public String listarContactos(@FormParam("mail") String mail,
			@FormParam("token") String token) throws JSONException {

		InitialContext ctx;

		JSONObject respuesta = new JSONObject();

		try {
			ctx = new InitialContext();
			IControladorUsuario icusuario = (IControladorUsuario) ctx
					.lookup("java:global/GeoRedUy-EAR/GeoRedUy-EJB/ControladorUsuario!controladores.IControladorUsuario");

			JSONArray jusuarios = null;
			if ((mail != null) && (token != null))
				if (icusuario.usuarioValido(mail, token))
					jusuarios = icusuario.listarContactos(mail, token);

			respuesta.put("lista", jusuarios);

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(respuesta);

		return respuesta.toString();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/listarContactosConectados")
	public String listarContactosConectados(@FormParam("mail") String mail,
			@FormParam("token") String token) throws JSONException {

		InitialContext ctx;

		JSONObject respuesta = new JSONObject();

		try {
			ctx = new InitialContext();
			IControladorUsuario icusuario = (IControladorUsuario) ctx
					.lookup("java:global/GeoRedUy-EAR/GeoRedUy-EJB/ControladorUsuario!controladores.IControladorUsuario");

			JSONArray jusuarios = null;
			if ((mail != null) && (token != null))
				if (icusuario.usuarioValido(mail, token))
					jusuarios = icusuario.listarContactosConectados(mail);

			respuesta.put("lista", jusuarios);

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(respuesta);

		return respuesta.toString();
	}
}
