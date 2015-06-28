package edu.fing.cep.engine.utils;

import java.io.IOException;
import java.util.HashMap;

import javax.xml.namespace.QName;

import org.switchyard.remote.RemoteInvoker;
import org.switchyard.remote.RemoteMessage;
import org.switchyard.remote.http.HttpInvoker;

import edu.fing.commons.dto.SituationDetectedTO;

public class CepUtils {

	private static final QName SERVICE = new QName(
			"urn:edu.fing.context.management:context-reasoner:1.0", "SituationReceiver");

	public static void notifyContextReasoner(SituationDetectedTO situation){
		
		String response = null;
		String port = System.getProperty(
				"org.switchyard.component.sca.client.port", "8080");
		RemoteInvoker invoker = new HttpInvoker("http://localhost:" + port + "/switchyard-remote");
		
		// Create the request message
		RemoteMessage message = new RemoteMessage();
		message.setService(SERVICE).setOperation("receiveSituationFromCEP").setContent(situation);

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
