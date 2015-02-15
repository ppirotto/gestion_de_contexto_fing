package edu.fing.contenxt.management;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.predic8.wsdl.Binding;
import com.predic8.wsdl.BindingOperation;
import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.WSDLParser;

@ManagedBean
@ViewScoped
public class ServiceBean {

	private String descripcion;

	private List<String> operations = new ArrayList<String>();
	private String selectedOperation;

	private List<String> situationList;
	private String serviceName;
	private String serviceURL;
	private String virtualService;

	@ManagedProperty(value = "#{sesionBean}")
	private SesionBean sesion;

	public String crearServicio() {
		System.out.println("Crear servicio");
		String mensaje = "Servicio creado con éxito";
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(null, mensaje));

		// servicio de context reasoner
		// (url servicio, situación, lista de adaptaciones con su data)
		return "inicio";
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public List<String> getOperations() {
		return this.operations;
	}

	public void getOperationsForService() {
		if (!this.serviceURL.isEmpty()) {
			this.operations.clear();
			WSDLParser parser = new WSDLParser();

			Definitions defs = parser.parse(this.serviceURL + "?wsdl");

			for (Binding pt : defs.getBindings()) {

				for (BindingOperation op : pt.getOperations()) {
					this.operations.add(op.getName());
				}
			}
		} else {

		}

		// System.out.println(this.getFiles().size());
		// FacesMessage message = null;
		// if (!this.adaptations.isEmpty()) {
		// message = new FacesMessage("Succesful", " is uploaded.");
		//
		// } else {
		// message = new FacesMessage("Error", " is uploaded.");
		// }
		// FacesContext.getCurrentInstance().addMessage("growl2", message);
	}

	public String getSelectedOperation() {
		return this.selectedOperation;
	}

	public String getServiceName() {
		return this.serviceName;
	}

	public String getServiceURL() {
		return this.serviceURL;
	}

	public SesionBean getSesion() {
		return this.sesion;
	}

	public List<String> getSituationList() {
		return this.situationList;
	}

	public String getVirtualService() {
		return this.virtualService;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setOperations(List<String> operations) {
		this.operations = operations;
	}

	public void setSelectedOperation(String selectedOperation) {
		this.selectedOperation = selectedOperation;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public void setServiceURL(String serviceURL) {
		this.serviceURL = serviceURL;
	}

	public void setSesion(SesionBean sesion) {
		this.sesion = sesion;
	}

	public void setSituationList(List<String> situationList) {
		this.situationList = situationList;
	}

	public void setVirtualService(String virtualService) {
		this.virtualService = virtualService;
	}

}
