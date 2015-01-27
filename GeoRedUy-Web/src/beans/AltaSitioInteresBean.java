package beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import controladores.IControladorSitioInteres;

import otros.Categoria;

public class AltaSitioInteresBean {

	private String descripcion;

	private List<String> categoriasSelec;

	private Map<String, String> categorias;

	private String nombre;

	private byte[] logo;
	private String nombrelogo;
	
	private Map<String,byte[]> files;
	private UploadedFile file;

	
	public byte[] getLogo() {
		return logo;
	}

	public void setLogo(byte[] logo) {
		this.logo = logo;
	}

	public String getNombrelogo() {
		return nombrelogo;
	}

	public void setNombrelogo(String nombrelogo) {
		this.nombrelogo = nombrelogo;
	}

	public Map<String, byte[]> getFiles() {
		return files;
	}

	public void setFiles(Map<String, byte[]> files) {
		this.files = files;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		System.out
				.println("seteo fotooooooooooooooooooooooooooooooooooooooooooooo");
		this.file = file;
	}

	private String direccion;

	private float coordX;
	private float coordY;

	public float getCoordX() {
		return coordX;
	}

	public void setCoordX(float coordX) {
		System.out.println("LA LATITUD ES" + coordX);
		this.coordX = coordX;
	}

	public float getCoordY() {
		return coordY;
	}

	public void setCoordY(float coordY) {
		this.coordY = coordY;
	}

	@EJB
	private IControladorSitioInteres icSitio;

	public AltaSitioInteresBean() {

		files = new HashMap<String,byte[]>();

	}

	

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		System.out.println("direccion: " + direccion);
		this.direccion = direccion;
	}

	public String getNombre() {

		return nombre;
	}

	public void setNombre(String nombre) {
		System.out.println("nombre: " + nombre);
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public List<String> getCategoriasSelec() {
		return categoriasSelec;
	}

	public void setCategoriasSelec(List<String> categoriasSelec) {
		this.categoriasSelec = categoriasSelec;
	}

	public void setCategorias(Map<String, String> categorias) {
		this.categorias = categorias;
	}

	public Map<String, String> getCategorias() {
		return categorias;
	}

	public void upload(FileUploadEvent event) {
		FacesMessage msg = new FacesMessage("Ok! ", event.getFile()
				.getFileName() + " fue subido con éxito.");
		FacesContext.getCurrentInstance().addMessage(null, msg);

		System.out.println("adjunteeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeEEEEEEEEE");
		files.put(event.getFile().getFileName(),event.getFile().getContents());
		
		
	}
	
	public void uploadLogo(FileUploadEvent event) {
		FacesMessage msg = new FacesMessage("Ok! ", event.getFile()
				.getFileName() + " fue subido con éxito.");
		FacesContext.getCurrentInstance().addMessage(null, msg);

		System.out.println("adjunteeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeEEEEEEEEELOGOOOOO");
		logo=event.getFile().getContents();
		nombrelogo=event.getFile().getFileName();
		
		
	}


	public Map<String, String> listarCategorias() {
		Collection<Categoria> lista = icSitio.listarCategorias();
		Map<String, String> mapCategorias = new HashMap<String, String>();

		Iterator<Categoria> it = lista.iterator();
		while (it.hasNext()) {

			Categoria cat = (Categoria) it.next();
			mapCategorias.put(cat.getCategoria(), cat.getCategoria());
		}
		return mapCategorias;

	}

	public String crearSitioInteres() {
		System.out.println("crearSitioInteres");


		boolean creada = icSitio.altaSitioInteres(nombre, direccion,
				descripcion, Math.round(coordX * 1000000),
				Math.round(coordY * 1000000),logo,nombrelogo, files, categoriasSelec);
		FacesMessage msg;
		if (creada)
			msg = new FacesMessage("Sitio de interés creado con éxito.", "");
		else
			msg = new FacesMessage("Error",
					"Ya existe un sitio de interés con ese nombre.");
		FacesContext.getCurrentInstance().addMessage(null, msg);

		return "homeAdminApp";
	}

}
