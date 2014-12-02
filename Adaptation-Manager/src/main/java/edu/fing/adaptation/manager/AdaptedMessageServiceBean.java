package edu.fing.adaptation.manager;

import javax.inject.Inject;

import org.switchyard.component.bean.Reference;
import org.switchyard.component.bean.Service;

import edu.fing.commons.dto.AdaptedMessage;

@Service(AdaptedMessageService.class)
public class AdaptedMessageServiceBean implements AdaptedMessageService {

	@Inject
	@Reference
	private RoutingService routingService;

	@Override
	public String submit(AdaptedMessage adaptedMessage) {

		AdaptedMessage response = this.routingService.submit(adaptedMessage);
		return response.getMessage();
	}

}
