package edu.fing.switchyard.Adaptation_Manager;

import edu.fing.commons.AdaptedMessage;

public interface AdaptationManagerI {

	String adaptedMessage(AdaptedMessage msg);

	String recieve(AdaptedMessage msg);
}
