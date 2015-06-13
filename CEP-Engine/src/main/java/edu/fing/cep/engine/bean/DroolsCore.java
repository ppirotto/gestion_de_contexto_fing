package edu.fing.cep.engine.bean;

import edu.fing.commons.dto.ContextualDataTO;
import edu.fing.commons.front.dto.FrontResponseTO;
import edu.fing.commons.front.dto.VersionTO;

public interface DroolsCore {
	
	FrontResponseTO deployVersion(VersionTO desiredVersion);

	FrontResponseTO testDroolsCompiling(VersionTO versionTO);

	void intializeDroolsContext();
	
	String receiveMessage(ContextualDataTO data);
}
