package edu.fing.contenxt.management;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import edu.fing.commons.front.dto.ContextSourceTO;
import edu.fing.context.management.jar.creation.JarCreationService;

@ManagedBean
@ViewScoped
public class ContextSourceBean {

	private String modeConverter;
	private String eventName;
	private String receiveMode;
	private String url;
	private String cron;

	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean session;

	public String crearFuenteContexto() {
		System.out.println("Fuente creada");
		String mensaje = "Fuente de contexto creada con �xito";
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(null, mensaje));

		ContextSourceTO cS = new ContextSourceTO();
		cS.setEventName(this.eventName);
		cS.setModeConverter(this.modeConverter);
		cS.setReceiveMode(this.receiveMode);
		cS.setUrl(this.url);
		cS.setCron(this.cron);
		try {
			JarCreationService.createContextSource(cS);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// servicio de context reasoner
		// (url servicio, situaci�n, lista de adaptaciones con su data)
		return "inicio";
	}

	public String getCron() {
		return this.cron;
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

	public void setCron(String cron) {
		this.cron = cron;
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
