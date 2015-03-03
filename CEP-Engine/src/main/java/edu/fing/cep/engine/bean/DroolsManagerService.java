package edu.fing.cep.engine.bean;

import edu.fing.commons.dto.ContextualDataTO;
import edu.fing.commons.front.dto.AvailableRulesTO;
import edu.fing.commons.front.dto.CreateRulesVersionResponseTO;
import edu.fing.commons.front.dto.VersionTO;

public interface DroolsManagerService {

	String insert(ContextualDataTO data);

	CreateRulesVersionResponseTO deployVersion(String versionNumber);

	void updateActiveVersion(String versionNumber);

	AvailableRulesTO getAvailableRules();

	CreateRulesVersionResponseTO createNewVersion(VersionTO version);
}
