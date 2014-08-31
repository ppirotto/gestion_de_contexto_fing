package com.example.switchyard.example;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.xml.namespace.QName;

import org.springframework.beans.factory.annotation.Autowired;
import org.switchyard.component.bean.Reference;
import org.switchyard.component.bean.Service;
import org.switchyard.internal.DefaultContext;
import org.switchyard.remote.RemoteInvoker;
import org.switchyard.remote.RemoteMessage;
import org.switchyard.remote.http.HttpInvoker;

@Service(ServicioA.class)
public class ServicioABean implements ServicioA {
	
	@Inject
    @Reference
	private ConsumeGetAttractions consumeGetAttractions;
	
	@Override
	public String apendear(String name) {
		RemoteInvoker invoker = new HttpInvoker("http://localhost:8080/attractions-provider/AttractionsService");
        String message = "<Envelope xmlns=\"http://schemas.xmlsoap.org/soap/envelope/\"> <Body> <getAttractions xmlns=\"urn:com.example.switchyard:switchyard-example:1.0\">"+
            "<city>"+"MONTEVIDEO"+"</city></getAttractions></Body></Envelope>";

//        List<String> attractions = consumeGetAttractions.getAttractions(message);
        System.out.println("PASO POR A");
        System.out.println(name);
		return  name + "PASO POR A";
	}

}
