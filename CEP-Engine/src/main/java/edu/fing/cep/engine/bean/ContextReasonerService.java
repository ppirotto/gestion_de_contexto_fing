package edu.fing.cep.engine.bean;

import edu.fing.commons.front.dto.AvailableRulesTO;
import edu.fing.commons.front.dto.FrontResponseTO;
import edu.fing.commons.front.dto.VersionTO;

public interface ContextReasonerService {
	void updateActiveVersion(String versionNumber);

	AvailableRulesTO getAvailableRules();

	FrontResponseTO createNewVersion(VersionTO version);

	VersionTO getActiveVersion();
}
