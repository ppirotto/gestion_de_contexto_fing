package edu.fing.context.management.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.FlowEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import edu.fing.commons.front.dto.ContextSourceTO;
import edu.fing.commons.front.dto.RuleTemplateTO;
import edu.fing.commons.front.dto.SituationTO;
import edu.fing.context.management.util.RemoteInvokerUtils;
import edu.fing.context.management.util.RemoteInvokerUtils.ServiceIp;
import edu.fing.context.management.util.RuleTemplateService;

@ManagedBean
@ViewScoped
public class SituationBean {

	private String description;
	private String name;
	private long duration;
	private Map<String, ContextSourceTO> mappedContextData = new HashMap<String, ContextSourceTO>();
	private List<String> selectedOutputDatum;

	private String selectedContextSource;

	private List<String> contextSources = (List<String>) RemoteInvokerUtils.invoke(RemoteInvokerUtils.ContextReasonerConfigService, "getContextSources", null, ServiceIp.ContextReasonerIp);

	private List<String> contextDataList = (List<String>) RemoteInvokerUtils.invoke(RemoteInvokerUtils.ContextReasonerConfigService, "getContextData", null, ServiceIp.ContextReasonerIp);
	private List<String> selectedInputDatum;

	private TreeNode root = new DefaultTreeNode("Fuente de contexto", null);


	private String rule;

	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean session;

	public String crearSituacion() {
		System.out.println("Crear situaci�n");

		SituationTO sit = new SituationTO();
		sit.setName(this.name);
		sit.setDescription(this.getDescription());
		sit.setDuration(this.duration);

		sit.setContextSources(new ArrayList(this.mappedContextData.values()));

		Boolean result = (Boolean) RemoteInvokerUtils.invoke(RemoteInvokerUtils.ContextReasonerConfigService, "createSituation", sit, ServiceIp.ContextReasonerIp);
		if (result) {
			String mensaje = "Servicio creado con �xito";
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(null, mensaje));

		}
		return "inicio";
	}

	public void createDatumForInput() {
		ContextSourceTO csTo = new ContextSourceTO();

		csTo.setEventName(this.selectedContextSource);
		csTo.setContextData(this.selectedInputDatum);

		if (this.getMappedContextData().get(this.selectedContextSource) == null) {
			this.getMappedContextData().put(this.selectedContextSource, csTo);

			// Solo para vista
			TreeNode node0 = new DefaultTreeNode(this.selectedContextSource, this.root);

			for (String data : this.selectedInputDatum) {
				TreeNode node01 = new DefaultTreeNode(data, node0);

			}
		}
	}

	public List<String> getContextDataList() {

		return this.contextDataList;
	}

	public List<String> getContextSources() {

		return this.contextSources;
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

	public Map<String, ContextSourceTO> getMappedContextData() {
		return this.mappedContextData;
	}

	public String getName() {
		return this.name;
	}

	public TreeNode getRoot() {
		return this.root;
	}

	public String getRule() {
		return this.rule;
	}

	public String getSelectedContextSource() {
		return this.selectedContextSource;
	}

	public List<String> getSelectedInputDatum() {
		return this.selectedInputDatum;
	}

	public List<String> getSelectedOutputDatum() {
		return this.selectedOutputDatum;
	}

	public SessionBean getSession() {
		return this.session;
	}

	public String onFlowProcess(FlowEvent event) {
		if (event.getNewStep().equals("ruleTab")) {

			// llamar servicio de
			this.rule = "llamar servicio que crea el template";
			RuleTemplateTO ruleTempTO = new RuleTemplateTO();
			ruleTempTO.setDescription(description);
			ruleTempTO.setDuration(duration);
			ruleTempTO.setMappedContextData(mappedContextData);
			ruleTempTO.setName(name);
			ruleTempTO.setSelectedOutputDatum(selectedOutputDatum);
			try {
				this.rule = RuleTemplateService.createRuleTemplate(ruleTempTO);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return event.getNewStep();

	}

	public void setContextDataList(List<String> contextDataList) {
		this.contextDataList = contextDataList;
	}

	public void setContextSources(List<String> contextSources) {
		this.contextSources = contextSources;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public void setMappedContextData(Map<String, ContextSourceTO> mappedContextData) {
		this.mappedContextData = mappedContextData;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	public void setSelectedContextSource(String selectedContextSource) {
		this.selectedContextSource = selectedContextSource;
	}

	public void setSelectedInputDatum(List<String> selectedInputDatum) {
		this.selectedInputDatum = selectedInputDatum;
	}

	public void setSelectedOutputDatum(List<String> selectedOutputDatum) {
		this.selectedOutputDatum = selectedOutputDatum;
	}

	public void setSession(SessionBean session) {
		this.session = session;
	}
}
