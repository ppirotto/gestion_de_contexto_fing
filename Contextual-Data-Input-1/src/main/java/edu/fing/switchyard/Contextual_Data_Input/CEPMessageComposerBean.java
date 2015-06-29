package edu.fing.switchyard.Contextual_Data_Input;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.xml.sax.InputSource;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;

import edu.fing.commons.dto.ContextualDataTO;

@Service(CEPMessageComposer.class)
public class CEPMessageComposerBean implements CEPMessageComposer {

	@Inject
	@Reference
	private CEPFeederService droolsFeederService;

	private enum ContextualDataModeConverter {
		JSON, XML
	}

	private ContextualDataModeConverter mode;

	private String eventName;
	
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
	}

	@SuppressWarnings("unchecked")
	@Override
	public String send(String input) {

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
			String cleanInput = input.replaceAll("\r", "").replaceAll("\n", "");
			DocumentBuilderFactory dbf =DocumentBuilderFactory.newInstance();
            DocumentBuilder db;
			try {
				db = dbf.newDocumentBuilder();
				Document doc = db.parse(new InputSource(new ByteArrayInputStream(cleanInput.getBytes("utf-8"))));
				String root = doc.getDocumentElement().getNodeName();
			    XStream xStream = new XStream(new DomDriver());
			    xStream.alias(root, java.util.Map.class);
			    xStream.registerConverter(new MapEntryConverter());
			    objectAsMap = (Map<String,Object>)xStream.fromXML(cleanInput);				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
		                String key = reader.getNodeName();
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
