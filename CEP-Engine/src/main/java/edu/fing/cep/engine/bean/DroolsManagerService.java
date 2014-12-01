package edu.fing.cep.engine.bean;

import java.util.HashMap;

import edu.fing.commons.front.dto.AvailableRulesTO;

public interface DroolsManagerService {

	String insert(HashMap<String, String> inputMessage);

	void deployVersion(String versionNumber);

	void updateActiveVersion(String versionNumber);

	AvailableRulesTO getAvailableRules();
}
