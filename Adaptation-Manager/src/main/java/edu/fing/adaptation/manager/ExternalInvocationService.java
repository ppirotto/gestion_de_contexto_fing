package edu.fing.adaptation.manager;

import edu.fing.commons.dto.AdaptedMessage;

public interface ExternalInvocationService {

	AdaptedMessage submit(AdaptedMessage adaptedMessage);
}
