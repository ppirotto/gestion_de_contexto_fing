package edu.fing.adaptation.manager;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import edu.fing.commons.dto.AdaptedMessage;

public class ExternalInvocation extends RouteBuilder {

	/**
	 * The Camel route is configured via this method. The from endpoint is
	 * required to be a SwitchYard service.
	 */
	public void configure() {
		// TODO Auto-generated method stub
		from("switchyard://ExternalInvocationService").process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				AdaptedMessage adaptedMessage = exchange.getIn().getHeader("adaptedMessage", AdaptedMessage.class);
				exchange.getIn().setBody(adaptedMessage);

			}
		}).log("Received message for 'ExternalInvocationService' : ${body}").to("switchyard://ServiceInvoker").process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				AdaptedMessage adaptedMessage = exchange.getIn().getBody(AdaptedMessage.class);
				exchange.getIn().setHeader("adaptedMessage", adaptedMessage);
				exchange.getIn().setBody(adaptedMessage.getMessage());
			}
		});
	}

}
