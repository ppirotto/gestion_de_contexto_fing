package edu.fing.context.management.bean;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import edu.fing.commons.dto.AdaptationTO;
import edu.fing.commons.front.dto.ServiceTO;
import edu.fing.commons.front.dto.SituationTO;
import edu.fing.context.management.dto.InfoTreeNode;
import edu.fing.context.management.util.RemoteInvokerUtils;
import edu.fing.context.management.util.RemoteInvokerUtils.ServiceIp;

@ManagedBean
@ViewScoped
public class ViewSituationContextDataBean {

	private String description;
	private String serviceName;
	private TreeNode root;
	private InfoTreeNode selectedNode;

	private List<ServiceTO> services;

	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean session;

	@PostConstruct
	public void construct() {

		this.services = (List<ServiceTO>) RemoteInvokerUtils.invoke(RemoteInvokerUtils.ContextReasonerConfigService, "getServicesWithSituationsAndAdaptations", null, ServiceIp.ContextReasonerIp);

		this.root = new DefaultTreeNode("Root", null);
		if (this.services != null) {
			for (ServiceTO serviceTO : this.services) {
				TreeNode node0 = new DefaultTreeNode(new InfoTreeNode(serviceTO.getServiceName() + "/" + serviceTO.getOperationName(), serviceTO.getUrl(), "Servicio"), this.root);
				for (SituationTO situationTO : serviceTO.getSituations()) {
					TreeNode node00 = new DefaultTreeNode(new InfoTreeNode(situationTO.getName(), situationTO.getDescription(), "Situación"), node0);
					for (AdaptationTO adaptationTO : situationTO.getAdaptations()) {
						TreeNode node000 = new DefaultTreeNode(new InfoTreeNode(adaptationTO.getAdaptationType().toString(), (String) adaptationTO.getData(), "Adaptación"), node00);
					}

				}
			}
		}
	}

	public String getDescription() {
		return this.description;
	}

	public TreeNode getRoot() {
		return this.root;
	}

	public InfoTreeNode getSelectedNode() {
		return this.selectedNode;
	}

	public String getServiceName() {
		return this.serviceName;
	}

	public SessionBean getSession() {
		return this.session;
	}

	public void onNodeSelect(NodeSelectEvent event) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected", event.getTreeNode().toString());
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public void setSelectedNode(InfoTreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public void setSession(SessionBean session) {
		this.session = session;
	}
}
