package edu.fing.context.reasoner.bean;

import java.util.HashMap;

import edu.fing.commons.dto.SituationDetectedTO;

public interface SituationReceiver {

	String receiveSituationFromCEP(SituationDetectedTO situation);

	String receiveSituationFromCEP2();
}
