package serviciosWeb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.FormParam;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.Base64;



import controladores.IControladorUsuario;

@Path("/rest/login")
public class ILogin {

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/iniciarsesion")
	public String iniciarSesion(@FormParam("mail") String mail,
			@FormParam("token") String token, @FormParam("tokenCloud") String tokenCloud) {
		
			System.out.println("MAIL DEL ANDROID:"+mail);
			System.out.println("TOKEN DEL ANDROID:"+token);
			
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		// seteo URL
		WebResource service = client
				.resource(UriBuilder.fromUri(
						"https://graph.facebook.com/me/?access_token=" + token)
						.build());
		String respuestaF = null;
		String emailPosta = null;
		try {
			respuestaF = service
					.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE) // tipo
					.accept(MediaType.TEXT_PLAIN) // formato de la respuesta
					.get(String.class);

			JSONObject ob = new JSONObject(respuestaF);

			emailPosta = ob.getString("email");

		} catch (Exception e1) {
			System.out.println("TOKEN NO VALIDO EXCEPCION");

		}

		System.out.println("Respuesta JSON: " + emailPosta);

		InitialContext ctx;
		String mensaje = null;
		JSONObject respuesta = new JSONObject();

		if ((emailPosta != null) && emailPosta.equals(mail)) {

			try {
				ctx = new InitialContext();
				IControladorUsuario icusuario = (IControladorUsuario) ctx
						.lookup("java:global/GeoRedUy-EAR/GeoRedUy-EJB/ControladorUsuario!controladores.IControladorUsuario");

				if (mail != null && token != null) {
					int ret = icusuario.loguearUsuario(mail, token, tokenCloud);

					if (ret == 1) {
						mensaje = "Usuario logueado correctamente";
					} else
						mensaje = "Usuario no registrado";
				} else {
					mensaje = "Parametros no especificados";
				}

			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {

			mensaje = "Token no valido";
		}

		try {
			respuesta.put("mensaje", mensaje);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return respuesta.toString();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/registro")
	public String registrarUsuario(@FormParam("mail") String mail,
			@FormParam("token") String token,
			@FormParam("nombre") String nombre, @FormParam("sexo") String sexo, @FormParam("foto") String foto,
			@FormParam("categorias") String categorias,
			@FormParam("notificaciones") String notificaciones)
			throws JSONException {
	
		InitialContext ctx;
		String mensaje = null;
		JSONObject respuesta = new JSONObject();

		try {
			ctx = new InitialContext();
			IControladorUsuario icusuario = (IControladorUsuario) ctx
					.lookup("java:global/GeoRedUy-EAR/GeoRedUy-EJB/ControladorUsuario!controladores.IControladorUsuario");

			if ((mail != null) && (nombre != null) && (sexo != null)
					&& (categorias != null) && (notificaciones != null) && (foto!=null)) {

				ArrayList<String> listaCategorias = new ArrayList<String>(
						Arrays.asList(categorias.split(",")));
				ArrayList<String> listaNotificaciones = new ArrayList<String>(
						Arrays.asList(notificaciones.split(",")));

				boolean ret;
				byte[] f = null;
				if (foto!=null)
					f = Base64.decode(foto);
				ret = icusuario.registrarUsuario(mail, token, nombre,
						sexo,f, listaCategorias, listaNotificaciones);
				if (ret)
					mensaje = "Usuario registrado correctamente";
				else
					mensaje = "Error al registrarse";

			} else {
				mensaje = "Parametros no especificados";
			}

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		respuesta.put("mensaje", mensaje);

		return respuesta.toString();
	}
	
	
	
	
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/mandarMensaje")
	public String mandarMensaje(
			@FormParam("token") String token, @FormParam("mensaje") String mensaje) {
			System.out.println("MENSAJE DEL ANDROID:"+mensaje);
			System.out.println("TOKEN DEL CLOUD:"+token);
		
	
		
		Sender sender = new Sender("AIzaSyD5vVvbcBHLvxM8UU9LUe1zQ1DziS0q4JM");
		Message message = new Message.Builder().addData("message",mensaje).build();
		
	//FIXME

		
		Result result = null;
			try {
				result = sender.send(message,token, 1);
		
			
			if (result.getMessageId() != null) {
				String canonicalRegId = result.getCanonicalRegistrationId();
				if (canonicalRegId != null) {
				  // same device has more than on registration ID: update database
				}
				} else {
				String error = result.getErrorCodeName();
				if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
				  // application has been removed from device - unregister database
				}
				}
	} catch (IOException e) {
				
				e.printStackTrace();
			}
			
		System.out.println(result.toString());
		
		return result.toString();
	}
	
}
