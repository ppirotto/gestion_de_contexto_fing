package edu.fing.contenxt.management;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

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
    
    
	public RulesBean(){
 
    }
 
    @PostConstruct
    public void charge(){
    	res = RemoteInvokerUtils.invoke(RemoteInvokerUtils.DroolsConfigService, "getAvailableRules", null, AvailableRulesTO.class);
    	if (res.getVersions()!=null){
    		selectedVersion=res.getVersions().get(0);
    		for (VersionTO v : res.getVersions()) {
				if (v.getId().equals(res.getActiveVersionId())){
					activeVersion = v.getVersionNumber();
					newVersionName = activeVersion;
				}
    		}
    	}
    }

    public void selectedVersionChanged(){
    	System.out.println("selectedVersionChanged");
    }
    
    public void selectedRuleChanged(){
    	System.out.println("selectedRuleChanged");
    }
    public void saveRuleAction(){
    	System.out.println("saveRuleAction");
    }
    
    public void deployVersion(){
    	System.out.println("deployVersion");
    	Boolean success = RemoteInvokerUtils.invoke(RemoteInvokerUtils.DroolsConfigService, "deployVersion", versionToDeploy.getVersionNumber(), Boolean.class);
    	System.out.println(success);
    }
    
    public void createVersion(){
    	System.out.println("createVersion");
    	selectedVersion.setVersionNumber(newVersionName);
    	selectedVersion.setId(null);
    	Boolean success = RemoteInvokerUtils.invoke(RemoteInvokerUtils.DroolsConfigService, "createNewVersion", selectedVersion, Boolean.class);
    	System.out.println(success);
    }
    
	public AvailableRulesTO getRes() {
		return res;
	}

	public void setRes(AvailableRulesTO res) {
		this.res = res;
	}


	public VersionTO getSelectedVersion() {
		return selectedVersion;
	}

	public void setSelectedVersion(VersionTO selectedVersion) {
		this.selectedVersion = selectedVersion;
	}

	public RuleTO getSelectedRule() {
		return selectedRule;
	}

	public void setSelectedRule(RuleTO selectedRule) {
		this.selectedRule = selectedRule;
	}

	public String getActiveVersion() {
		return activeVersion;
	}

	public void setActiveVersion(String activeVersion) {
		this.activeVersion = activeVersion;
	}

	public String getNewVersionName() {
		return newVersionName;
	}

	public void setNewVersionName(String newVersionName) {
		this.newVersionName = newVersionName;
	}

	public VersionTO getVersionToDeploy() {
		return versionToDeploy;
	}

	public void setVersionToDeploy(VersionTO versionToDeploy) {
		this.versionToDeploy = versionToDeploy;
	}
 
//    public AvailableRulesTO availableRules(){
//		return res;
//    }
    
    
}