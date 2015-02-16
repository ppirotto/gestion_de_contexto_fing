package edu.fing.context.reasoner.bean;

import edu.fing.commons.front.dto.ServiceTO;
import edu.fing.commons.front.dto.SituationTO;

public interface ConfigurationService {

	Boolean createSituation(SituationTO situationTO);

	Boolean createService(ServiceTO serviceTO);

}
