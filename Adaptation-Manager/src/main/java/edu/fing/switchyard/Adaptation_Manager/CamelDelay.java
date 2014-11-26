package edu.fing.switchyard.Adaptation_Manager;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import edu.fing.commons.AdaptedMessage;

public class CamelDelay extends RouteBuilder {

	/**
	 * The Camel route is configured via this method. The from endpoint is
	 * required to be a SwitchYard service.
	 */
	@Override
	public void configure() {
		// TODO Auto-generated method stub
		from("switchyard://DelayService").process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				AdaptedMessage adaptedMessage = exchange.getIn().getBody(AdaptedMessage.class);
				exchange.getIn().setHeader("delayTime", adaptedMessage.getAdaptations().get(0).getData());
				adaptedMessage.getAdaptations().remove(0);
				System.out.println("message: " + adaptedMessage.getMessage());
				System.out.println("cantidad de adaptations: " + adaptedMessage.getAdaptations().size());

				System.out.println("ESTAMOS EN EL DELAY");
			}
		}).log("Received message for 'DelayService' : ${body}").delay(header("delayTime")).log("volvi de dormirme");
	}

}
