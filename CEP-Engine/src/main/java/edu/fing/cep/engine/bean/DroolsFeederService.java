package edu.fing.cep.engine.bean;

import java.util.HashMap;

public interface DroolsFeederService {
	
	String receiveMessage(HashMap<String, String> input);

}
