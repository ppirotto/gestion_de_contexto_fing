package edu.fing.drools;
import java.util.HashMap;
import java.util.Map;
import edu.fing.commons.dto.ContextualDataTO;

<#list rule.mappedContextData as contextSourceTO>
	declare ${rule.situationName}_${contextSourceTO.eventName}
	@role(event)
	<#list contextSourceTO.contextData as contextDatum>
		${contextDatum}:String
	</#list>
	end
</#list>


<#list rule.mappedContextData as contextSourceTO>
	
	rule "${rule.situationName}_${contextSourceTO.eventName} message mapper"
	when
	    message:ContextualDataTO(this.getEventName() == "${contextSourceTO.eventName}")
	then
	    System.out.println("Mapping input message to ${rule.situationName}_${contextSourceTO.eventName}...");
		Map<String,Object> info = message.getInfo();
		
		/*La variable donde se carga la fuente de contexto DEBE llamarse inputEvent*/
		${rule.situationName}_${contextSourceTO.eventName} inputEvent = new ${rule.situationName}_${contextSourceTO.eventName}();
		
		/*Cargar los inputs en el evento*/
	<#list contextSourceTO.contextData as contextDatum>
		inputEvent.set${contextDatum?cap_first}((String)info.get("${contextDatum}"));
	</#list>
		insert(inputEvent);
	end
</#list>

	rule "Notify Situation ${rule.situationName}"
	when
		//Condición para detectar situación
	then
	    HashMap<String,Object> contextualData = new HashMap<String,Object>();
	    /*
	    Cargar hash de data contextual
	    -El nombre de la variable DEBE ser contextualData
	    -Las claves del hash deben ser strings "entre comillas"
	    */
	<#list rule.selectedOutputData as outputDatum>
		contextualData.put("${outputDatum}",/*VALOR_${outputDatum}*/);
	</#list>
	
	    /*Sustituir USER_ID por el id del usuario de tipo String*/
		situationDetected(/*USER_ID*/, "${rule.situationName}",contextualData);
	end

