package edu.fing.adaptation.manager;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import edu.fing.commons.dto.AdaptedMessage;

public class Filter extends RouteBuilder {

	/**
	 * The Camel route is configured via this method. The from endpoint is
	 * required to be a SwitchYard service.
	 */
	public void configure() {
		from("switchyard://FilterService").log("Received message for 'FilterService' : ${body}").process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				AdaptedMessage adaptedMessage = exchange.getIn().getHeader("adaptedMessage", AdaptedMessage.class);
				exchange.getIn().setHeader("filter", adaptedMessage.getAdaptations().get(0).getData());
				adaptedMessage.getAdaptations().remove(0);
			}
		}).log("Filter body :${header[filter]} ${body}");
	}
}
