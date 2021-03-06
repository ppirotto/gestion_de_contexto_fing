package edu.fing.cep.engine.bean;

import edu.fing.commons.front.dto.FrontResponseTO;
import edu.fing.commons.front.dto.VersionTO;

public interface DroolsManagerService {
	
	FrontResponseTO deployVersion(VersionTO desiredVersion);

	FrontResponseTO testDroolsCompiling(VersionTO versionTO);

	void intializeDroolsContext();

}
