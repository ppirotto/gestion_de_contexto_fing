package edu.fing.context.reasoner.util;

import javax.xml.namespace.QName;

import org.switchyard.remote.RemoteInvoker;
import org.switchyard.remote.RemoteMessage;
import org.switchyard.remote.http.HttpInvoker;

public class RemoteInvokerUtils {
	public enum ServiceIp {
		CepEngineIP("localhost"); 

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

	public static final QName DroolsManagerService = new QName("urn:edu.fing.context.management:cep-engine:1.0", "DroolsManagerService");

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
