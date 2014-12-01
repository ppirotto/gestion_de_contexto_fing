package edu.fing.contenxt.management;

import java.io.IOException;
import java.util.HashMap;

import javax.xml.namespace.QName;

import org.switchyard.remote.RemoteInvoker;
import org.switchyard.remote.RemoteMessage;
import org.switchyard.remote.http.HttpInvoker;

public class RemoteInvokerUtils {
//	public static void probar(){
//		System.out.println("LLEGUEEEEEEEEEEEEEEEEEEEe");
//		
//	}
	private static final QName SERVICE = new QName(
			"urn:edu.fing.switchyard:CEP-Engine:1.0", "DroolsConfigService");

	public static void notifyContextReasoner(String msg){
		System.err.println("BBBBBBBBBBBBBBBBBBBB");
		String response = null;
		String port = System.getProperty(
				"org.switchyard.component.sca.client.port", "8080");
		RemoteInvoker invoker = new HttpInvoker("http://localhost:" + port + "/switchyard-remote");
		
		// Create the request message
		RemoteMessage message = new RemoteMessage();
		message.setService(SERVICE).setOperation("updateActiveVersion").setContent(msg);

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