package edu.fing.cep.engine.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.switchyard.component.bean.Reference;
import org.switchyard.component.bean.Service;

import com.sun.tools.javac.resources.version;

import edu.fing.commons.front.dto.AvailableRulesTO;
import edu.fing.commons.front.dto.RuleTO;
import edu.fing.commons.front.dto.VersionTO;

@Service(DroolsConfigService.class)
public class DroolsConfigServiceBean implements DroolsConfigService {

	@Inject
	@Reference	
	private DroolsManagerService droolsManager;
	
	@Override
	public String deployVersion(String versionNumber) {
		// TODO Auto-generated method stub
		droolsManager.deployVersion(versionNumber);
		return "OK!";
	}
	
	@Override
	public String updateActiveVersion(String versionNumber) {
		// TODO Auto-generated method stub
		droolsManager.updateActiveVersion(versionNumber);
		return  "OK!";
	}

	@Override
	public AvailableRulesTO getAvailableRules() {
		// TODO Auto-generated method stub
		return droolsManager.getAvailableRules();
//		AvailableRulesTO res = new AvailableRulesTO();
//		res.setActiveVersionId(0);
//		res.setLastDeployDate(new Date());
//		List<VersionTO> versions = new ArrayList<VersionTO>();
//		VersionTO v = new VersionTO();
//		v.setCreationDate(new Date());
//		v.setId(2L);
//		v.setVersionNumber("concha");
//		List<RuleTO> rules = new ArrayList<RuleTO>();
//		RuleTO r = new RuleTO();
//		r.setId(123);
//		r.setName("conchuda");
//		r.setRule("regluda");
//		rules.add(r);
//		v.setRules(rules );
//		versions.add(v);
//		res.setVersions(versions );
//		return res;
//		return new AvailableRulesTO();
	}
	
	

}
