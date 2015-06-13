package edu.fing.context.management.bean;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import edu.fing.commons.front.dto.ContextSourceTO;
import edu.fing.context.management.jar.creation.JarCreationService;
import edu.fing.context.management.util.RemoteInvokerUtils;
import edu.fing.context.management.util.RemoteInvokerUtils.ServiceIp;

@ManagedBean
@ViewScoped
public class ContextSourceBean {

	private String modeConverter;
	private String eventName;
	private String receiveMode;
	private String url;
	private String cron;
	private String ip;

	public String crearFuenteContexto() {
		System.out.println("Fuente creada");

		ContextSourceTO cS = new ContextSourceTO();
		cS.setEventName(this.eventName);
		cS.setModeConverter(this.modeConverter);
		cS.setReceiveMode(this.receiveMode);
		cS.setUrl(this.url);
		cS.setCron(this.cron);
		try {
			JarCreationService.createContextSource(cS);
		} catch (IOException e) {
			e.printStackTrace();
		}
		boolean res = (Boolean) RemoteInvokerUtils.invoke(RemoteInvokerUtils.ContextReasonerConfigService,
				"createContextSource", cS, ServiceIp.ContextReasonerIp);

		if (res) {
			String mensaje = "Fuente de contexto creada con Éxito";
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", mensaje));
		} else {
			String mensaje = "Error al crear fuente de contexto";
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", mensaje));
		}

		return "inicio";
	}

	public String getCron() {
		return this.cron;
	}

	public String getEventName() {
		return this.eventName;
	}

	public String getIp() {

		if (this.ip == null) {
			InetAddress IP = null;
			try {
				IP = InetAddress.getLocalHost();
			} catch (UnknownHostException e) {

				e.printStackTrace();
			}

			this.ip = IP.getHostAddress();
		}
		return this.ip;

	}

	public String getModeConverter() {
		return this.modeConverter;
	}

	public String getReceiveMode() {
		return this.receiveMode;
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

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setModeConverter(String modeConverter) {
		this.modeConverter = modeConverter;
	}

	public void setReceiveMode(String receiveMode) {
		this.receiveMode = receiveMode;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
