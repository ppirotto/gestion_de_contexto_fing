package edu.fing.contenxt.management;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@ViewScoped
public class SituationBean {

	private String description;
	private String name;
	private int duration;

	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean session;

	public String crearSituacion() {
		System.out.println("Crear situación");
		String mensaje = "Servicio creado con éxito";
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(null, mensaje));

		// servicio de context reasoner
		// (url servicio, situación, lista de adaptaciones con su data)
		return "inicio";
	}

	public String getDescripcion() {
		return this.getDescription();
	}

	public int getDuration() {
		return this.duration;
	}

	public String getName() {
		return this.name;
	}

	public void setDescripcion(String descripcion) {
		this.setDescription(descripcion);
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public SessionBean getSession() {
		return session;
	}

	public void setSession(SessionBean session) {
		this.session = session;
	}

}
