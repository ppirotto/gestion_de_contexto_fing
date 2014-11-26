package edu.fing.switchyard.Adaptation_Gateway.bean;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.xml.namespace.QName;

import org.switchyard.Context;
import org.switchyard.component.bean.Reference;
import org.switchyard.component.bean.Service;

import edu.fing.commons.Adaptation;
import edu.fing.commons.AdaptedMessage;
import edu.fing.switchyard.Adaptation_Gateway.InterfaceManager;

@Service(ItineraryServiceI.class)
public class ItineraryServiceBean implements ItineraryServiceI {

	private static final QName SERVICE = new QName("urn:edu.fing.switchyard:Adaptation-Manager:1.0", "RoutingServiceInterface");

	@Inject
	private Context context;

	@Inject
	@Reference
	private InterfaceManager interfaceManager;

	@Override
	public void receiveAdaptations(String mess) {

		// System.out.println("data recibida del context reasoner: user:" +
		// data.getUser() + " service:" + data.getService());
		// }

		// this.context.setProperty("itinerary",
		// "switchyard://PruebaRouting?operationName=hola");
		String itinerary = this.context.getPropertyValue("itinerary");
		System.out.println(itinerary);
		System.out.println("en el gatewaaaaay");

		AdaptedMessage adaptedMessage = new AdaptedMessage();
		adaptedMessage.setMessage(mess);
		adaptedMessage.setHeader("switchyard://DelayService,switchyard://DelayService");
		ArrayList<Adaptation> adaptations = new ArrayList<Adaptation>();
		Adaptation adapt = new Adaptation();
		adapt.setData(10000);
		adapt.setName("Delay");
		adaptations.add(adapt);
		Adaptation adapt2 = new Adaptation();
		adapt2.setData(10000);
		adapt2.setName("Delay");
		adaptations.add(adapt2);
		adaptedMessage.setAdaptations(adaptations);

		this.interfaceManager.submit(adaptedMessage);

		// this.managerService.acceptMessage(adaptedMessage);

		// // this.managerService.submit(adaptedMessage);
		//
		// RemoteInvoker invoker = new
		// HttpInvoker("http://localhost:8080/switchyard-remote");
		//
		// RemoteMessage message = new RemoteMessage();
		//
		// CompositeContext contextE = (CompositeContext) message.getContext();
		// DefaultContext defaultContext = new DefaultContext();
		// // new CamelCompositeContext(exchange);
		// defaultContext.mergeInto(this.context);// setProperty("itinerary",
		// // itinerary);
		// contextE.setContext(Scope.MESSAGE, defaultContext);
		// contextE.setContext(Scope.EXCHANGE, defaultContext);
		//
		// //
		// message.setService(SERVICE).setOperation("adaptedMessage").setContent(adaptedMessage);
		// message.setService(SERVICE).setOperation("submit").setContent(adaptedMessage);
		//
		// // CompositeContext context2 = (CompositeContext)
		// message.getContext();
		// // context2.setProperty("itinerary", itinerary);
		// // context2.setContext(Scope.EXCHANGE, this.context);
		// // context2.setContext(Scope.MESSAGE, this.context);
		// // String itinerary2 = context2.getPropertyValue("itinerary");
		// // System.out.println(itinerary2);
		// // // Invoke the service
		// RemoteMessage reply;
		// try {
		// reply = invoker.invoke(message);
		//
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}
}
