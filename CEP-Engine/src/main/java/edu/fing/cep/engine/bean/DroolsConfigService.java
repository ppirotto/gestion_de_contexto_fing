package edu.fing.cep.engine.bean;

import edu.fing.commons.front.dto.AvailableRulesTO;
import edu.fing.commons.front.dto.CreateRulesVersionResponseTO;
import edu.fing.commons.front.dto.VersionTO;

public interface DroolsConfigService {

	Boolean deployVersion(String versionNumber);

//	String updateActiveVersion(String versionNumber);
	
	AvailableRulesTO getAvailableRules();
	
	CreateRulesVersionResponseTO createNewVersion(VersionTO version);
}
