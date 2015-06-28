package edu.fing.test.cases;


import java.util.List;

import org.switchyard.annotations.Transformer;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public final class AttractionsServiceTransformer {

	@Transformer(to = "{urn:com.example.switchyard:switchyard-example:1.0}getAttractionsResponse")
	public String transformListToGetAttractionsResponse(List<AttractionDTO> from) {
		String res = "<getAttractionsResponse>";
		for (AttractionDTO attractionDTO : from) {
			res += "<attraction><place>" + attractionDTO.getAttraction() + "</place><description>" + attractionDTO.getDescription() +
					"</description><city>"+ attractionDTO.getCity() +"</city><outside>" + attractionDTO.isOutside() + 
					"</outside></attraction>";
		}
		
		
//		for (String elem : from) {
//			boolean outside = false;
//			if (elem.equals("Parque")) {
//				outside = true;
//			}
//			res += "<attraction><place>" + elem + "</place><outside>" + outside + "</outside></attraction>";
//		}
		res += "</getAttractionsResponse>";
		return res;
	}

	@Transformer(from = "{urn:com.example.switchyard:switchyard-example:1.0}getAttractions")
	public String transformGetAttractionsToString(Element from) {
		Node item = from.getElementsByTagName("city").item(0);
		String city = null;
		if(item!=null){
			city = item.getTextContent();
		}
		return city;
	}

}
