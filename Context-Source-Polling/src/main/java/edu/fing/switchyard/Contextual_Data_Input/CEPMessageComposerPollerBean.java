package edu.fing.switchyard.Contextual_Data_Input;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.codehaus.jackson.map.ObjectMapper;
import org.switchyard.component.bean.Reference;
import org.switchyard.component.bean.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.collections.MapConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;

import edu.fing.commons.dto.ContextualDataTO;

@Service(CEPMessageComposerPoller.class)
public class CEPMessageComposerPollerBean implements CEPMessageComposerPoller {

	@Inject
	@Reference
	private CEPFeederService droolsFeederService;

	@Inject
	@Reference
	private ContextSourceInvoker contextSourceInvoker;
	
	private enum ContextualDataModeConverter {
		JSON, XML
	}

	private ContextualDataModeConverter mode;

	private String eventName;
	
	private String url;
	
	@PostConstruct
	void init() {
		Properties prop = new Properties();
		String propFileName = "config.properties";

		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

		if (inputStream != null) {
			try {
				prop.load(inputStream);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("ERROR trying to load '" + propFileName + "'.");
		}
		mode = Enum.valueOf(ContextualDataModeConverter.class, (String) prop.get("modeConverter"));
		eventName = (String) prop.getProperty("eventName");
		url = (String) prop.getProperty("url");
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String poll(String message) {

		String input = null;
			input = this.contextSourceInvoker.invoke();
		if (input==null){
			return "ERROR";
		}
		ObjectMapper objectMapper = new ObjectMapper();
		@SuppressWarnings("unchecked")
		Map<String, Object> objectAsMap = null;

		if (mode.equals(ContextualDataModeConverter.JSON)) {
			try {
				objectAsMap = objectMapper.readValue(input, Map.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (mode.equals(ContextualDataModeConverter.XML)) {
		    XStream xStream = new XStream(new DomDriver());
		    xStream.alias("map", java.util.Map.class);
		    xStream.registerConverter(new MapEntryConverter());
		    String cleanInput = input.replaceAll("\r", "").replaceAll("\n", "");
		    Map<String, Object> asd = (Map<String,Object>)xStream.fromXML(cleanInput);
		    objectAsMap = asd;
		}

		ContextualDataTO data = new ContextualDataTO();
		
		data.setEventName(eventName);
		data.setInfo(objectAsMap);
		for(String key : objectAsMap.keySet()){
			System.out.println("key= "+key);
			System.out.println("value= "+objectAsMap.get(key) + "\n");
		}
		String res = droolsFeederService.receiveMessage(data); 
		return res;
	}
	
    public static class MapEntryConverter implements Converter {

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


	
	public static Map<String, Object> convertNodesFromXml(String xml) throws Exception {
	    InputStream is = new ByteArrayInputStream(xml.getBytes());
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    dbf.setNamespaceAware(true);
	    DocumentBuilder db = dbf.newDocumentBuilder();
	    Document document = db.parse(is);
	    return createMap(document.getDocumentElement());
	}

	public static Map<String, Object> createMap(Node node) {
	    Map<String, Object> map = new HashMap<String, Object>();
	    NodeList nodeList = node.getChildNodes();
	    for (int i = 0; i < nodeList.getLength(); i++) {
	        Node currentNode = nodeList.item(i);
//	        if (currentNode.hasAttributes()) {
//	            for (int j = 0; j < currentNode.getAttributes().getLength(); j++) {
//	                Node item = currentNode.getAttributes().item(i);
//	                map.put(item.getNodeName(), item.getTextContent());
//	            }
//	        }
	        if (node.getFirstChild() != null && node.getFirstChild().getNodeType() == Node.ELEMENT_NODE) {
	            map.putAll(createMap(currentNode));
	        } else if (node.getFirstChild().getNodeType() == Node.TEXT_NODE && currentNode.getLocalName()!=null) {
	        	System.out.println(currentNode);
	            map.put(currentNode.getLocalName(), currentNode.getTextContent());
	            
	        }
	    }
	    return map;
	}

}
