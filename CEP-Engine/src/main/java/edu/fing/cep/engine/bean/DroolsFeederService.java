package edu.fing.cep.engine.bean;

import edu.fing.commons.dto.ContextualDataTO;

public interface DroolsFeederService {
	
	String receiveMessage(ContextualDataTO data);

}
