package edu.fing.adaptation.manager;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import edu.fing.commons.dto.AdaptedMessage;

public class RoutingSlip extends RouteBuilder {

	public void configure() {
		
		from("switchyard://RoutingService").process(new Processor() {
			@Override
			public void process(Exchange exchange) throws Exception {
				AdaptedMessage adaptedMessage = exchange.getIn().getBody(AdaptedMessage.class);
				exchange.getIn().setBody(adaptedMessage.getMessage());
				exchange.getIn().setHeader("adaptationDirective", adaptedMessage.getAdaptations());
				exchange.getIn().setHeader("itinerary", adaptedMessage.getItinerary());
			}
		}).log("Received message for 'RoutingSlip' : ${body}").routingSlip().header("itinerary")
			.log("Finish routing");
	}
}
