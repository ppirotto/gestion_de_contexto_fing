package edu.fing.switchyard.Service_Invoker;

import edu.fing.commons.dto.AdaptedMessage;

public interface InvokerService {

	AdaptedMessage submit(AdaptedMessage adaptedMessage);
}
