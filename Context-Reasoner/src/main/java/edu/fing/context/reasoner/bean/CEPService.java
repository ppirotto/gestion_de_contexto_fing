package edu.fing.context.reasoner.bean;

import edu.fing.commons.front.dto.AvailableRulesTO;
import edu.fing.commons.front.dto.CreateRulesVersionResponseTO;
import edu.fing.commons.front.dto.VersionTO;

public interface CEPService {

	CreateRulesVersionResponseTO updateActiveVersion(String versionNumber);

	AvailableRulesTO getAvailableRules();

	CreateRulesVersionResponseTO createNewVersion(VersionTO version);

	VersionTO getActiveVersion();

}
