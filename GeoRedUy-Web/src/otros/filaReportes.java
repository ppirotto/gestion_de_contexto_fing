package otros;

public class filaReportes {

	
	private double latitud;
	private double longitud;
	
	private int cantidadC;
	private String nombreSitio;

	
	public filaReportes() {
		
	}



	

	public filaReportes(double latitud, double longitud, int cantidadC,
			String nombreSitio) {
		
		this.latitud = latitud/1000000;
		this.longitud = longitud/1000000;
		this.cantidadC = cantidadC;
		this.nombreSitio = nombreSitio;
	}





	public String getNombreSitio() {
		return nombreSitio;
	}

	public void setNombreSitio(String nombreSitio) {
		this.nombreSitio = nombreSitio;
	}

	public double getLatitud() {
		return latitud;
	}

	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}

	public double getLongitud() {
		return longitud;
	}

	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}

	public int getCantidadC() {
		return cantidadC;
	}

	public void setCantidadC(int cantidadC) {
		this.cantidadC = cantidadC;
	}
	
	
}
