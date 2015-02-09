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

@ManagedBean
@ViewScoped
public class ItineraryBean {

	public enum AdaptationType {
		FILTER, ENRICH, DELAY
	}

	private AdaptationType adaptSelec;

	private String descripcion;

	private List<String> serviceList;

	private List<String> situationList;
	private String nombreServicioSelec;
	private String nombreSituacionSelec;;

	private List<AdaptationDto> adaptations = new LinkedList<AdaptationDto>();

	@ManagedProperty(value = "#{sesionBean}")
	private SesionBean sesion;

	public void agregarAdaptacion() {

		AdaptationDto a = new AdaptationDto(this.adaptations.size(), this.getAdaptSelec());
		this.adaptations.add(a);

	}

	public String crearEvento() {
		System.out.println("Crear el evento");
		String mensaje = "Evento creado con éxito";
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(mensaje, null));
		return "homeAdminApp";

	}

	public List<AdaptationDto> getAdaptations() {
		return this.adaptations;
	}

	public AdaptationType getAdaptSelec() {
		return this.adaptSelec;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public String getNombreServicioSelec() {
		return this.nombreServicioSelec;
	}

	public String getNombreSituacionSelec() {
		return this.nombreSituacionSelec;
	}

	public List<String> getServiceList() {
		return this.serviceList;
	}

	public SesionBean getSesion() {
		return this.sesion;
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

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setNombreServicioSelec(String nombreServicioSelec) {
		this.nombreServicioSelec = nombreServicioSelec;
	}

	public void setNombreSituacionSelec(String nombreSituacionSelec) {
		this.nombreSituacionSelec = nombreSituacionSelec;
	}

	public void setServiceList(List<String> serviceList) {
		this.serviceList = serviceList;
	}

	public void setSesion(SesionBean sesion) {
		this.sesion = sesion;
	}

	public void setSituationList(List<String> situationList) {
		this.situationList = situationList;
	}

}
