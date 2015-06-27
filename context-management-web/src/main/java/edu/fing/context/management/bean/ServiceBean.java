package edu.fing.context.management.bean;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

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
public class ServiceBean {

	private String description;
	private String ip;
	private List<String> operations = new ArrayList<String>();
	private List<String> selectedOperations;
	private List<String> situationList;
	private String serviceName;
	private String serviceURL;

	public String crearServicio() {
		System.out.println("Crear servicio");
		// llamar servicio para crear virtual service
		VirtualServiceDto vS = new VirtualServiceDto();
		vS.setServiceName(this.serviceName);
		vS.setServiceURL(this.serviceURL);

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

		Boolean result = (Boolean) RemoteInvokerUtils.invoke(RemoteInvokerUtils.ContextReasonerConfigService,
				"createService", serv, ServiceIp.ContextReasonerIp);
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
		try {

			if (!this.serviceURL.isEmpty()) {
				this.operations.clear();
				WSDLParser parser = new WSDLParser();

				Definitions defs = parser.parse(this.serviceURL + "?wsdl");
				this.serviceName = defs.getServices().get(0).getName();

				for (Binding pt : defs.getBindings()) {
					for (BindingOperation op : pt.getOperations()) {
						if (op.getBinding().getName().toLowerCase().contains("soap")) {
							op.getBinding().getPortType().getName();
							this.operations.add(op.getName());
						}
					}
				}
			}
		} catch (Exception e) {
			RequestContext currentInstance = RequestContext.getCurrentInstance();

			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "El servicio no es válido");
			currentInstance.showMessageInDialog(message);
		}

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

	public List<String> getSituationList() {
		return this.situationList;
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

	public void setSituationList(List<String> situationList) {
		this.situationList = situationList;
	}

}
