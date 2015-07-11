package edu.fing.cep.engine.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
	private void main() {
		Map info = new HashMap<String, Object>();
		List list = ((List) info.get("list"));
		if (!list.isEmpty()) {
			for (Object object : list) {
				Map cityInfo = (Map) object;
				String cityWeather = ((String) ((Map) ((List) cityInfo
						.get("wheather")).get(0)).get("main"));
				String cityName = (String) cityInfo.get("name");

			}
			// Map place = (Map) ((List)responseMap.get("results")).get(0);

		}
	}
}

// package edu.fing.drools;
// import java.util.HashMap;
// import java.util.Map;
// import edu.fing.commons.dto.ContextualDataTO;
//
// declare InCityRaining
// @role(event)
// userId:String
// city:String
// end
//
// declare CityWeather_update
// @role(event)
// weather:String
// city:String
// end
//
// declare CityWeather
// @role(event)
// weather:String
// city:String
// end
//
// rule "CityWeather message mapper"
// when
// message:ContextualDataTO(this.getEventName() == "CityWeather")
// then
// System.out.println("Mapping input message to CityWeather...");
// Map<String,Object> info = message.getInfo();
//
// /*La variable donde se carga la fuente de contexto DEBE llamarse inputEvent*/
// CityWeather inputEvent = new CityWeather();
//
// List list = ((List)info.get("list"));
// if (!list.isEmpty()){
// for
// Map place = (Map) ((List)responseMap.get("results")).get(0);
//
// /*Cargar los inputs en el evento*/
// inputEvent.setWeather((String)info.get("weather"));
// inputEvent.setCity((String)info.get("city"));
// insert(inputEvent);
// end
//
// rule "Notify Situation InCityRaining"
// when
// //Condicion para detectar situación
// then
// HashMap<String,Object> contextualData = new HashMap<String,Object>();
// /*
// Cargar hash de data contextual
// -El nombre de la variable DEBE ser contextualData
// -Las claves del hash deben ser strings "entre comillas"
// */
// contextualData.put("city",/*VALOR_city*/);
// /*Sustituir USER_ID por el id del usuario de tipo String*/
// situationDetected(/*USER_ID*/, "InCityRaining",contextualData);
// end

