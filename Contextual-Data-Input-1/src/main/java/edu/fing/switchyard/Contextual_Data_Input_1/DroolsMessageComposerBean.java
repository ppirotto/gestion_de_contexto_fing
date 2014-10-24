package edu.fing.switchyard.Contextual_Data_Input_1;

import java.io.IOException;
import java.util.HashMap;

import javax.xml.namespace.QName;

import org.switchyard.component.bean.Service;
import org.switchyard.remote.RemoteInvoker;
import org.switchyard.remote.RemoteMessage;
import org.switchyard.remote.http.HttpInvoker;



@Service(DroolsMessageComposer.class)
public class DroolsMessageComposerBean implements DroolsMessageComposer {

	private static final QName SERVICE = new QName(
			"urn:edu.fing.switchyard:CEP-Engine:1.0", "ESBServiceDrools");

	@Override
	public String send(String originalMessage) {
		String response = null;
		String port = System.getProperty(
				"org.switchyard.component.sca.client.port", "8080");
		RemoteInvoker invoker = new HttpInvoker("http://localhost:" + port + "/switchyard-remote");

		HashMap<String, String> inputMessage = new HashMap<String, String>();
		inputMessage.put("type", "USER_LOCATION");
		inputMessage.put("body", originalMessage);
		
		// Create the request message
		RemoteMessage message = new RemoteMessage();
		message.setService(SERVICE).setOperation("receiveMessage")
				.setContent(inputMessage);

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

		return response;
	}

}
