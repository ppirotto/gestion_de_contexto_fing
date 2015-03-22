package edu.fing.context.reasoner.bean;

import java.util.List;

import edu.fing.commons.front.dto.ContextSourceTO;
import edu.fing.commons.front.dto.FrontResponseTO;
import edu.fing.commons.front.dto.ItineraryTO;
import edu.fing.commons.front.dto.ServiceTO;
import edu.fing.commons.front.dto.SituationTO;

public interface ConfigurationService {

	List<ServiceTO> getServicesWithSituationsAndAdaptations();

	List<ServiceTO> getServices();

	List<SituationTO> getSituations();

	List<String> getContextData();

	List<String> getContextDataBySituation(String situationName);

	List<String> getContextSources();

	FrontResponseTO createSituation(SituationTO situationTO);

	Boolean createService(ServiceTO serviceTO);

	FrontResponseTO createItinerary(ItineraryTO itineraryTO);

	Boolean createContextDatum(String contextDatumName);

	Boolean createContextSource(ContextSourceTO contextSourceTO);

}
