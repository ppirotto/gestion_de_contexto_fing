package edu.fing.adaptation.manager;

import edu.fing.commons.dto.AdaptedMessage;

public interface ServiceInvoker {

	AdaptedMessage submit(AdaptedMessage adaptedMessage);
}
