package edu.fing.switchyard.Service_Invoker;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.inject.Inject;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.switchyard.Context;
import org.switchyard.component.bean.Service;

@Service(InvokerService.class)
public class InvokerServiceBean implements InvokerService {
	
	@Inject
	private Context context;

	private static final TransformerFactory tFactory = TransformerFactory.newInstance();
	
	public String invokeRealService(String message) {
		
		String serviceUrl = context.getPropertyValue("service");
		String response = null;
		
		String request = this.addEnvelopeToRequest(message);		
		
		if (message != null){
			response = this.postSoapMessage(request,serviceUrl);
			response = this.removeEnvelopeFromResponse(response);
		}
		
		return response;
	}
	
	private String addEnvelopeToRequest(String request){
		
		StringReader reader = new StringReader(request);
		StringWriter writer = new StringWriter();
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("xslt/Transformer.xsl");

		try {
			Templates templates = tFactory.newTemplates(new StreamSource(inputStream));
			javax.xml.transform.Transformer transformer = templates.newTransformer();
			transformer.transform(new StreamSource(reader),	new StreamResult(writer));
			return writer.toString();
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private String postSoapMessage(String message, String serviceUrl){
		try {
			URL obj = new URL(serviceUrl);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	
			// add request header
			con.setRequestMethod("POST");
			// con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			
			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(message);
			wr.flush();
			wr.close();
					
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			return response.toString();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private String removeEnvelopeFromResponse(String response){
		try{
			StringReader reader = new StringReader(response);
			StringWriter writer = new StringWriter();
	
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream("xslt/Remover.xsl");
			Templates templates = tFactory.newTemplates(new StreamSource(inputStream));
			javax.xml.transform.Transformer transformer = templates.newTransformer();
			transformer.transform(new StreamSource(reader),new StreamResult(writer));
			return writer.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
