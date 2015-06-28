package edu.fing.adaptation.manager;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import javax.xml.transform.Templates;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import edu.fing.commons.dto.AdaptationTO;
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
				@SuppressWarnings("unchecked")
				List<AdaptationTO> adaptationDirective = exchange.getIn().getHeader("adaptationDirective", List.class);
				String message = (String)exchange.getIn().getBody();
				
				String xslt = (String) adaptationDirective.get(0).getData();
				StringReader reader = new StringReader(message);
				StringWriter writer = new StringWriter();
				try {
					Templates templates = tFactory.newTemplates(new StreamSource(new StringReader(xslt)));
					javax.xml.transform.Transformer transformer = templates.newTransformer();
					transformer.transform(new StreamSource(reader), new StreamResult(writer));
					message = writer.toString();

				} catch (Exception e) {
					e.printStackTrace();
				}
				adaptationDirective.remove(0);
				exchange.getIn().setBody(message);
			}
		}).log("Enrich body : ${body}");
		;
	}

}
