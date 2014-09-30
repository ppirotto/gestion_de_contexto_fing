package edu.fing.switchyard.CEP_Engine;

import java.util.HashMap;

public interface DroolsFeederService {
	
	String receiveMessage(HashMap<String, String> input);

}
