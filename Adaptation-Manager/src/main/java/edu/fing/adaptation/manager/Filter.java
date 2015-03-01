package edu.fing.adaptation.manager;

import java.io.StringReader;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.xml.sax.InputSource;

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
				// exchange.getIn().setHeader("filter",
				// adaptedMessage.getAdaptations().get(0).getData());
				XPathFactory xpathFactory = XPathFactory.newInstance();
				XPath xpath = xpathFactory.newXPath();
				XPathExpression expr = xpath.compile((String) adaptedMessage.getAdaptations().get(0).getData());
				String message = (String) expr.evaluate(new InputSource(new StringReader(adaptedMessage.getMessage())), XPathConstants.STRING);
				adaptedMessage.setMessage(message);
				adaptedMessage.getAdaptations().remove(0);
			}
		}).log("Filter body :${header[filter]} ${body}");
	}
}
