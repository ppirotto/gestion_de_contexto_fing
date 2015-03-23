package edu.fing.adaptation.manager;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.Templates;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import edu.fing.commons.dto.AdaptedMessage;

public class Enrich extends RouteBuilder {

	private static final TransformerFactory tFactory = TransformerFactory.newInstance();

	/**
	 * The Camel route is configured via this method. The from endpoint is
	 * required to be a SwitchYard service.
	 */
	@Override
	public void configure() {
		from("switchyard://EnrichService").log("Received message for 'EnrichService' : ${body}").process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				AdaptedMessage adaptedMessage = exchange.getIn().getHeader("adaptedMessage", AdaptedMessage.class);
				String xslt = (String) adaptedMessage.getAdaptations().get(0).getData();
				StringReader reader = new StringReader(adaptedMessage.getMessage());
				StringWriter writer = new StringWriter();
				try {
					Templates templates = tFactory.newTemplates(new StreamSource(new StringReader(xslt)));
					javax.xml.transform.Transformer transformer = templates.newTransformer();
					transformer.transform(new StreamSource(reader), new StreamResult(writer));
					adaptedMessage.setMessage(writer.toString());

				} catch (Exception e) {
					e.printStackTrace();
				}
				adaptedMessage.getAdaptations().remove(0);
				exchange.getIn().setHeader("adaptedMessage", adaptedMessage);
				exchange.getIn().setBody(adaptedMessage.getMessage());
			}
		}).log("Enrich body : ${body}");
		;
	}

}
