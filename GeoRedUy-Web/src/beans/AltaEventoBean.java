package beans;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@ManagedBean
@ViewScoped
public class AltaEventoBean {

	private String descripcion;
	private String direccion = "";;
	private byte[] file;
	private String fileName;
	private String nombre;
	private Date fechaInicio;
	private Date fechaFin;
	private Double costo;
	private String SitioSelec;
	private String nombreSitioSelec = "";

	private String nombreBusqueda = "";
	private int latitud = 0;
	private int longitud = 0;

	private List<SitioInteres> listaSitios;

	@ManagedProperty(value = "#{sesionBean}")
	private SesionBean sesion;

	private Collection<String> categoriasSelec;

	private Map<String, String> categorias;

	@EJB
	private IControladorSitioInteres icSitio;
	@EJB
	private IControladorEmpresa icEmpresa;

	public String crearEvento() {
		System.out.println("Crear el evento");
		this.icSitio.crearEvento(this.nombre, this.direccion, this.descripcion, this.fechaInicio, this.fechaFin, this.latitud, this.longitud, this.file, this.fileName, this.categoriasSelec,
				this.nombreSitioSelec);

		String mensaje = "Evento creado con éxito";
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(mensaje, null));
		return "homeAdminApp";

	}

	public Map<String, String> getCategorias() {
		return this.categorias;
	}

	public Collection<String> getCategoriasSelec() {
		return this.categoriasSelec;
	}

	public Double getCosto() {
		return this.costo;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public Date getFechaFin() {
		return this.fechaFin;
	}

	public Date getFechaInicio() {
		return this.fechaInicio;
	}

	public byte[] getFile() {
		return this.file;
	}

	public String getFileName() {
		return this.fileName;
	}

	public int getLatitud() {
		return this.latitud;
	}

	public List<SitioInteres> getListaSitios() {
		return this.listaSitios;
	}

	public int getLongitud() {
		return this.longitud;
	}

	public String getNombre() {
		return this.nombre;
	}

	public String getNombreBusqueda() {
		return this.nombreBusqueda;
	}

	public String getNombreSitioSelec() {
		return this.nombreSitioSelec;
	}

	public String getSitioSelec() {
		return this.SitioSelec;
	}

	public Map<String, String> listarCategorias() {
		Collection<Categoria> lista = this.icSitio.listarCategorias();
		Map<String, String> mapCategorias = new HashMap<String, String>();

		Iterator<Categoria> it = lista.iterator();
		while (it.hasNext()) {

			Categoria cat = (Categoria) it.next();
			mapCategorias.put(cat.getCategoria(), cat.getCategoria());
		}
		return mapCategorias;

	}

	public void listarSitios() {
		System.out.println("listame sitiossssssssss");
		System.out.println("Busca" + this.nombreBusqueda);
		this.listaSitios = this.icSitio.listarSitiosInteres(this.nombreBusqueda);
		System.out.println(this.listaSitios);
	}

	public void setCategorias(Map<String, String> categorias) {
		this.categorias = categorias;
	}

	public void setCategoriasSelec(Collection<String> categoriasSelec) {
		this.categoriasSelec = categoriasSelec;
	}

	public void setCategoriasSelec(List<String> categoriasSelec) {
		this.categoriasSelec = categoriasSelec;
	}

	public void setCosto(Double costo) {
		this.costo = costo;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setLatitud(int latitud) {
		this.latitud = latitud;
	}

	public void setListaSitios(List<SitioInteres> listaSitios) {
		this.listaSitios = listaSitios;
	}

	public void setLongitud(int longitud) {
		this.longitud = longitud;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setNombreBusqueda(String nombreBusqueda) {
		this.nombreBusqueda = nombreBusqueda;
	}

	public void setNombreSitioSelec(String nombreSitioSelec) {
		this.nombreSitioSelec = nombreSitioSelec;
	}

	public void setSesion(SesionBean sesion) {
		this.sesion = sesion;
	}

	public void setSitioSelec(String sitioSelec) {
		this.SitioSelec = sitioSelec;
	}

	public void upload(FileUploadEvent event) {
		FacesMessage msg = new FacesMessage("Ok! ", event.getFile().getFileName() + " fue subido con éxito.");
		FacesContext.getCurrentInstance().addMessage(null, msg);

		System.out.println("adjunteeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeEEEEEEEEE");
		this.file = event.getFile().getContents();
		this.fileName = event.getFile().getFileName();

	}
}
