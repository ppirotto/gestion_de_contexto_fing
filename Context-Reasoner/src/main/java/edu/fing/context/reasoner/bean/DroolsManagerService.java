package edu.fing.context.reasoner.bean;

import edu.fing.commons.dto.ContextualDataTO;
import edu.fing.commons.front.dto.AvailableRulesTO;
import edu.fing.commons.front.dto.FrontResponseTO;
import edu.fing.commons.front.dto.VersionTO;

public interface DroolsManagerService {

	String insert(ContextualDataTO data);

	FrontResponseTO deployVersion(VersionTO desiredVersion);

	void updateActiveVersion(String versionNumber);

	AvailableRulesTO getAvailableRules();

	FrontResponseTO createNewVersion(VersionTO version);

	FrontResponseTO testDroolsCompiling(VersionTO versionTO);

}
