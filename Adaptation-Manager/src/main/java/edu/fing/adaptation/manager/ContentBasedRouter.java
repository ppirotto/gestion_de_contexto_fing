package edu.fing.adaptation.manager;

import java.io.StringReader;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.codehaus.jackson.map.ObjectMapper;
import org.xml.sax.InputSource;

import edu.fing.commons.dto.AdaptationTO;
import edu.fing.commons.dto.AdaptedMessage;
import edu.fing.commons.front.dto.AdaptationTreeNodeTO;

public class ContentBasedRouter extends RouteBuilder {

	/**
	 * The Camel route is configured via this method. The from endpoint is
	 * required to be a SwitchYard service.
	 */
	@Override
	public void configure() {
		this.from("switchyard://ContentBasedRouterService").log("Received message for 'ContentBasedRouterService' : ${body}")
				.process(new Processor() {

					@Override
					public void process(Exchange exchange) throws Exception {
						@SuppressWarnings("unchecked")
						List<AdaptationTO> adaptationDirective = exchange.getIn().getHeader("adaptationDirective", List.class);
						String message = (String)exchange.getIn().getBody();
						
						XPathFactory xpathFactory = XPathFactory.newInstance();
						XPath xpath = xpathFactory.newXPath();
						
						ObjectMapper objectMapper = new ObjectMapper();
						AdaptationTreeNodeTO adaptationTreeNode = null;
						try {
							adaptationTreeNode = objectMapper.readValue((String) adaptationDirective.get(0).getData(),
									AdaptationTreeNodeTO.class);
						} catch (Exception e) {
							e.printStackTrace();
						}
						AdaptationTO adaptationTO = this.preOrderSearch(adaptationTreeNode, xpath, message);
						adaptationDirective.remove(0);
						if (adaptationTO != null) {
							adaptationDirective.add(0, adaptationTO);
						}
						exchange.getIn().setHeader("uri", adaptationTO.getAdaptationType().getUri());
					}

					private AdaptationTO preOrderSearch(AdaptationTreeNodeTO node, XPath xpath, String message) {
						if (node == null)
							return null;

						System.out.println(node.getXpath());

						if (node.getXpath() != null) {
							Boolean result = false;
							try {
								XPathExpression expr = xpath.compile(node.getXpath());
								result = (Boolean) expr.evaluate(new InputSource(new StringReader(message)), XPathConstants.BOOLEAN);
							} catch (XPathExpressionException e) {
								e.printStackTrace();
							}
							if (result) {
								return this.preOrderSearch(node.getLeftNode(), xpath, message);
							} else {
								return this.preOrderSearch(node.getRightNode(), xpath, message);
							}
						} else {
							return node.getAdaptation();
						}
					}

				}).log("ContentBasedRouter body: ${body}").recipientList(header("uri"));
	}

}
