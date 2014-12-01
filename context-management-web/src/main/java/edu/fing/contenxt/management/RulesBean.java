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
    
    
	public RulesBean(){
 
    }
 
    @PostConstruct
    public void charge(){
    	res = RemoteInvokerUtils.invoke(RemoteInvokerUtils.DroolsConfigService, "getAvailableRules", null, AvailableRulesTO.class);
    }

    public void selectedVersionChanged(){
    	System.out.println("selectedVersionChanged");
    }
    
    public void selectedRuleChanged(){
    	System.out.println("selectedRuleChanged");
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
 
//    public AvailableRulesTO availableRules(){
//		return res;
//    }
    
    
}