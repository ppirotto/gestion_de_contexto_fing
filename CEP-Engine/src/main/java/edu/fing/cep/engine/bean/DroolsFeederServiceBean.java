package edu.fing.cep.engine.bean;

import javax.inject.Inject;

import org.switchyard.component.bean.Reference;
import org.switchyard.component.bean.Service;

import edu.fing.commons.dto.ContextualDataTO;

@Service(DroolsFeederService.class)
public class DroolsFeederServiceBean implements DroolsFeederService {

	@Inject
	@Reference
	private DroolsCore droolsCore;
	
	@Override
	public String receiveMessage(ContextualDataTO data) {
		return droolsCore.receiveMessage(data);
	}

}
