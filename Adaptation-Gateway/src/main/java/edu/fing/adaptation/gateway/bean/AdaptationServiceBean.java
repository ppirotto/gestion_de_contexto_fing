package edu.fing.adaptation.gateway.bean;

import java.util.ArrayList;

import javax.inject.Inject;

import org.switchyard.component.bean.Reference;
import org.switchyard.component.bean.Service;

import edu.fing.commons.dto.Adaptation;
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
		adaptedMessage.setItinerary("switchyard://DelayService,switchyard://ExternalInvocationService,switchyard://DelayService");
		adaptedMessage.setService("http://localhost:8080/attractions-provider/AttractionsService");
		ArrayList<Adaptation> adaptations = new ArrayList<Adaptation>();
		Adaptation adapt = new Adaptation();
		adapt.setData(10000);
		adapt.setName("Delay");
		adaptations.add(adapt);
		Adaptation adapt2 = new Adaptation();
		adapt2.setData(10000);
		adapt2.setName("Delay");
		adaptations.add(adapt2);
		adaptedMessage.setAdaptations(adaptations);

		return this.adaptationManager.submit(adaptedMessage);
	}
}
