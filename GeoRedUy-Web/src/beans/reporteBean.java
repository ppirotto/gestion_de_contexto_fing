package beans;

import java.io.ByteArrayInputStream;

import java.io.File;

import java.io.InputStream;

import java.util.ArrayList;

import java.util.Date;

import java.util.List;

import javax.ejb.EJB;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;



import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;



import com.sun.jersey.core.util.Base64;


import otros.ArchivoUtil;

import otros.ReporteXLS;
import otros.filaReportes;





import controladores.IControladorOferta;
import controladores.IControladorSitioInteres;

@ManagedBean
@SessionScoped
public class reporteBean {

	private Date fechaInicio;
	private Date fechaFin;
	
	private List<filaReportes> lista;
	
	@EJB
	
	private IControladorSitioInteres icSitio;
	
	@EJB
	private IControladorOferta icOferta;
	
	
	private StreamedContent archivo;
	
	public StreamedContent getArchivo() {
		return archivo;
	}




	public void setArchivo(StreamedContent archivo) {
		this.archivo = archivo;
	}




	public reporteBean() {
		
		
		
	
	}




	@ManagedProperty(value="#{sesionBean}")
	private SesionBean sesion;
	
	
	

	public List<filaReportes> getLista() {
		return lista;
	}




	public void setLista(List<filaReportes> lista) {
		this.lista = lista;
	}




	public Date getFechaInicio() {
		return fechaInicio;
	}




	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}




	public Date getFechaFin() {
		return fechaFin;
	}




	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}




	public void setSesion(SesionBean sesion) {
		this.sesion = sesion;
	}


	public String cantidadCheckIns(){
		
		
		JSONArray resp= icSitio.cantidadCheckInsPorSitio(fechaInicio, fechaFin);
		this.lista = new ArrayList<filaReportes>();
		for (int i=0; i< resp.length();i++){
			
			
			try {
				lista.add(new filaReportes(resp.getJSONObject(i).getInt("latitud"), resp.getJSONObject(i).getInt("longitud"), resp.getJSONObject(i).getInt("cantidad"), resp.getJSONObject(i).getString("nombre")));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					
		}
		
		fechaInicio=null;
		fechaFin=null;
		
		return "reporteCheckIN";
				
		
	}

	
	
	private String crearReporte(){
		
		File tmp = null;
		String strBytes = null;
		try {
			tmp = ArchivoUtil.archivoTemporal();

		
			
			ReporteXLS r = new ReporteXLS();

			JSONArray ofertas= icOferta.reporteOfertas(fechaInicio, fechaFin);
			
			
			r.procesarArchivoReporteT(tmp, ofertas);
			

			strBytes = ArchivoUtil.base64encode(tmp);

			System.out.println("Archivo: " + tmp.getAbsolutePath());

			
				System.out.println(tmp.delete() ? " borrado"
						: " no se pudo borrar");
			

		} catch (Exception e) {
			System.out.println(tmp != null && tmp.delete() ? "Archivo borrado"
					: "No se pudo borrar el archivo " + tmp.getAbsolutePath());
			
			e.printStackTrace();

			
		}

		return strBytes;

	
		
	}
	
	
	public void  reporteOfertas(){
		
	
		String contenido =crearReporte();
		InputStream stream =  new ByteArrayInputStream(Base64.decode(contenido));	
		String nombre= "reporteOfertas";
		
		
			archivo = new DefaultStreamedContent(stream, "application/xls", nombre+".xls");
			
			fechaInicio=null;
			fechaFin=null;
		
	}
}
