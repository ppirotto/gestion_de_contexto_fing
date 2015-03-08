package edu.fing.contenxt.management;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import edu.fing.commons.front.dto.ContextSourceTO;
import edu.fing.commons.front.dto.SituationTO;
import edu.fing.contenxt.management.RemoteInvokerUtils.ServiceIp;

@ManagedBean
@ViewScoped
public class SituationBean {

	private String description;
	private String name;
	private long duration;
	private String selectedContextSource;

	private List<String> contextSources;

	private List<String> contextDataList;
	private List<String> selectedInputDatum;
	private List<String> selectedOutputDatum;

	private List<ContextSourceTO> mappedContextData = new ArrayList<ContextSourceTO>();

	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean session;

	public String crearSituacion() {
		System.out.println("Crear situación");

		SituationTO sit = new SituationTO();
		sit.setName(this.name);
		sit.setDescription(this.getDescription());
		sit.setMinuteDuration(this.duration);

		Boolean result = (Boolean) RemoteInvokerUtils.invoke(RemoteInvokerUtils.ContextReasonerConfigService, "createSituation", sit, ServiceIp.ContextReasonerIp);
		if (result) {
			String mensaje = "Servicio creado con éxito";
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(null, mensaje));
			// servicio de context reasoner
			// (url servicio, situación, lista de adaptaciones con su data)

		}
		return "inicio";
	}

	public void createDatumForInput() {
		ContextSourceTO csTo = new ContextSourceTO();
		csTo.setContextData(this.selectedInputDatum);
		this.getMappedContextData().add(csTo);
	}

	public List<String> getContextSources() {

		// List<String> list = (List<String>)
		// RemoteInvokerUtils.invoke(RemoteInvokerUtils.ContextReasonerConfigService,
		// "getDatum", null, ServiceIp.ContextReasonerIp);
		List<String> list = null;
		if (list != null) {
			this.contextSources = list;

		} else {
			this.contextSources = mocker();

		}
		return this.contextSources;
	}

	public String getDescripcion() {
		return this.getDescription();
	}

	public String getDescription() {
		return this.description;
	}

	public long getDuration() {
		return this.duration;
	}

	public List<ContextSourceTO> getMappedContextData() {
		return this.mappedContextData;
	}

	public String getName() {
		return this.name;
	}

	public String getSelectedContextSource() {
		return this.selectedContextSource;
	}

	public List<String> getSelectedInputDatum() {
		return this.selectedInputDatum;
	}

	public List<String> getSelectedOutputDatum() {
		return this.selectedOutputDatum;
	}

	public SessionBean getSession() {
		return this.session;
	}

	private List<String> mocker() {
		List<String> list = new ArrayList<String>();
		list.add("city");
		list.add("latitud");
		list.add("longitud");
		list.add("user");
		return list;
	}

	public void setContextSources(List<String> contextSources) {
		this.contextSources = contextSources;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public void setMappedContextData(List<ContextSourceTO> mappedContextData) {
		this.mappedContextData = mappedContextData;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSelectedContextSource(String selectedContextSource) {
		this.selectedContextSource = selectedContextSource;
	}

	public void setSelectedInputDatum(List<String> selectedInputDatum) {
		this.selectedInputDatum = selectedInputDatum;
	}

	public void setSelectedOutputDatum(List<String> selectedOutputDatum) {
		this.selectedOutputDatum = selectedOutputDatum;
	}

	public void setSession(SessionBean session) {
		this.session = session;
	}
}
