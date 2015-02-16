package edu.fing.contenxt.management;

import java.util.LinkedList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.UploadedFile;

import edu.fing.commons.front.dto.ServiceTO;
import edu.fing.commons.front.dto.SituationTO;

@ManagedBean
@ViewScoped
public class ItineraryBean {

	public enum AdaptationType {
		FILTER, ENRICH, DELAY, SERVICE_INVOCATION
	}

	private AdaptationType adaptSelec;

	private String description;
	private int priority;

	private List<ServiceTO> serviceList;

	private List<SituationTO> situationList;
	private ServiceTO selectedService;
	private SituationTO selectedSituation;
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

	public int getPriority() {
		return this.priority;
	}

	public ServiceTO getSelectedService() {
		return this.selectedService;
	}

	public SituationTO getSelectedSituation() {
		return this.selectedSituation;
	}

	public List<ServiceTO> getServiceList() {
		List<ServiceTO> results = (List<ServiceTO>) RemoteInvokerUtils.invoke(RemoteInvokerUtils.ContextReasonerConfigService, "getServices", null, "192.168.0.101", "8080");
		this.serviceList = results;
		return this.serviceList;
	}

	public SessionBean getSession() {
		return this.session;
	}

	public List<SituationTO> getSituationList() {
		List<SituationTO> results = (List<SituationTO>) RemoteInvokerUtils.invoke(RemoteInvokerUtils.ContextReasonerConfigService, "getSituations", null, "192.168.0.101", "8080");
		this.situationList = results;
		return this.situationList;
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

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public void setSelectedService(ServiceTO selectedService) {
		this.selectedService = selectedService;
	}

	public void setSelectedSituation(SituationTO selectedSituation) {
		this.selectedSituation = selectedSituation;
	}

	public void setServiceList(List<ServiceTO> serviceList) {
		this.serviceList = serviceList;
	}

	public void setSession(SessionBean session) {
		this.session = session;
	}

	public void setSituationList(List<SituationTO> situationList) {
		this.situationList = situationList;
	}

}
