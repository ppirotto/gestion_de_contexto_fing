package edu.fing.switchyard.CEP_Engine;

import java.util.HashMap;

import javax.inject.Inject;

import org.switchyard.component.bean.Reference;
import org.switchyard.component.bean.Service;

import edu.fing.switchyard.CEP_Engine.drools.DroolsManagerService;

@Service(DroolsFeederService.class)
public class DroolsFeederServiceBean implements DroolsFeederService {

	@Inject
	@Reference	
	private DroolsManagerService droolsManager;
	
	@Override
	public String receiveMessage(HashMap<String, String> input) {
		
		droolsManager.insert(input);
		return null;
	}

}
