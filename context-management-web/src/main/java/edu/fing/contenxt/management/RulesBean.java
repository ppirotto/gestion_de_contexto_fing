package edu.fing.contenxt.management;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import edu.fing.commons.front.dto.AvailableRulesTO;
import edu.fing.commons.front.dto.RuleTO;
import edu.fing.commons.front.dto.VersionTO;

@ManagedBean
@ViewScoped
public class RulesBean {

	private AvailableRulesTO res;

	private VersionTO selectedVersion;

	private RuleTO selectedRule;

	private String activeVersion;

	private String newVersionName;

	private VersionTO versionToDeploy;

	public RulesBean() {

	}

	@PostConstruct
	public void charge() {
		this.res = RemoteInvokerUtils.invoke(RemoteInvokerUtils.DroolsConfigService, "getAvailableRules", null, AvailableRulesTO.class);
		if (this.res == null) {
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
			r.setRule("gggggggggggggggggggggggggggg");
			rules.add(r);

			v1.setRules(rules);

			a.add(v1);
			a.add(v1);

			this.res.setVersions(a);

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
		Boolean success = RemoteInvokerUtils.invoke(RemoteInvokerUtils.DroolsConfigService, "createNewVersion", this.selectedVersion, Boolean.class);
		System.out.println(success);
	}

	public void deployVersion() {
		System.out.println("deployVersion");
		Boolean success = RemoteInvokerUtils.invoke(RemoteInvokerUtils.DroolsConfigService, "deployVersion", this.versionToDeploy.getVersionNumber(), Boolean.class);
		System.out.println(success);
	}

	public String getActiveVersion() {
		return this.activeVersion;
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