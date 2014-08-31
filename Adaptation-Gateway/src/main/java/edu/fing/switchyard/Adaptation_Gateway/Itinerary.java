package edu.fing.switchyard.Adaptation_Gateway;

public class Itinerary {

	public String setItinerary(){
		String itinerary = "switchyard://DelayService,switchyard://AppendService,switchyard://RemoteInvokerService";
		return itinerary;
	}

}
