package edu.fing.adaptation.manager;

import edu.fing.commons.dto.AdaptedMessage;

public interface DelayService {

	AdaptedMessage submit(AdaptedMessage adaptedMessage);
}
