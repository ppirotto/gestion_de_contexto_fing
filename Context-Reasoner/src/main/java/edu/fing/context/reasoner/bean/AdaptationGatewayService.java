package edu.fing.context.reasoner.bean;

import java.util.List;

import edu.fing.commons.dto.ContextReasonerData;

public interface AdaptationGatewayService {

	void receiveAdaptations(List<ContextReasonerData> contextReasonerData);
}
