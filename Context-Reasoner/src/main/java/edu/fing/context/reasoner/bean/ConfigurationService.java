package edu.fing.context.reasoner.bean;

import java.util.List;

import edu.fing.commons.front.dto.ItineraryTO;
import edu.fing.commons.front.dto.ServiceTO;
import edu.fing.commons.front.dto.SituationTO;

public interface ConfigurationService {

	Boolean createSituation(SituationTO situationTO);

	Boolean createService(ServiceTO serviceTO);

	List<ServiceTO> getServices();

	List<SituationTO> getSituations();

	Boolean createItinerary(ItineraryTO itineraryTO);

}
