package edu.fing.adaptation.manager;

import edu.fing.commons.dto.AdaptedMessage;

public interface RoutingService {

	AdaptedMessage submit(AdaptedMessage adaptedMessage);
}
