package edu.fing.adaptation.gateway.bean;

import java.util.List;

import edu.fing.commons.dto.ContextReasonerData;

public interface ItineraryService {

	void receiveAdaptations(List<ContextReasonerData> contextReasonerData);
}
