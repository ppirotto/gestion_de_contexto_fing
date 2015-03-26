package edu.fing.context.management.bean;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import edu.fing.commons.front.dto.ContextSourceTO;
import edu.fing.context.management.dto.InfoTreeNode;
import edu.fing.context.management.util.RemoteInvokerUtils;
import edu.fing.context.management.util.RemoteInvokerUtils.ServiceIp;

@ManagedBean
@ViewScoped
public class ViewContextDataBean {

	private TreeNode root;

	private List<ContextSourceTO> contextSources;

	@PostConstruct
	public void construct() {

		this.setContextSources((List<ContextSourceTO>) RemoteInvokerUtils
				.invoke(RemoteInvokerUtils.ContextReasonerConfigService, "getContextSourcesWithContextData", null, ServiceIp.ContextReasonerIp));

		this.root = new DefaultTreeNode("Root", null);
		if (this.getContextSources() != null) {
			for (ContextSourceTO conextSource : this.getContextSources()) {
				TreeNode node0 = new DefaultTreeNode(new InfoTreeNode(conextSource.getEventName(), null, "Fuente de Contexto" + " - " + conextSource.getReceiveMode().toLowerCase()), this.root);
				for (String data : conextSource.getContextData()) {
					new DefaultTreeNode(new InfoTreeNode(data, null, "Dato Contextual"), node0);

				}
			}
		}
	}

	public List<ContextSourceTO> getContextSources() {
		return this.contextSources;
	}

	public TreeNode getRoot() {
		return this.root;
	}

	public void setContextSources(List<ContextSourceTO> contextSources) {
		this.contextSources = contextSources;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

}
