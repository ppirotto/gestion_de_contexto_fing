package edu.fing.adaptation.manager;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import edu.fing.commons.dto.AdaptationTO;
import edu.fing.commons.dto.AdaptedMessage;

public class ExternalInvocation extends RouteBuilder {

	/**
	 * The Camel route is configured via this method. The from endpoint is
	 * required to be a SwitchYard service.
	 */
	public void configure() {

		from("switchyard://ExternalInvocationService").process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				@SuppressWarnings("unchecked")
				List<AdaptationTO> adaptationDirective = exchange.getIn().getHeader("adaptationDirective", List.class);
				String message = (String) exchange.getIn().getBody();
				
				AdaptedMessage adaptedMessage = new AdaptedMessage();
				adaptedMessage.setMessage(message);
				adaptedMessage.setAdaptations(adaptationDirective);
				exchange.getIn().setBody(adaptedMessage);

			}
		}).log("Received message for 'ExternalInvocationService' : ${body}").to("switchyard://ServiceInvoker").process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				AdaptedMessage adaptedMessage = exchange.getIn().getBody(AdaptedMessage.class);
				exchange.getIn().setHeader("adaptationDirective", adaptedMessage.getAdaptations());
				exchange.getIn().setBody(adaptedMessage.getMessage());
			}
		});
	}

}
