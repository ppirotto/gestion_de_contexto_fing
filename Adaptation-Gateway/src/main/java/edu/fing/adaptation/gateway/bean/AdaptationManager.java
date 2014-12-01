package edu.fing.adaptation.gateway.bean;

import edu.fing.commons.dto.AdaptedMessage;

public interface AdaptationManager {

	String submit(AdaptedMessage adaptedMessage);
}
