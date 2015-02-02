package beans;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import pojos.Empresa;


import controladores.IControladorEmpresa;

@ManagedBean
@ViewScoped
public class AltaLocalEmpresaBean {

	
	private String descripcion;
	private StreamedContent   imagen;
	
	
	
	
	@ManagedProperty(value="#{sesionBean}")
	private SesionBean sesion;
	
	@EJB
	private IControladorEmpresa icEmpresa;

	
	private String nombre;
	private String direccion;
	

	private float latitud;
	private float longitud;
	



	public StreamedContent getImagen() {
		return imagen;
	}

	public float getLatitud() {
		return latitud;
	}

	public void setLatitud(float latitud) {
		this.latitud = latitud;
	}

	public float getLongitud() {
		return longitud;
	}

	public void setLongitud(float longitud) {
		this.longitud = longitud;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	

	public void setLatitud(int latitud) {
		this.latitud = latitud;
	}

	

	public void setLongitud(int longitud) {
		this.longitud = longitud;
	}

	public void setSesion(SesionBean sesion) {
		this.sesion = sesion;
	}


	
	public String altaLocalEmpresa(){
		
		icEmpresa.insertarLocal(sesion.getNombreEmpresa(), nombre, Math.round(latitud * 1000000), Math.round(longitud * 1000000), direccion);

		
	FacesMessage msg = new FacesMessage("Local creado con éxito.", "");
		
		FacesContext.getCurrentInstance().addMessage(null, msg);
		return "homeAdminEmpresa";
	}
	
}
