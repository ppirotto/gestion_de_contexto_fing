package edu.fing.contenxt.management;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@ViewScoped
public class ContextSourceBean {

	private String modeConverter;
	private String eventName;
	private String receiveMode;
	private String url;

	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean session;

	public String crearFuenteContexto() {
		System.out.println("Fuente creada");
		String mensaje = "Fuente de contexto creada con éxito";
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(null, mensaje));

		ContextSourceDto cS = new ContextSourceDto();
		cS.setEventName(this.eventName);
		cS.setModeConverter(this.modeConverter);
		cS.setReceiveMode(this.receiveMode);
		cS.setUrl(this.url);
		// servicio de context reasoner
		// (url servicio, situación, lista de adaptaciones con su data)
		return "inicio";
	}

	public String getEventName() {
		return this.eventName;
	}

	public String getModeConverter() {
		return this.modeConverter;
	}

	public String getReceiveMode() {
		return this.receiveMode;
	}

	public SessionBean getSession() {
		return this.session;
	}

	public String getUrl() {
		return this.url;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public void setModeConverter(String modeConverter) {
		this.modeConverter = modeConverter;
	}

	public void setReceiveMode(String receiveMode) {
		this.receiveMode = receiveMode;
	}

	public void setSession(SessionBean session) {
		this.session = session;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
