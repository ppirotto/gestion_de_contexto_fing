package edu.fing.cep.engine.bean;

import edu.fing.commons.front.dto.AvailableRulesTO;
import edu.fing.commons.front.dto.CreateRulesVersionResponseTO;
import edu.fing.commons.front.dto.VersionTO;

public interface DroolsConfigService {
	
	AvailableRulesTO getAvailableRules();
	
	CreateRulesVersionResponseTO createNewVersion(VersionTO version);

	Boolean deployVersion(VersionTO version);
}
