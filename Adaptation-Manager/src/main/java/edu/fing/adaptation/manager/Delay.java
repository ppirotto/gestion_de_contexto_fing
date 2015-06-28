package edu.fing.adaptation.manager;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import edu.fing.commons.dto.AdaptationTO;
import edu.fing.commons.dto.AdaptedMessage;

public class Delay extends RouteBuilder {

	/**
	 * The Camel route is configured via this method. The from endpoint is
	 * required to be a SwitchYard service.
	 */
	@Override
	public void configure() {

		from("switchyard://DelayService").process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				@SuppressWarnings("unchecked")
				List<AdaptationTO> adaptationDirective = exchange.getIn().getHeader("adaptationDirective", List.class);
				exchange.getIn().setHeader("delayTime", adaptationDirective.get(0).getData());
				adaptationDirective.remove(0);
			}
		}).log("Received message for 'DelayService' : ${body}").delay(this.header("delayTime")).log("Finish Delay");

	}

}
