package edu.fing.cep.engine.bean;

import java.util.HashMap;

import javax.inject.Inject;

import org.switchyard.component.bean.Reference;
import org.switchyard.component.bean.Service;

@Service(DroolsFeederService.class)
public class DroolsFeederServiceBean implements DroolsFeederService {

	@Inject
	@Reference	
	private DroolsManagerService droolsManager;
	
	@Override
	public String receiveMessage(HashMap<String, String> input) {
		
		return droolsManager.insert(input);
	}
	
	

}
