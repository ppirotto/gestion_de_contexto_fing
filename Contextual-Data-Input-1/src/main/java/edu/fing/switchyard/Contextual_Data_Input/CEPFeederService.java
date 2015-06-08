package edu.fing.switchyard.Contextual_Data_Input;
import edu.fing.commons.dto.ContextualDataTO;

public interface CEPFeederService {
	
	String receiveMessage(ContextualDataTO data);

}