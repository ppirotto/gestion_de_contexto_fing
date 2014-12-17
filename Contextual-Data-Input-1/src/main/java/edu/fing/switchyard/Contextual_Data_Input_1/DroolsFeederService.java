package edu.fing.switchyard.Contextual_Data_Input_1;
import edu.fing.commons.dto.ContextualDataTO;

public interface DroolsFeederService {
	
	String receiveMessage(ContextualDataTO data);

}