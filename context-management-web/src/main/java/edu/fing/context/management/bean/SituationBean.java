package edu.fing.context.management.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
import edu.fing.commons.front.dto.FrontResponseTO;
import edu.fing.commons.front.dto.RuleTO;
import edu.fing.commons.front.dto.RuleTemplateTO;
import edu.fing.commons.front.dto.SituationTO;
import edu.fing.commons.front.dto.VersionTO;
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
	private List<String> selectedOutputData;

	private String selectedContextSource;

	@SuppressWarnings("unchecked")
	private List<String> contextSources = (List<String>) RemoteInvokerUtils.invoke(RemoteInvokerUtils.ContextReasonerConfigService, "getContextSources", null, ServiceIp.ContextReasonerIp);

	@SuppressWarnings("unchecked")
	private List<String> contextDataList = (List<String>) RemoteInvokerUtils.invoke(RemoteInvokerUtils.ContextReasonerConfigService, "getContextData", null, ServiceIp.ContextReasonerIp);
	private List<String> selectedInputData;

	private TreeNode root = new DefaultTreeNode("Fuente de contexto", null);

	private RuleTO selectedRule;
	private VersionTO versionRules;

	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean session;

	public String crearSituacion() {
		System.out.println("Crear situaci�n");

		SituationTO sit = new SituationTO();
		sit.setName(this.name);
		sit.setDescription(this.getDescription());
		sit.setDuration(this.duration);
		sit.setVersionTO(this.versionRules);

		sit.setContextSources(new ArrayList(this.mappedContextData.values()));
		sit.setOutputContextData(this.selectedOutputData);

		FrontResponseTO response = (FrontResponseTO) RemoteInvokerUtils.invoke(RemoteInvokerUtils.ContextReasonerConfigService, "createSituation", sit, ServiceIp.ContextReasonerIp);
		if (response.isSuccess()) {
			String mensaje = "Servicio creado con �xito";
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(null, mensaje));

		} else {
			//MOSTRAR MENSAJE DE ERROR!!!
		}
		return "inicio";
	}

	public void createDatumForInput() {
		ContextSourceTO csTo = new ContextSourceTO();

		csTo.setEventName(this.selectedContextSource);
		csTo.setContextData(this.getSelectedInputData());

		if (this.getMappedContextData().get(this.selectedContextSource) == null) {
			this.getMappedContextData().put(this.selectedContextSource, csTo);

			// Solo para vista
			TreeNode node0 = new DefaultTreeNode(this.selectedContextSource, this.root);

			for (String data : this.getSelectedInputData()) {
				TreeNode node01 = new DefaultTreeNode(data, node0);

			}
		} else {// piso lo viejo
			this.getMappedContextData().put(this.selectedContextSource, csTo);

			// Solo para vista

			List<TreeNode> children = this.root.getChildren();
			for (TreeNode treeNode : children) {

				String name = (String) treeNode.getData();
				if (name.equals(this.selectedContextSource)) {
					treeNode.getChildren().clear();
					for (String data : this.getSelectedInputData()) {
						TreeNode node01 = new DefaultTreeNode(data, treeNode);

					}
				}

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

	public String getSelectedContextSource() {
		return this.selectedContextSource;
	}

	public List<String> getSelectedInputData() {
		return this.selectedInputData;
	}

	public List<String> getSelectedOutputData() {
		return this.selectedOutputData;
	}

	public RuleTO getSelectedRule() {
		return this.selectedRule;
	}

	public SessionBean getSession() {
		return this.session;
	}

	public VersionTO getVersionRules() {
		return this.versionRules;
	}

	public boolean isReadOnly() {
		if (this.selectedRule != null) {
			return !this.name.equals(this.selectedRule.getName());
		} else {
			return true;
		}

	}

	private VersionTO mocker() {

		VersionTO v1 = new VersionTO();
		v1.setCreationDate(new Date());
		v1.setVersionNumber("2.0");
		List<RuleTO> rules = new ArrayList<RuleTO>();
		RuleTO r = new RuleTO();
		r.setName("InTheEvening");
		r.setDrl("La regla de la posta posta");

		RuleTO r2 = new RuleTO();
		r2.setName("OTRA regla");
		r2.setDrl("otraaaaaaaaaaaaaaaaa");
		rules.add(r);
		rules.add(r2);
		v1.setRules(rules);

		return v1;
	}

	public String onFlowProcess(FlowEvent event) {
		if (event.getNewStep().equals("ruleTab")) {

			// llamar servicio de
			// this.displayRule = "llamar servicio que crea el template";
			RuleTemplateTO ruleTempTO = new RuleTemplateTO();
			ruleTempTO.setDescription(description);
			ruleTempTO.setDuration(duration);

			ruleTempTO.setMappedContextData(new ArrayList<ContextSourceTO>(
					mappedContextData.values()));
			ruleTempTO.setSituationName(name);
			ruleTempTO.setSelectedOutputData(selectedOutputData);

			// this.displayRule =
			// RuleTemplateService.createRuleTemplate(ruleTempTO);
			try {
				String generatedRule = RuleTemplateService.createRuleTemplate(ruleTempTO);
				VersionTO versionTO = (VersionTO) RemoteInvokerUtils.invoke(RemoteInvokerUtils.ContextReasonerCEPService, 
						"getLastVersion", null, ServiceIp.ContextReasonerIp);
				RuleTO rule = new RuleTO();
				rule.setDrl(generatedRule);
				rule.setName(name);
				versionTO.getRules().add(rule);
				this.setVersionRules(versionTO);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			for (RuleTO ruleTO : this.getVersionRules().getRules()) {
				if (ruleTO.getName().equals(this.name)) {
					this.selectedRule = ruleTO;
				}
			}

		}
		return event.getNewStep();
	}

	public void selectedRuleChanged() {

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

	public void setSelectedContextSource(String selectedContextSource) {
		this.selectedContextSource = selectedContextSource;
	}

	public void setSelectedInputData(List<String> selectedInputData) {
		this.selectedInputData = selectedInputData;
	}

	public void setSelectedOutputData(List<String> selectedOutputData) {
		this.selectedOutputData = selectedOutputData;
	}

	public void setSelectedRule(RuleTO selectedRule) {
		this.selectedRule = selectedRule;
	}

	public void setSession(SessionBean session) {
		this.session = session;
	}

	public void setVersionRules(VersionTO versionRules) {
		this.versionRules = versionRules;
	}
}
