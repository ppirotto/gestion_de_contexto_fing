package edu.fing.context.reasoner.bean;

import edu.fing.commons.dto.SituationDetectedTO;


public interface SituationReceiver {

	String receiveSituationFromCEP(SituationDetectedTO situation);

}
