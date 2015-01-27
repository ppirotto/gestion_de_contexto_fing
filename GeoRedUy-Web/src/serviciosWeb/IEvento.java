package serviciosWeb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

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

import otros.Categoria;

import com.sun.jersey.core.util.Base64;

import controladores.IControladorEmpresa;
import controladores.IControladorNotificacion;
import controladores.IControladorSitioInteres;
import controladores.IControladorUsuario;

@Path("/rest/evento")
public class IEvento {

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/crearEvento")
	public String crearEvento(@FormParam("nombre") String nombre,
			@FormParam("descripcion") String descripcion,
			@FormParam("direccion") String direccion,
			@FormParam("fechaInicio") Long fechaInicio,
			@FormParam("fechaFin") Long fechaFin,
			@FormParam("latitud") int latitud,
			@FormParam("longitud") int longitud,
			@FormParam("categorias") JSONArray categorias,
			@FormParam("imagen") String imagen) throws JSONException {

		InitialContext ctx;

		String respuesta = null;

		try {
			ctx = new InitialContext();

			IControladorSitioInteres icevento = (IControladorSitioInteres) ctx
					.lookup("java:global/GeoRedUy-EAR/GeoRedUy-EJB/ControladorSitioInteres!controladores.IControladorSitioInteres");
			
			if (nombre != null && descripcion != null && direccion != null	&& fechaInicio != null) {

				Collection<String> listaCategorias = new ArrayList<String>();
				if (categorias != null) {

					for (int i = 0; i < categorias.length(); i++) {
						listaCategorias.add(categorias.getString(i));
					}

				}
				byte[] f = null;
				if (imagen != null)
					f = Base64.decode(imagen);
				Date fInicio =  new Date(fechaInicio);
				Date fFin;
				if (fechaFin!=null)
					 fFin = new Date(fechaFin);
				else
					fFin = fInicio;
				icevento.crearEvento(nombre, direccion, descripcion,fInicio,fFin, latitud, longitud, f,
						"", listaCategorias, "");
				respuesta = "Evento creado correctamente.";
			} else
				respuesta = "Parametros insuficientes.";

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(respuesta);

		return respuesta.toString();
	}

}
