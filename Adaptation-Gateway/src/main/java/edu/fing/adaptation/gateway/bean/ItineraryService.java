package edu.fing.adaptation.gateway.bean;

import java.util.List;

import edu.fing.commons.dto.ContextReasonerData;
import edu.fing.commons.front.dto.ConfiguredItineraryTO;

public interface ItineraryService {

	void receiveAdaptations(List<ContextReasonerData> contextReasonerData);

	List<ConfiguredItineraryTO> getItineraries();
}
