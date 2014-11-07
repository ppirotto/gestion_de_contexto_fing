package edu.fing.cep.engine.bean;

import javax.inject.Inject;

import org.switchyard.component.bean.Reference;
import org.switchyard.component.bean.Service;

@Service(DroolsConfigService.class)
public class DroolsConfigServiceBean implements DroolsConfigService {

	@Inject
	@Reference	
	private DroolsManagerService droolsManager;
	
	@Override
	public String deployVersion(String versionNumber) {
		// TODO Auto-generated method stub
		droolsManager.deployVersion(versionNumber);
		return null;
	}
	
	@Override
	public String updateActiveVersion(String versionNumber) {
		// TODO Auto-generated method stub
		droolsManager.updateActiveVersion(versionNumber);
		return null;
	}

}
