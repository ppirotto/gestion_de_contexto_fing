package edu.fing.switchyard.Adaptation_Manager;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import edu.fing.commons.AdaptedMessage;

public class RoutingSlipImpl extends RouteBuilder {

	/**
	 * The Camel route is configured via this method. The from endpoint is
	 * required to be a SwitchYard service.
	 */
	@Override
	public void configure() {

		// TODO Auto-generated method stub
		from("switchyard://RoutingServiceInterface").process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				AdaptedMessage adaptedMessage = exchange.getIn().getBody(AdaptedMessage.class);
				exchange.getIn().setHeader("itinerary", adaptedMessage.getHeader());
				System.out.println(adaptedMessage.getMessage());

				System.out.println("ESTAMOS EN EL ROUTING NOMAA");
			}
		}).log("Received message for 'RoutingSlip' : ${headers} ${body}").routingSlip().header("itinerary").log("PASO EL ROUT");
	}
}
