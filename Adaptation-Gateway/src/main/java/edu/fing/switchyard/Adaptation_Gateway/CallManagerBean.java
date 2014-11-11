package edu.fing.switchyard.Adaptation_Gateway;

import org.switchyard.component.bean.Service;

import edu.fing.commons.*;

@Service(CallManager.class)
public class CallManagerBean implements CallManager {

	// @Inject
	// @Reference
	// private ManagerService managerService;

	@Override
	public void hola() {

		AdaptedMessage adaptedMessage = new AdaptedMessage();
		adaptedMessage.setMessage("hola");

		// this.managerService.acceptMessage(adaptedMessage);
	}

}
