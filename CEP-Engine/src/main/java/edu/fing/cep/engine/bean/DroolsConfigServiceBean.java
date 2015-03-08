package edu.fing.cep.engine.bean;

import javax.inject.Inject;

import org.switchyard.component.bean.Reference;
import org.switchyard.component.bean.Service;

import edu.fing.commons.front.dto.AvailableRulesTO;
import edu.fing.commons.front.dto.CreateRulesVersionResponseTO;
import edu.fing.commons.front.dto.VersionTO;

@Service(DroolsConfigService.class)
public class DroolsConfigServiceBean implements DroolsConfigService {

	@Inject
	@Reference	
	private DroolsManagerService droolsManager;
	
	@Override
	public Boolean deployVersion(VersionTO version) {
		// TODO Auto-generated method stub
		try {
			droolsManager.deployVersion(version);
			droolsManager.updateActiveVersion(version.getVersionNumber());
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
	public CreateRulesVersionResponseTO createNewVersion(VersionTO version) {
		// TODO Auto-generated method stub
		return droolsManager.createNewVersion(version);
	}

}
