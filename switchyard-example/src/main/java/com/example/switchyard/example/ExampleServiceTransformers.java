package com.example.switchyard.example;

import org.switchyard.annotations.Transformer;
import org.w3c.dom.Element;

public final class ExampleServiceTransformers {

	@Transformer(to = "{urn:com.example.switchyard:switchyard-example:1.0}sayHelloResponse")
	public String transformStringToSayHelloResponse(String from) {
		return "<sayHelloResponse xmlns=\"urn:com.example.switchyard:switchyard-example:1.0\"><string>" + from + "</string></sayHelloResponse>";
	}

	@Transformer(from = "{urn:com.example.switchyard:switchyard-example:1.0}sayHello")
	public String transformSayHelloToString(Element from) {
		return from.getTextContent();
	}

}
