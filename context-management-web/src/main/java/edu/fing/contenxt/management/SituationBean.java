package edu.fing.contenxt.management;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import edu.fing.commons.front.dto.SituationTO;

@ManagedBean
@ViewScoped
public class SituationBean {

	private String description;
	private String name;
	private long duration;

	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean session;

	public String crearSituacion() {
		System.out.println("Crear situación");

		SituationTO sit = new SituationTO();
		sit.setName(this.name);
		sit.setDescription(this.description);
		sit.setMinuteDuration(this.duration);

		Boolean result = (Boolean) RemoteInvokerUtils.invoke(RemoteInvokerUtils.ContextReasonerConfigService, "createSituation", sit, "localhost", "8080");
		if (result) {
			String mensaje = "Servicio creado con éxito";
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(null, mensaje));
			// servicio de context reasoner
			// (url servicio, situación, lista de adaptaciones con su data)

		}
		return "inicio";
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

	public String getName() {
		return this.name;
	}

	public SessionBean getSession() {
		return this.session;
	}

	public void setDescripcion(String descripcion) {
		this.setDescription(descripcion);
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSession(SessionBean session) {
		this.session = session;
	}

}
