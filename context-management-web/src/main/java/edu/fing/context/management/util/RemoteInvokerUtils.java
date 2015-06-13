package edu.fing.context.management.util;

import javax.xml.namespace.QName;

import org.switchyard.remote.RemoteInvoker;
import org.switchyard.remote.RemoteMessage;
import org.switchyard.remote.http.HttpInvoker;

public class RemoteInvokerUtils {
	public enum ServiceIp {
		ContextReasonerIp("localhost"), AdaptationGatewayIp("192.168.0.103");

		private String ip;

		ServiceIp(String ip) {
			this.ip = ip;
		}

		public String getIp() {
			return this.ip;
		}

		public void setIp(String ip) {
			this.ip = ip;
		}
	}

	public static final QName ContextReasonerCEPService = new QName(
			"urn:edu.fing.context.management:context-reasoner:1.0", "CEPService");
	public static final QName ContextReasonerConfigService = new QName(
			"urn:edu.fing.context.management:context-reasoner:1.0", "ConfigurationService");
	public static final QName AdaptationGatewayConfigService = new QName(
			"urn:edu.fing.context.management:adaptation-gateway:1.0", "ItineraryService");

	public static Object invoke(QName service, String operationName, Object msg, ServiceIp serviceIp) {

		Object response = null;
		RemoteInvoker invoker = new HttpInvoker("http://" + serviceIp.getIp() + ":" + 8080 + "/switchyard-remote");

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
			e.printStackTrace();
		}
		return response;
	}
}
