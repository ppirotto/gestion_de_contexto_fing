package edu.fing.context.management.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import edu.fing.commons.front.dto.AvailableRulesTO;
import edu.fing.commons.front.dto.CreateRulesVersionResponseTO;
import edu.fing.commons.front.dto.RuleTO;
import edu.fing.commons.front.dto.VersionTO;
import edu.fing.context.management.util.RemoteInvokerUtils;
import edu.fing.context.management.util.RemoteInvokerUtils.ServiceIp;

@ManagedBean
@ViewScoped
public class RulesBean {

	private AvailableRulesTO res;
	private VersionTO selectedVersion;
	private RuleTO selectedRule;
	private String activeVersion;
	private String newVersionName;
	private VersionTO versionToDeploy;

	private String newRuleName;

	public RulesBean() {

	}

	public void addRule() {
		RuleTO rule = new RuleTO();
		rule.setName(this.newRuleName);

		this.versionToDeploy.getRules().add(rule);

	}

	@PostConstruct
	public void charge() {
		this.res = (AvailableRulesTO) RemoteInvokerUtils.invoke(RemoteInvokerUtils.DroolsConfigService, "getAvailableRules", null, ServiceIp.CepEngineIP);
		if (this.res == null) {
			mocker();
		}

		if (this.res.getVersions() != null) {
			this.selectedVersion = this.res.getVersions().get(0);
			for (VersionTO v : this.res.getVersions()) {
				if (v.getId().equals(this.res.getActiveVersionId())) {
					this.activeVersion = v.getVersionNumber();
					this.newVersionName = this.activeVersion;
				}
			}
		}

	}

	public void createVersion() {
		System.out.println("createVersion");
		this.selectedVersion.setVersionNumber(this.newVersionName);
		this.selectedVersion.setId(null);
		CreateRulesVersionResponseTO response = (CreateRulesVersionResponseTO) RemoteInvokerUtils.invoke(RemoteInvokerUtils.DroolsConfigService, "createNewVersion", this.selectedVersion,
				ServiceIp.CepEngineIP);

		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, response.getErrorCode(), response.getErrorMessage()));
		System.out.println("createNewVersion... responseCode: " + response.toString());
	}

	public void deployVersion() {
		System.out.println("deployVersion");
		CreateRulesVersionResponseTO response = (CreateRulesVersionResponseTO) RemoteInvokerUtils.invoke(RemoteInvokerUtils.DroolsConfigService, "deployVersion",
				this.versionToDeploy.getVersionNumber(), ServiceIp.CepEngineIP);
		System.out.println("deployVersion... responseCode: " + response.toString());
	}

	public String getActiveVersion() {
		return this.activeVersion;
	}

	public String getNewRuleName() {
		return this.newRuleName;
	}

	public String getNewVersionName() {
		return this.newVersionName;
	}

	public AvailableRulesTO getRes() {
		return this.res;
	}

	public RuleTO getSelectedRule() {
		return this.selectedRule;
	}

	public VersionTO getSelectedVersion() {
		return this.selectedVersion;
	}

	public VersionTO getVersionToDeploy() {
		return this.versionToDeploy;
	}

	private void mocker() {
		this.res = new AvailableRulesTO();
		this.res.setActiveVersionId((long) 3.3);
		this.res.setLastDeployDate(new Date());
		ArrayList a = new ArrayList<VersionTO>();
		VersionTO v1 = new VersionTO();
		v1.setCreationDate(new Date());
		v1.setId((long) 2);
		v1.setVersionNumber("2.0");
		List<RuleTO> rules = new ArrayList<RuleTO>();
		RuleTO r = new RuleTO();
		r.setId(1);
		r.setName("REGLA POSTA");
		r.setDrl("gggggggggggggggggggggggggggg");
		rules.add(r);
		v1.setRules(rules);
		a.add(v1);
		a.add(v1);
		this.res.setVersions(a);
	}

	public void saveRuleAction() {
		System.out.println("saveRuleAction");
	}

	public void selectedRuleChanged() {
		System.out.println("selectedRuleChanged");
	}

	public void selectedVersionChanged() {
		System.out.println("selectedVersionChanged");
	}

	public void setActiveVersion(String activeVersion) {
		this.activeVersion = activeVersion;
	}

	public void setNewRuleName(String newRuleName) {
		this.newRuleName = newRuleName;
	}

	public void setNewVersionName(String newVersionName) {
		this.newVersionName = newVersionName;
	}

	public void setRes(AvailableRulesTO res) {
		this.res = res;
	}

	public void setSelectedRule(RuleTO selectedRule) {
		this.selectedRule = selectedRule;
	}

	public void setSelectedVersion(VersionTO selectedVersion) {
		this.selectedVersion = selectedVersion;
	}

	public void setVersionToDeploy(VersionTO versionToDeploy) {
		this.versionToDeploy = versionToDeploy;
	}

}