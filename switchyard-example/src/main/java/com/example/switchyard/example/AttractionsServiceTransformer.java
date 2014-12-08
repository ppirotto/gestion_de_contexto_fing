package com.example.switchyard.example;

import java.util.List;

import org.switchyard.annotations.Transformer;
import org.w3c.dom.Element;

public final class AttractionsServiceTransformer {

	@Transformer(to = "{urn:com.example.switchyard:switchyard-example:1.0}getAttractionsResponse")
	public String transformListToGetAttractionsResponse(List<String> from) {
		String res = "<getAttractionsResponse xmlns=\"urn:com.example.switchyard:switchyard-example:1.0\">";
		for (String elem : from) {
			boolean outside = false;
			if (elem.equals("Parque")) {
				outside = true;
			}
			res += "<attraction><place>" + elem + "</place><outside>" + outside + "</outside></attraction>";
		}
		res += "</getAttractionsResponse>";
		return res;
	}

	@Transformer(from = "{urn:com.example.switchyard:switchyard-example:1.0}getAttractions")
	public String transformGetAttractionsToString(Element from) {
		// TODO Auto-generated method stub
		return from.getElementsByTagName("city").item(0).getTextContent();
	}

}
