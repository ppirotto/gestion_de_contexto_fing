package edu.fing.switchyard.Adaptation_Manager;

import java.util.HashMap;

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
				HashMap<String, String> hashMap = new HashMap<String, String>();
				hashMap.put("itinerary", "switchyard://PruebaRouting?operationName=hola");
				exchange.getContext().setProperties(hashMap);
				AdaptedMessage adaptedMessage = exchange.getIn().getBody(AdaptedMessage.class);
				System.out.println(adaptedMessage.getMessage());
				// Set<Property> properties =
				// RoutingSlipImpl.this.context.getProperties();
				System.out.println("ESTAMOS EN EL ROUTING NOMAA");
			}
		}).log("Received message for 'RoutingSlip' : ${headers} ${body}").routingSlip().header("itinerary").log("PASO EL ROUT");
	}
}
