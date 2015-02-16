package edu.fing.contenxt.management;

import javax.xml.namespace.QName;

import org.switchyard.remote.RemoteInvoker;
import org.switchyard.remote.RemoteMessage;
import org.switchyard.remote.http.HttpInvoker;

public class RemoteInvokerUtils {

	public static final QName DroolsConfigService = new QName("urn:edu.fing.switchyard:CEP-Engine:1.0", "DroolsConfigService");
	public static final QName ContextReasonerConfigService = new QName("urn:edu.fing.switchyard:context-reasoner:1.0", "ConfigurationService");

	public static <T> T invoke(QName service, String operationName, Object msg, Class<T> responseClass) {

		return invoke(service, operationName, msg, responseClass, "localhost", "8080");
	}

	@SuppressWarnings("unchecked")
	public static <T> T invoke(QName service, String operationName, Object msg, Class<T> responseClass, String ip, String port) {

		T response = null;
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
				response = (T) reply.getContent();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;

		// return response;
	}
}
