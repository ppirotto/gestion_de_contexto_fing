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
import com.google.android.gcm.server.Sender;

import controladores.IControladorNotificacion;
import controladores.IControladorUsuario;

@Path("/rest/notificaciones")
public class INotificacion {

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/listarmisnotificaciones")
	public String verNotificaciones(@FormParam("mail") String mail,
			@FormParam("token") String token) throws JSONException {

		InitialContext ctx;

		JSONObject respuesta = new JSONObject();

		try {
			ctx = new InitialContext();

			IControladorNotificacion icnotificacion = (IControladorNotificacion) ctx
					.lookup("java:global/GeoRedUy-EAR/GeoRedUy-EJB/ControladorNotificacion!controladores.IControladorNotificacion");

			IControladorUsuario icusuario = (IControladorUsuario) ctx
					.lookup("java:global/GeoRedUy-EAR/GeoRedUy-EJB/ControladorUsuario!controladores.IControladorUsuario");

			JSONArray jnotificaciones = null;
			JSONArray jsolicitudes = null;

			if ((mail != null) && (token != null)) {

				if (icusuario.usuarioValido(mail, token)) {
					jsolicitudes = icusuario.listarMisSolicitudes(mail);
					jnotificaciones = icnotificacion.verNotificaciones(mail);

				}
			}
			respuesta.put("notificaciones", jnotificaciones);
			respuesta.put("solicitudes", jsolicitudes);

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(respuesta);

		return respuesta.toString();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/estoyaca")
	public String nuevasNotificaciones(@FormParam("mail") String mail,
			@FormParam("token") String token,
			@FormParam("latitud") int latitud,
			@FormParam("longitud") int longitud) throws JSONException {

		System.out.println("estoy aca");
		
		InitialContext ctx;

		JSONObject respuesta = new JSONObject();

		try {
			ctx = new InitialContext();

			IControladorUsuario icusuario = (IControladorUsuario) ctx
					.lookup("java:global/GeoRedUy-EAR/GeoRedUy-EJB/ControladorUsuario!controladores.IControladorUsuario");

			IControladorNotificacion icnotificacion = (IControladorNotificacion) ctx
					.lookup("java:global/GeoRedUy-EAR/GeoRedUy-EJB/ControladorNotificacion!controladores.IControladorNotificacion");

			if ((mail != null) && (token != null)) {

				if (icusuario.usuarioValido(mail, token)) {
					if ((icusuario.obtenerNotificacionesDeseadas(mail).contains("Sitios de Interes"))
							&& (icnotificacion.hayNotificacionesSitiosInteres(mail, latitud, longitud))) {

						System.out.println("Hay nuevos sitios que te pueden interesar!");
						if (icusuario.obtenerTokenCloud(mail, token)!= null){
							Sender sender = new Sender(PuntoEntradaWS.getApikeycloud());
							Message message = new Message.Builder()
									.addData("titulo", "Nuevos sitios de interes")
									.addData("descripcion","Hay nuevos sitios que te pueden interesar!")
									.addData("tipo", "SitioInteres").build();
	
							try {
								sender.send(message,
										icusuario.obtenerTokenCloud(mail, token), 1);
							} catch (IOException e) {
	
								e.printStackTrace();
							}
						}
					}

					if ((icusuario.obtenerNotificacionesDeseadas(mail).contains("CheckIn"))
							&& (icnotificacion.hayNotificacionesCheckins(mail,latitud, longitud))) {

						System.out.println("Hay nuevos check-ins de tus amigos!");
						if (icusuario.obtenerTokenCloud(mail, token)!= null){
							Sender sender = new Sender(PuntoEntradaWS.getApikeycloud());
							Message message = new Message.Builder()
									.addData("titulo", "Nuevos check-ins")
									.addData("descripcion",	"Hay nuevos check-ins de tus amigos!")
									.addData("tipo", "CheckIn").build();
	
							try {
								sender.send(message,
										icusuario.obtenerTokenCloud(mail, token), 1);
							} catch (IOException e) {
	
								e.printStackTrace();
							}
							
						}
					}

					if ((icusuario.obtenerNotificacionesDeseadas(mail).contains("Ofertas"))
							&& (icnotificacion.hayNotificacionesOfertas(mail,latitud, longitud))) {

						System.out.println("Hay nuevas ofertas que te pueden interesar!");
						if (icusuario.obtenerTokenCloud(mail, token)!= null){
							Sender sender = new Sender(PuntoEntradaWS.getApikeycloud());
							Message message = new Message.Builder()
									.addData("titulo", "Nuevas ofertas")
									.addData("descripcion","Hay nuevas ofertas que te pueden interesar!")
									.addData("tipo", "Oferta").build();
	
							try {
								sender.send(message,icusuario.obtenerTokenCloud(mail, token), 1);
							} catch (IOException e) {
	
								e.printStackTrace();
							}
						}
					}
					
					
					if ((icusuario.obtenerNotificacionesDeseadas(mail).contains("Eventos"))
							&& (icnotificacion.hayNotificacionesEventos(mail,latitud, longitud))) {

						System.out.println("Hay nuevas eventos que te pueden interesar!");
						if (icusuario.obtenerTokenCloud(mail, token)!= null){
							Sender sender = new Sender(PuntoEntradaWS.getApikeycloud());
							Message message = new Message.Builder()
									.addData("titulo", "Nuevos eventos")
									.addData("descripcion","Hay nuevos eventos que te pueden interesar!")
									.addData("tipo", "Evento").build();
	
							try {
								sender.send(message,icusuario.obtenerTokenCloud(mail, token), 1);
							} catch (IOException e) {
	
								e.printStackTrace();
							}
						}
					}

				}
			}

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return respuesta.toString();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/notificacionesvistas")
	public String solicitudesVistas(@FormParam("mail") String mail,
			@FormParam("token") String token) throws JSONException {

		InitialContext ctx;

		JSONObject respuesta = new JSONObject();

		try {
			ctx = new InitialContext();
			IControladorUsuario icusuario = (IControladorUsuario) ctx
					.lookup("java:global/GeoRedUy-EAR/GeoRedUy-EJB/ControladorUsuario!controladores.IControladorUsuario");

			IControladorNotificacion icnotificacion = (IControladorNotificacion) ctx
					.lookup("java:global/GeoRedUy-EAR/GeoRedUy-EJB/ControladorNotificacion!controladores.IControladorNotificacion");

			if ((mail != null) && (token != null)) {
				if (icusuario.usuarioValido(mail, token)){
					icusuario.solicitudesVistas(mail);
					icnotificacion.notificacionesVistas(mail);
				}
			}
			respuesta.put("respuesta", "notificaciones vistas");

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(respuesta);

		return respuesta.toString();
	}
}
