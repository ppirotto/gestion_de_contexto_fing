package edu.fing.context.management.bean;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import edu.fing.commons.front.dto.AvailableRulesTO;
import edu.fing.commons.front.dto.FrontResponseTO;
import edu.fing.commons.front.dto.RuleTO;
import edu.fing.commons.front.dto.VersionTO;
import edu.fing.context.management.util.RemoteInvokerUtils;
import edu.fing.context.management.util.RemoteInvokerUtils.ServiceIp;

@ManagedBean
@ViewScoped
public class RulesBean {

	private AvailableRulesTO res;
	private VersionTO selectedVersion;
	private RuleTO selectedRule = (new RuleTO());
	private String activeVersion;
	private String newVersionName;
	private VersionTO versionToDeploy;

	private String newRuleName;

	private FrontResponseTO responseMsg;

	public RulesBean() {

	}

	@PostConstruct
	public void charge() {
		this.res = (AvailableRulesTO) RemoteInvokerUtils.invoke(RemoteInvokerUtils.ContextReasonerCEPService,
				"getAvailableRules", null, ServiceIp.ContextReasonerIp);

		if (this.res.getVersions() != null) {
			this.selectedVersion = this.res.getVersions().get(0);
			for (VersionTO v : this.res.getVersions()) {
				if (v.getVersionNumber().equals(this.res.getActiveVersionNumber())) {
					this.activeVersion = v.getVersionNumber();
					this.newVersionName = this.activeVersion;
				}
			}
		}

	}

	public String refreshIfValid() {
		if (responseMsg.isSuccess()) {

			return "ruleManagement";
		} else
			return null;
	}

	public String createVersion() {
		System.out.println("createVersion");
		this.selectedVersion.setVersionNumber(this.newVersionName);
		this.setResponseMsg((FrontResponseTO) RemoteInvokerUtils.invoke(RemoteInvokerUtils.ContextReasonerCEPService,
				"createNewVersion", this.selectedVersion, ServiceIp.ContextReasonerIp));
		System.out.println("createNewVersion... responseCode: " + this.getResponseMsg().getErrorCode());

		if (this.getResponseMsg().isSuccess()) {
			FacesContext fctx = FacesContext.getCurrentInstance();
			FacesMessage message = new FacesMessage("OK", "Versión " + this.newVersionName + " creada");
			fctx.addMessage(null, message);

			return "ruleManagement";
		}

		else {
			return null;
		}
	}

	public void deployVersion() {
		System.out.println("deployVersion");
		FrontResponseTO response = (FrontResponseTO) RemoteInvokerUtils.invoke(
				RemoteInvokerUtils.ContextReasonerCEPService, "updateActiveVersion",
				this.versionToDeploy.getVersionNumber(), ServiceIp.ContextReasonerIp);
		this.setResponseMsg(response);
		if (response.isSuccess()) {

		}
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

	public FrontResponseTO getResponseMsg() {
		return this.responseMsg;
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

	public void saveRuleAction() {

	}

	public void selectedRuleChanged() {

	}

	public void selectedVersionChanged() {

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

	public void setResponseMsg(FrontResponseTO responseMsg) {
		this.responseMsg = responseMsg;
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