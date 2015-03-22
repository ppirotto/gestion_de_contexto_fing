package edu.fing.context.reasoner.bean;

import edu.fing.commons.front.dto.AvailableRulesTO;
import edu.fing.commons.front.dto.FrontResponseTO;
import edu.fing.commons.front.dto.VersionTO;

public interface CEPService {

	FrontResponseTO updateActiveVersion(String versionNumber);

	AvailableRulesTO getAvailableRules();

	FrontResponseTO createNewVersion(VersionTO version);

	VersionTO getActiveVersion();

	VersionTO getLastVersion();

}
