package edu.fing.service.invoker;

import edu.fing.commons.dto.AdaptedMessage;

public interface InvokerService {

	AdaptedMessage submit(AdaptedMessage adaptedMessage);
}
