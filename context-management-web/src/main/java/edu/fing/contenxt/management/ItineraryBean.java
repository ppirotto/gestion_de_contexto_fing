package edu.fing.contenxt.management;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.UploadedFile;

import edu.fing.commons.constant.AdaptationType;
import edu.fing.commons.dto.AdaptationTO;
import edu.fing.commons.front.dto.ItineraryTO;
import edu.fing.commons.front.dto.ServiceTO;
import edu.fing.commons.front.dto.SituationTO;
import edu.fing.contenxt.management.RemoteInvokerUtils.ServiceIp;

@ManagedBean
@ViewScoped
public class ItineraryBean {

	private AdaptationType adaptSelec;

	private String description;
	private int priority;

	private List<ServiceTO> serviceList;
	private AdaptationType[] adaptationTypeList = AdaptationType.values();
	private List<SituationTO> situationList;
	private Long selectedService;
	private String selectedSituation;
	private List<UploadedFile> files = new LinkedList<UploadedFile>();

	private List<AdaptationDto> adaptations = new LinkedList<AdaptationDto>();

	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean session;

	public void agregarAdaptacion() {

		AdaptationDto a = new AdaptationDto(this.adaptations.size() + 1, this.adaptSelec, this.description);

		this.adaptations.add(a);

		this.description = null;
	}

	public String crearItinerario() {
		System.out.println("Crear itinerario");

		ItineraryTO itinerary = new ItineraryTO();

		itinerary.setPriority(this.priority);
		List<AdaptationTO> list = new ArrayList<AdaptationTO>();
		for (AdaptationDto adapt : this.adaptations) {
			AdaptationTO a = new AdaptationTO();
			a.setAdaptationType(adapt.getAdaptationType());
			a.setOrder(adapt.getId());
			a.setName("");
			a.setDescription(adapt.getDescription());

			Object data = null;
			if (adapt.getAdaptationType().getDataType() != null) {
				switch (adapt.getAdaptationType().getDataType()) {
				case FILE:
					data = adapt.getData().getContents();
					break;
				case INT:
					data = new Integer(adapt.getExtraData());
					break;
				case STRING:
					data = adapt.getExtraData();
					break;
				default:
					break;

				}
			}
			a.setData(data);

			list.add(a);
		}

		itinerary.setAdaptations(list);
		itinerary.setSituationName(this.getSelectedSituation());
		ServiceTO serviceTO = new ServiceTO();
		serviceTO.setId(this.selectedService);
		itinerary.setService(serviceTO);
		Boolean result = (Boolean) RemoteInvokerUtils.invoke(RemoteInvokerUtils.ContextReasonerConfigService, "createItinerary", itinerary, ServiceIp.ContextReasonerIp);
		if (result) {

			String mensaje = "Itinerario creado con éxito";
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(mensaje, null));
		}
		// servicio de context reasoner
		// (url servicio, situación, lista de adaptaciones con su data)
		return "inicio";
	}

	public List<AdaptationDto> getAdaptations() {
		return this.adaptations;
	}

	public AdaptationType[] getAdaptationTypeList() {
		return this.adaptationTypeList;
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

	public Long getSelectedService() {
		return this.selectedService;
	}

	public String getSelectedSituation() {
		return this.selectedSituation;
	}

	public List<ServiceTO> getServiceList() {
		List<ServiceTO> results = (List<ServiceTO>) RemoteInvokerUtils.invoke(RemoteInvokerUtils.ContextReasonerConfigService, "getServices", null, ServiceIp.ContextReasonerIp);
		this.serviceList = results;
		return this.serviceList;
	}

	public SessionBean getSession() {
		return this.session;
	}

	public List<SituationTO> getSituationList() {
		List<SituationTO> results = (List<SituationTO>) RemoteInvokerUtils.invoke(RemoteInvokerUtils.ContextReasonerConfigService, "getSituations", null, ServiceIp.ContextReasonerIp);
		this.situationList = results;
		return this.situationList;
	}

	public void setAdaptations(List<AdaptationDto> adaptations) {
		this.adaptations = adaptations;
	}

	public void setAdaptationTypeList(AdaptationType[] adaptationTypeList) {
		this.adaptationTypeList = adaptationTypeList;
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

	public void setSelectedService(Long selectedService) {
		this.selectedService = selectedService;
	}

	public void setSelectedSituation(String selectedSituation) {
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

	public void upload() {
		System.out.println(this.getFiles().size());
		FacesMessage message = null;
		if (!this.adaptations.isEmpty()) {
			message = new FacesMessage("Succesful", " is uploaded.");

		} else {
			message = new FacesMessage("Error", " is uploaded.");
		}

	}
}
