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

import edu.fing.commons.front.dto.ContextSourceTO;
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

	private List<SituationTO> situations;

	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean session;

	@PostConstruct
	public void construct() {

		this.situations = (List<SituationTO>) RemoteInvokerUtils.invoke(RemoteInvokerUtils.ContextReasonerConfigService, "getSituationsWithContextData", null, ServiceIp.ContextReasonerIp);

		this.root = new DefaultTreeNode("Root", null);
		if (this.situations != null) {
			for (SituationTO situationTO : this.situations) {
				TreeNode node0 = new DefaultTreeNode(new InfoTreeNode(situationTO.getName(), situationTO.getRule(), "Situación"), this.root);

				TreeNode node01 = new DefaultTreeNode(new InfoTreeNode("Entrada", null, ""), node0);
				TreeNode node02 = new DefaultTreeNode(new InfoTreeNode("Salida", null, ""), node0);

				for (ContextSourceTO contextSourceTO : situationTO.getContextSources()) {
					TreeNode node001 = new DefaultTreeNode(new InfoTreeNode(contextSourceTO.getEventName(), contextSourceTO.getDescription(), "Fuente de Contexto"), node01);
					for (String contextData : contextSourceTO.getContextData()) {
						TreeNode node000 = new DefaultTreeNode(new InfoTreeNode(contextData, contextData, "Dato contextual"), node001);
					}
					//
				}

				for (String contextData : situationTO.getOutputContextData()) {
					new DefaultTreeNode(new InfoTreeNode(contextData, contextData, "Dato contextual"), node02);
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
