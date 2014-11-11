package edu.fing.switchyard.Adaptation_Manager;

import javax.inject.Inject;

import org.switchyard.Context;
import org.switchyard.component.bean.Service;

import edu.fing.AdaptedMessage;

@Service(AdaptationManagerI.class)
public class AdaptationManagerIBean implements AdaptationManagerI {

	@Inject
	private Context context;

	@Override
	public String adaptedMessage(AdaptedMessage msg) {
		System.out.println("EN EL ADAPTER MANAGER");

		String itinerary = this.context.getPropertyValue("itinerary");
		System.out.println(itinerary);
		System.out.println(this.context);
		return "OK";
	}
}
