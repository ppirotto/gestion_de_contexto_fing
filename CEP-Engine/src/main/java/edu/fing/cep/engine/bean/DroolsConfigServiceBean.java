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
	public Boolean deployVersion(String versionNumber) {
		// TODO Auto-generated method stub
		try {
			droolsManager.deployVersion(versionNumber);
			droolsManager.updateActiveVersion(versionNumber);
			return Boolean.TRUE;
		} catch (Exception e){
			return Boolean.FALSE;
		}
	}
	
//	@Override
//	public String updateActiveVersion(String versionNumber) {
//		// TODO Auto-generated method stub
//		droolsManager.updateActiveVersion(versionNumber);
//		return  "OK!";
//	}

	@Override
	public AvailableRulesTO getAvailableRules() {
		// TODO Auto-generated method stub
		return droolsManager.getAvailableRules();
	}

	@Override
	public Boolean createNewVersion(VersionTO version) {
		// TODO Auto-generated method stub
		return droolsManager.createNewVersion(version);
	}
	
	

}
