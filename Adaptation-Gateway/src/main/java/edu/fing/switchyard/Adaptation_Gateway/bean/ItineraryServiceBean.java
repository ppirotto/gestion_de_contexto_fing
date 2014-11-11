package edu.fing.switchyard.Adaptation_Gateway.bean;

import java.io.IOException;

import javax.inject.Inject;
import javax.xml.namespace.QName;

import org.switchyard.Context;
import org.switchyard.Scope;
import org.switchyard.component.bean.Service;
import org.switchyard.internal.CompositeContext;
import org.switchyard.internal.DefaultContext;
import org.switchyard.remote.RemoteInvoker;
import org.switchyard.remote.RemoteMessage;
import org.switchyard.remote.http.HttpInvoker;

import edu.fing.ItineraryService;
import edu.fing.commons.AdaptedMessage;

@Service(ItineraryService.class)
public class ItineraryServiceBean implements ItineraryService {

	private static final QName SERVICE = new QName("urn:edu.fing.switchyard:Adaptation-Manager:1.0", "RoutingServiceInterface");

	@Inject
	private Context context;

	@Override
	public void receiveAdaptations(String mess) {
		// for (ContextReasonerData data : contextReasonerData) {
		// System.out.println("data recibida del context reasoner: user:" +
		// data.getUser() + " service:" + data.getService());
		// }

		// this.context.setProperty("itinerary",
		// "switchyard://PruebaRouting?operationName=hola");
		String itinerary = this.context.getPropertyValue("itinerary");
		System.out.println(itinerary);
		System.out.println("en el gatewaaaaay");

		AdaptedMessage adaptedMessage = new AdaptedMessage();
		adaptedMessage.setMessage("hola HIJO DE MIL PUTAS");

		// this.managerService.acceptMessage(adaptedMessage);

		// // this.managerService.submit(adaptedMessage);
		//
		RemoteInvoker invoker = new HttpInvoker("http://localhost:8080/switchyard-remote");

		RemoteMessage message = new RemoteMessage();

		CompositeContext contextE = (CompositeContext) message.getContext();
		DefaultContext defaultContext = new DefaultContext();
		// new CamelCompositeContext(exchange);
		defaultContext.mergeInto(this.context);// setProperty("itinerary",
												// itinerary);
		contextE.setContext(Scope.MESSAGE, defaultContext);
		contextE.setContext(Scope.EXCHANGE, defaultContext);

		// message.setService(SERVICE).setOperation("adaptedMessage").setContent(adaptedMessage);
		message.setService(SERVICE).setOperation("submit").setContent(adaptedMessage);

		// CompositeContext context2 = (CompositeContext) message.getContext();
		// context2.setProperty("itinerary", itinerary);
		// context2.setContext(Scope.EXCHANGE, this.context);
		// context2.setContext(Scope.MESSAGE, this.context);
		// String itinerary2 = context2.getPropertyValue("itinerary");
		// System.out.println(itinerary2);
		// // Invoke the service
		RemoteMessage reply;
		try {
			reply = invoker.invoke(message);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
