package edu.fing.switchyard.Adaptation_Gateway.bean;

import java.util.List;

import edu.fing.ContextReasonerData;

public interface ItineraryService {

	void receiveAdaptations(List<ContextReasonerData> contextReasonerData);
}
