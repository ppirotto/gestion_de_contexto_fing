package edu.fing.adaptation.manager;

import edu.fing.commons.dto.AdaptedMessage;

public interface RoutingService {

	String submit(AdaptedMessage adaptedMessage);
}
