package edu.fing.cep.engine.bean;

import edu.fing.commons.front.dto.AvailableRulesTO;

public interface DroolsConfigService {

	String deployVersion(String versionNumber);

	String updateActiveVersion(String versionNumber);
	
	AvailableRulesTO getAvailableRules();
	
}
