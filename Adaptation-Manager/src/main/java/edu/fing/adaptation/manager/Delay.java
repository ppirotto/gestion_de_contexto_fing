package edu.fing.adaptation.manager;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import edu.fing.commons.dto.AdaptedMessage;

public class Delay extends RouteBuilder {

	/**
	 * The Camel route is configured via this method. The from endpoint is
	 * required to be a SwitchYard service.
	 */
	public void configure() {

		// TODO Auto-generated method stub
		from("switchyard://DelayService").process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				AdaptedMessage adaptedMessage = exchange.getIn().getHeader("adaptedMessage", AdaptedMessage.class);
				exchange.getIn().setHeader("delayTime", adaptedMessage.getAdaptations().get(0).getData());
				adaptedMessage.getAdaptations().remove(0);
			}
		}).log("Received message for 'DelayService' : ${body}").delay(header("delayTime")).log("Finish Delay");

	}

}
