package edu.fing.contenxt.management;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@ViewScoped
public class SituationBean {

	private String descripcion;
	private String name;

	@ManagedProperty(value = "#{sesionBean}")
	private SesionBean sesion;

	public String crearSituacion() {
		System.out.println("Crear situación");
		String mensaje = "Servicio creado con éxito";
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(null, mensaje));

		// servicio de context reasoner
		// (url servicio, situación, lista de adaptaciones con su data)
		return "inicio";
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public SesionBean getSesion() {
		return this.sesion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setSesion(SesionBean sesion) {
		this.sesion = sesion;
	}

}
