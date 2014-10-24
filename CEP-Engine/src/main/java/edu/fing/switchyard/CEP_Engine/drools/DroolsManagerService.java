package edu.fing.switchyard.CEP_Engine.drools;

import java.util.HashMap;

public interface DroolsManagerService {

	String insert(HashMap<String, String> inputMessage);

	void addRule();

}
