package edu.fing.cep.engine.bean;

import javax.inject.Inject;

import org.switchyard.component.bean.Reference;
import org.switchyard.component.bean.Service;

import edu.fing.commons.front.dto.FrontResponseTO;
import edu.fing.commons.front.dto.VersionTO;

@Service(DroolsManagerService.class)
public class DroolsManagerServiceBean implements DroolsManagerService{

	@Inject
	@Reference
	private DroolsCore droolsCore;
	
	@Override
	public FrontResponseTO deployVersion(VersionTO desiredVersion) {
		return droolsCore.deployVersion(desiredVersion);
	}

	@Override
	public FrontResponseTO testDroolsCompiling(VersionTO versionTO) {
		return droolsCore.testDroolsCompiling(versionTO);
	}

	@Override
	public void intializeDroolsContext() {
		droolsCore.intializeDroolsContext();
	}

}
