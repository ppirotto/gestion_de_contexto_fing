package edu.fing.switchyard.Adaptation_Gateway.bean;

import java.util.List;

import org.switchyard.component.bean.Service;

import edu.fing.ContextReasonerData;

@Service(ItineraryService.class)
public class ItineraryServiceBean implements ItineraryService {

	@Override
	public void receiveAdaptations(List<ContextReasonerData> contextReasonerData) {
		for (ContextReasonerData data : contextReasonerData) {
			System.out.println("data recibida del context reasoner: user:"+data.getUser() +" service:" + data.getService());
		}
		
		
	}

}
