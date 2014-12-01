package edu.fing.adaptation.manager;

import org.apache.camel.builder.RouteBuilder;

public class Delay extends RouteBuilder {

	/**
	 * The Camel route is configured via this method.  The from endpoint is required to be a SwitchYard service.
	 */
	public void configure() {
		// TODO Auto-generated method stub
		from("switchyard://DelayService").log("Received message for 'DelayService' : ${body}");
	}

}
