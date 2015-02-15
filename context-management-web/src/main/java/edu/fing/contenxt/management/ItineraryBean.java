package edu.fing.contenxt.management;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.UploadedFile;

@ManagedBean
@ViewScoped
public class ItineraryBean {

	public enum AdaptationType {
		FILTER, ENRICH, DELAY
	}

	private AdaptationType adaptSelec;

	private String description;
	private int priority;

	private List<String> serviceList;

	private List<String> situationList;
	private String nombreServicioSelec;
	private String nombreSituacionSelec;
	private List<UploadedFile> files = new LinkedList<UploadedFile>();

	private List<AdaptationDto> adaptations = new LinkedList<AdaptationDto>();

	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean session;

	public void agregarAdaptacion() {

		AdaptationDto a = new AdaptationDto(this.adaptations.size() + 1, this.getAdaptSelec());
		this.adaptations.add(a);

	}

	public String crearItinerario() {
		System.out.println("Crear itinerario");
		String mensaje = "Itinerario creado con éxito";
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(mensaje, null));

		// servicio de context reasoner
		// (url servicio, situación, lista de adaptaciones con su data)
		return mensaje;
	}

	public List<AdaptationDto> getAdaptations() {
		return this.adaptations;
	}

	public AdaptationType getAdaptSelec() {
		return this.adaptSelec;
	}

	public String getDescription() {
		return this.description;
	}

	public List<UploadedFile> getFiles() {
		return this.files;
	}

	public String getNombreServicioSelec() {
		return this.nombreServicioSelec;
	}

	public String getNombreSituacionSelec() {
		return this.nombreSituacionSelec;
	}

	public int getPriority() {
		return this.priority;
	}

	public List<String> getServiceList() {
		return this.serviceList;
	}

	public SessionBean getSession() {
		return this.session;
	}

	public List<String> getSituationList() {
		return this.situationList;
	}

	@PostConstruct
	public void mocker() {

		this.serviceList = new ArrayList<String>();
		this.serviceList.add("url-servicio1");
		this.serviceList.add("url-servicio2");
		this.serviceList.add("url-servicio3");

		this.situationList = new ArrayList<String>();
		this.situationList.add("InCityRaining");
		this.situationList.add("InCity");
		this.situationList.add("WithCar");

	}

	public void setAdaptations(List<AdaptationDto> adaptations) {
		this.adaptations = adaptations;
	}

	public void setAdaptSelec(AdaptationType adaptSelec) {
		this.adaptSelec = adaptSelec;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setFiles(List<UploadedFile> files) {
		this.files = files;
	}

	public void setNombreServicioSelec(String nombreServicioSelec) {
		this.nombreServicioSelec = nombreServicioSelec;
	}

	public void setNombreSituacionSelec(String nombreSituacionSelec) {
		this.nombreSituacionSelec = nombreSituacionSelec;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public void setServiceList(List<String> serviceList) {
		this.serviceList = serviceList;
	}

	public void setSession(SessionBean session) {
		this.session = session;
	}

	public void setSituationList(List<String> situationList) {
		this.situationList = situationList;
	}

	public void upload() {
		System.out.println(this.getFiles().size());
		FacesMessage message = null;
		if (!this.adaptations.isEmpty()) {
			message = new FacesMessage("Succesful", " is uploaded.");

		} else {
			message = new FacesMessage("Error", " is uploaded.");
		}
		FacesContext.getCurrentInstance().addMessage("growl2", message);
	}

}
