package edu.fing.adaptation.gateway.bean;

import java.util.ArrayList;

import javax.inject.Inject;

import org.switchyard.component.bean.Reference;
import org.switchyard.component.bean.Service;

import edu.fing.commons.dto.AdaptationTO;
import edu.fing.commons.dto.AdaptedMessage;

@Service(AdaptationService.class)
public class AdaptationServiceBean implements AdaptationService {

	@Inject
	@Reference
	private AdaptationManager adaptationManager;

	@Override
	public String setItinerary(String message) {

		// Ir a buscar a la base el itinerario dado el servicio y el user
		AdaptedMessage adaptedMessage = new AdaptedMessage();
		adaptedMessage.setMessage(message);
		adaptedMessage.setItinerary("switchyard://DelayService,switchyard://ExternalInvocationService,switchyard://FilterService");
		adaptedMessage.setService("http://localhost:8080/attractions-provider/AttractionsService");
		ArrayList<AdaptationTO> adaptations = new ArrayList<AdaptationTO>();
		AdaptationTO adapt = new AdaptationTO();
		adapt.setData("5000");
		adapt.setName("delay");
		adaptations.add(adapt);
		AdaptationTO adapt2 = new AdaptationTO();
		adapt2.setData("/getAttractionsResponse/attraction[outside='false']");
		adapt2.setName("filter");
		adaptations.add(adapt2);
		// Adaptation adapt3 = new Adaptation();
		// adapt3.setData("2000");
		// adapt3.setName("delay");
		// adaptations.add(adapt3);
		adaptedMessage.setAdaptations(adaptations);

		return this.adaptationManager.submit(adaptedMessage);
	}
}
