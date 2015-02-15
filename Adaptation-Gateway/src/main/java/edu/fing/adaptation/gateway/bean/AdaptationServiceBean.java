package edu.fing.adaptation.gateway.bean;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.xml.parsers.ParserConfigurationException;

import org.switchyard.component.bean.Reference;
import org.switchyard.component.bean.Service;
import org.xml.sax.SAXException;

import edu.fing.commons.dto.AdaptationTO;
import edu.fing.commons.dto.AdaptedMessage;

@Service(AdaptationService.class)
public class AdaptationServiceBean implements AdaptationService {

	@Inject
	@Reference
	private AdaptationManager adaptationManager;

	@Override
	public String setItinerary(String message) {

		// Ir a buscar a la base el itinerario dado el servicio y el user
		AdaptedMessage adaptedMessage = new AdaptedMessage();
		adaptedMessage.setMessage(message);
		adaptedMessage.setItinerary("switchyard://DelayService,switchyard://ExternalInvocationService,switchyard://FilterService");
		adaptedMessage.setService("http://localhost:8080/attractions-provider/AttractionsService");
		ArrayList<AdaptationTO> adaptations = new ArrayList<AdaptationTO>();
		AdaptationTO adapt = new AdaptationTO();
		adapt.setData("5000");
		adapt.setName("delay");
		adaptations.add(adapt);
		AdaptationTO adapt2 = new AdaptationTO();
		adapt2.setData("/getAttractionsResponse/attraction[outside='false']");
		adapt2.setName("filter");
		adaptations.add(adapt2);
		// Adaptation adapt3 = new Adaptation();
		// adapt3.setData("2000");
		// adapt3.setName("delay");
		// adaptations.add(adapt3);
		adaptedMessage.setAdaptations(adaptations);

		Map<String, Object> root = new HashMap<String, Object>();
		try {
			root.put("doc", freemarker.ext.dom.NodeModel.parse(new File("/getAttractions.xml")));
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return this.adaptationManager.submit(adaptedMessage);
	}
}
