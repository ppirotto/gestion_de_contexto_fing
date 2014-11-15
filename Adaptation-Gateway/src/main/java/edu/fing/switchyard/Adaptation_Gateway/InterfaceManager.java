package edu.fing.switchyard.Adaptation_Gateway;

import edu.fing.commons.AdaptedMessage;

public interface InterfaceManager {
	String adaptedMessage(AdaptedMessage input);

	void submit(AdaptedMessage input);
}
