package edu.fing.contenxt.management;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import edu.fing.contenxt.management.RemoteInvokerUtils.ServiceIp;

@ManagedBean
@ViewScoped
public class ContextDatumBean {

	private String atributeName;
	private List<String> contextualDatumList;
	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean session;

	public String crearDatum() {
		System.out.println("Crear Datum");

		Boolean result = (Boolean) RemoteInvokerUtils.invoke(RemoteInvokerUtils.ContextReasonerConfigService, "createContextDatum", this.atributeName, ServiceIp.ContextReasonerIp);
		if (result) {
			String mensaje = "Servicio creado con �xito";
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(null, mensaje));
			// servicio de context reasoner
			// (url servicio, situaci�n, lista de adaptaciones con su data)

		}
		return "inicio";
	}

	public String getAtributeName() {
		return this.atributeName;
	}

	public List<String> getContextualDatumList() {

		List<String> list = (List<String>) RemoteInvokerUtils.invoke(RemoteInvokerUtils.ContextReasonerConfigService, "getDatum", null, ServiceIp.ContextReasonerIp);
		if (list != null) {
			return this.contextualDatumList;
		} else {
			return mocker();
		}
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

	public void setAtributeName(String atributeName) {
		this.atributeName = atributeName;
	}

	public void setContextualDatumList(List<String> contextualDatumList) {
		this.contextualDatumList = contextualDatumList;
	}

	public void setSession(SessionBean session) {
		this.session = session;
	}

}
