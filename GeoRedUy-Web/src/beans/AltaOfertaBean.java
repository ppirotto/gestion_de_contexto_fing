package beans;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;


import otros.Categoria;
import pojos.LocalEmp;




import controladores.IControladorEmpresa;
import controladores.IControladorOferta;
import controladores.IControladorSitioInteres;

@ManagedBean
@ViewScoped
public class AltaOfertaBean {

	
	private String descripcion;
	private byte[] file;
	private String fileName;
	private String nombre;
	private Date fechaInicio;
	private Date fechaFin;
	private Double costo;
	private int localSelec;
	
	//private List<LocalEmp> listaLocales;
	
	
	@ManagedProperty(value="#{sesionBean}")
	private SesionBean sesion;
	
	private Collection<String> categoriasSelec;

	private Map<String, String> categorias;
	
	
	@EJB
	private IControladorSitioInteres icSitio;
	@EJB
	private IControladorEmpresa icEmpresa;
	@EJB
	private IControladorOferta icOferta;
	
	
	
	


	public Collection<String> getCategoriasSelec() {
		return categoriasSelec;
	}

	public void setCategoriasSelec(Collection<String> categoriasSelec) {
		this.categoriasSelec = categoriasSelec;
	}

	public int getLocalSelec() {
		return localSelec;
	}

	public void setLocalSelec(int localSelec) {
		this.localSelec = localSelec;
	}

	public Double getCosto() {
		return costo;
	}

	public void setCosto(Double costo) {
		this.costo = costo;
	}

	

	public void setCategoriasSelec(List<String> categoriasSelec) {
		this.categoriasSelec = categoriasSelec;
	}

	public Map<String, String> getCategorias() {
		return categorias;
	}

	public void setCategorias(Map<String, String> categorias) {
		this.categorias = categorias;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
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

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public List<LocalEmp> listarLocales(){
		
		return icEmpresa.listarLocalesDeEmpresa(sesion.getNombreEmpresa());
		
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
	public void setSesion(SesionBean sesion) {
		this.sesion = sesion;
	}

	public void upload(FileUploadEvent event) {
		FacesMessage msg = new FacesMessage("Ok! ", event.getFile()
				.getFileName() + " fue subido con éxito.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	

		System.out.println("adjunteeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeEEEEEEEEE");
		file=event.getFile().getContents();
		fileName=event.getFile().getFileName();
		
	}
	
	public String crearOferta(){
		
		
		icOferta.crearOferta(sesion.getNombreEmpresa(), nombre, descripcion,
				fechaInicio, fechaFin, file,fileName, costo, categoriasSelec, localSelec);
		
		
		FacesMessage msg = new FacesMessage("Oferta creada con éxito.", "");
		
		FacesContext.getCurrentInstance().addMessage(null, msg);
		return "homeAdminEmpresa";
		
	}
}
