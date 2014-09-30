package edu.fing.switchyard.CEP_Engine;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.xml.namespace.QName;

import org.switchyard.remote.RemoteInvoker;
import org.switchyard.remote.RemoteMessage;
import org.switchyard.remote.http.HttpInvoker;

public class CepUtils {
	private static final QName SERVICE = new QName(
			"urn:edu.fing.switchyard:CEP-Engine:1.0", "ESBServiceDrools");

	public static void notifyContextReasoner(HashMap<String, String> msg){
		String response = null;
		String port = System.getProperty(
				"org.switchyard.component.sca.client.port", "8080");
		RemoteInvoker invoker = new HttpInvoker("http://localhost:" + port + "/switchyard-remote");

//		HashMap<String, String> inputMessage = new HashMap<String, String>();
//		inputMessage.put("type", "USER_LOCATION");
//		inputMessage.put("body", originalMessage);
		
		// Create the request message
		RemoteMessage message = new RemoteMessage();
		message.setService(SERVICE).setOperation("receiveMessage")
				.setContent(msg);

		// Invoke the service
		RemoteMessage reply;
		try {
			reply = invoker.invoke(message);
			if (reply.isFault()) {
				System.err.println("Oops ... something bad happened.  "
						+ reply.getContent());
			} else {
				response = (String) reply.getContent();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		return response;
	}
}
