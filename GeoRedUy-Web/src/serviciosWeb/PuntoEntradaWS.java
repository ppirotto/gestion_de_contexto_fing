package serviciosWeb;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("")
public class PuntoEntradaWS extends Application {

	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> empty = new HashSet<Class<?>>();
	
	private static final String apiKeyCloud = "AIzaSyD5vVvbcBHLvxM8UU9LUe1zQ1DziS0q4JM";
	
	public PuntoEntradaWS() {
		singletons.add(new ILogin());
		singletons.add(new ISitioInteres());
		singletons.add(new IUsuario());
		singletons.add(new INotificacion());
		singletons.add(new IOferta());
		singletons.add(new IEvento());
	}

	
	public static String getApikeycloud() {
		return apiKeyCloud;
	}


	@Override
	public Set<Class<?>> getClasses() {
		return empty;
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
}
