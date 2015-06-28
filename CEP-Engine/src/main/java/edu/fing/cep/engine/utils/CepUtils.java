package edu.fing.cep.engine.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.List;




import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.switchyard.remote.RemoteInvoker;
import org.switchyard.remote.RemoteMessage;
import org.switchyard.remote.http.HttpInvoker;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;

import edu.fing.commons.dto.ContextualDataTO;
import edu.fing.commons.dto.SituationDetectedTO;

public class CepUtils {

	private static final QName SERVICE = new QName(
			"urn:edu.fing.context.management:context-reasoner:1.0", "SituationReceiver");
	private static final String ORIGINAL = "ÁáÉéÍíÓóÚúÑñÜü";
	private static final String REPLACEMENT = "AaEeIiOoUuNnUu";
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
	public static String httpGet(String url){
		try {

			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");

			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			System.out.println(response.toString());
			return response.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Map<String, Object> jsonToMap(String json) {
		ObjectMapper objectMapper = new ObjectMapper();
		@SuppressWarnings("unchecked")
		Map<String, Object> objectAsMap = null;
		try {
			objectAsMap = objectMapper.readValue(json, Map.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objectAsMap;
	}
	
	
	public static void prueba(ContextualDataTO message){
		System.out.println("Checking if the user is in a valid city...");
		String response = CepUtils.httpGet(
          "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + "userPosition.getLat()" + "," + "userPosition.getLon()" + "&result_type=administrative_area_level_1&key=AIzaSyDBV2XLwr5AmhhymFzXQWq_HKXPOV7MQ2A");
		Map<String,Object> responseMap = CepUtils.jsonToMap(response);
//		System.out.println("DESDE CEP: " + mapa.get("results"));
		Map place = (Map) ((List)responseMap.get("results")).get(0);
		String[] departmentArray = ((String)place.get("formatted_address")).split(" ");
		String department = "";
		for (String elem : departmentArray) {
			if (!elem.toLowerCase().equals("department")){
				department+=" "+elem;
			} else {
				break;
			}
		}
		department = CepUtils.stripAccents(department);
		System.out.println("DEPARTAMENTO: '" + department + "'");
		HashMap<String,Object> contextualData = new HashMap<String,Object>();
		contextualData.put("city",department);
//		situationDetected(, "InCity",contextualData);
	}
	
	public static String stripAccents(String str) {
	    if (str == null)
	    	return null;
	    char[] array = str.toCharArray();
	    for (int index = 0; index < array.length; index++) {
	        int pos = ORIGINAL.indexOf(array[index]);
	        if (pos > -1) {
	            array[index] = REPLACEMENT.charAt(pos);
	        }
	    }
	    return new String(array);
	}
	
	public static Map<String, Object> xmlToMap(String xml) {
		XStream xStream = new XStream(new DomDriver());
		xStream.alias("map", java.util.Map.class);
		xStream.registerConverter(new MapEntryConverter());
		String cleanInput = xml.replaceAll("\r", "").replaceAll("\n", "");
		Map<String, Object> objectAsMap = (Map<String,Object>)xStream.fromXML(cleanInput);
		return objectAsMap;
	}
	
	
	
	

    private static class MapEntryConverter implements Converter {

        public boolean canConvert(Class clazz) {
            return AbstractMap.class.isAssignableFrom(clazz);
        }

        public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {

            AbstractMap map = (AbstractMap) value;
            for (Object obj : map.entrySet()) {
                Map.Entry entry = (Map.Entry) obj;
                writer.startNode(entry.getKey().toString());
                writer.setValue(entry.getValue().toString());
                writer.endNode();
            }

        }

        public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {

            Map<String, Object> map = new HashMap<String, Object>();

            if (reader.hasMoreChildren()){
	            while(reader.hasMoreChildren()) {
	                reader.moveDown();
	                if (reader.hasMoreChildren()){
	                	 while(reader.hasMoreChildren()) {
	                		 reader.moveDown();
	                		 String sonName = reader.getNodeName();
	                		 map.put(sonName,unmarshal(reader, context));
	                		 reader.moveUp();
	                	 }
	                } else {
		                String key = reader.getNodeName(); // nodeName aka element's name
		                String value = reader.getValue();
		                map.put(key, value);
	                }
	                reader.moveUp();
	            }
            } else return reader.getValue();
            return map;
        }
    }
	
}
