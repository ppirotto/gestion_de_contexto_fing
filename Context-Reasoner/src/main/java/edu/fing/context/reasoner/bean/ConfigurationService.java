package edu.fing.context.reasoner.bean;

import java.util.List;

import edu.fing.commons.front.dto.ItineraryTO;
import edu.fing.commons.front.dto.ServiceTO;
import edu.fing.commons.front.dto.SituationTO;

public interface ConfigurationService {

	List<ServiceTO> getServicesWithSituationsAndAdaptations();

	List<ServiceTO> getServices();

	List<SituationTO> getSituations();

	Boolean createSituation(SituationTO situationTO);

	Boolean createService(ServiceTO serviceTO);

	Boolean createItinerary(ItineraryTO itineraryTO);

}
