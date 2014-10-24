package edu.fing.switchyard.CEP_Engine;

import java.util.HashMap;

import javax.inject.Inject;

import org.switchyard.component.bean.Reference;
import org.switchyard.component.bean.Service;

import edu.fing.switchyard.CEP_Engine.drools.DroolsManagerService;

@Service(DroolsConfigService.class)
public class DroolsConfigServiceBean implements DroolsConfigService {

	@Inject
	@Reference	
	private DroolsManagerService droolsManager;
	
	@Override
	public String addRule() {
		// TODO Auto-generated method stub
		droolsManager.addRule();
		return null;
	}

}
