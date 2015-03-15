package edu.fing.context.management.bean;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
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

import edu.fing.commons.front.dto.ServiceTO;
import edu.fing.context.management.dto.VirtualServiceDto;
import edu.fing.context.management.jar.creation.JarCreationService;
import edu.fing.context.management.util.RemoteInvokerUtils;
import edu.fing.context.management.util.RemoteInvokerUtils.ServiceIp;

@ManagedBean
@ViewScoped
public class ServiceBean implements Serializable {

	private String description;
	private String ip;
	private List<String> operations = new ArrayList<String>();
	private List<String> selectedOperations;

	private List<String> situationList;
	private String serviceName;
	private String serviceURL;
	private String virtualService;

	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean session;

	public String crearServicio() {
		System.out.println("Crear servicio");

		// llamar servicio para crear virtual service
		VirtualServiceDto vS = new VirtualServiceDto();
		vS.setServiceName(this.serviceName);
		vS.setServiceURL(this.serviceURL);
		vS.setVirtualService(this.virtualService);

		try {
			JarCreationService.createVirtualService(vS);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ServiceTO serv = new ServiceTO();
		serv.setOperationNames(this.selectedOperations);
		serv.setServiceName(this.serviceName);
		serv.setUrl(this.serviceURL);
		serv.setDescription(this.description);

		// //////////////////
		Boolean result = (Boolean) RemoteInvokerUtils.invoke(RemoteInvokerUtils.ContextReasonerConfigService, "createService", serv, ServiceIp.ContextReasonerIp);
		if (result) {

			String mensaje = "Servicio creado con exito";
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(null, mensaje));

		}
		return "inicio";
	}

	public String getDescription() {
		return this.description;
	}

	public String getIp() {
		if (this.ip == null) {
			InetAddress IP = null;
			try {
				IP = InetAddress.getLocalHost();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			this.ip = IP.getHostAddress();
		}
		return this.ip;
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

	public List<String> getSelectedOperations() {
		return this.selectedOperations;
	}

	public String getServiceName() {
		return this.serviceName;
	}

	public String getServiceURL() {
		return this.serviceURL;
	}

	public SessionBean getSession() {
		return this.session;
	}

	public List<String> getSituationList() {
		return this.situationList;
	}

	public String getVirtualService() {
		return this.virtualService;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setOperations(List<String> operations) {
		this.operations = operations;
	}

	public void setSelectedOperations(List<String> selectedOperations) {
		this.selectedOperations = selectedOperations;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public void setServiceURL(String serviceURL) {
		this.serviceURL = serviceURL;
	}

	public void setSession(SessionBean session) {
		this.session = session;
	}

	public void setSituationList(List<String> situationList) {
		this.situationList = situationList;
	}

	public void setVirtualService(String virtualService) {
		this.virtualService = virtualService;
	}

}
