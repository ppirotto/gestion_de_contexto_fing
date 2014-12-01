package edu.fing.adaptation.manager;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import edu.fing.commons.dto.AdaptedMessage;

public class RoutingSlip extends RouteBuilder {

	/**
	 * The Camel route is configured via this method. The from endpoint is
	 * required to be a SwitchYard service.
	 */
	public void configure() {

		from("switchyard://RoutingService").process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {

				AdaptedMessage adaptedMessage = exchange.getIn().getBody(AdaptedMessage.class);
				exchange.getIn().setHeader("itinerary", adaptedMessage.getItinerary());

				System.out.println(adaptedMessage.getMessage());
				System.out.println("ESTAMOS EN EL ROUTING NOMAA");
			}
		}).log("Received message for 'RoutingSlip' : ${headers} ${body}").routingSlip().header("itinerary").log("PASO EL ROUT");
	}

}
