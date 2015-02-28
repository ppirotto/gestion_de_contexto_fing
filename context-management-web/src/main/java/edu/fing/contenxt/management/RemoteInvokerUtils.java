package edu.fing.contenxt.management;

import javax.xml.namespace.QName;

import org.switchyard.remote.RemoteInvoker;
import org.switchyard.remote.RemoteMessage;
import org.switchyard.remote.http.HttpInvoker;

public class RemoteInvokerUtils {

	public static final QName DroolsConfigService = new QName("urn:edu.fing.switchyard:CEP-Engine:1.0", "DroolsConfigService");
	public static final QName ContextReasonerConfigService = new QName("urn:edu.fing.context.management:context-reasoner:1.0", "ConfigurationService");
	public static final QName AdaptationGatewayConfigService = new QName("urn:edu.fing.context.management:adaptation-gateway:1.0", "ItineraryService");

	public static Object invoke(QName service, String operationName, Object msg) {

		return invoke(service, operationName, msg, "localhost", "8080");
	}

	@SuppressWarnings("unchecked")
	public static Object invoke(QName service, String operationName, Object msg, String ip, String port) {

		Object response = null;
		// String port =
		// System.getProperty("org.switchyard.component.sca.client.port",
		// "8080");
		RemoteInvoker invoker = new HttpInvoker("http://" + ip + ":" + port + "/switchyard-remote");

		// Create the request message
		RemoteMessage message = new RemoteMessage();
		message.setService(service).setOperation(operationName).setContent(msg);

		// Invoke the service
		RemoteMessage reply;
		try {
			reply = invoker.invoke(message);

			if (reply.isFault()) {
				System.err.println("Oops ... something bad happened.  " + reply.getContent());
			} else {
				response = reply.getContent();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;

		// return response;
	}
}
