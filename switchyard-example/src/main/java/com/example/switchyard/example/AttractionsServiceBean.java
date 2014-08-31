package com.example.switchyard.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.switchyard.Context;
import org.switchyard.Property;
import org.switchyard.component.bean.Reference;
import org.switchyard.component.bean.Service;

@Service(AttractionsService.class)
public class AttractionsServiceBean implements AttractionsService {

	@Inject
	private Context context;
	
//	@Inject
//	@Reference
//	private ToLogInterface logInterface;
//	
	@Override
	public List<String> getAttractions(String city) {
		Set<Property> set = context.getProperties();
		for (Property property : set) {
			System.out.println("'" + property.getName()+ "': " + property.getValue());
		}
		List<String> res = new ArrayList<String>();
		if(city!=null){
			res.add("Baile");
			res.add("Teatro");
			res.add("Parque");
			res.add("Cine");			
		}
		res.add("Bar");
		res.add("Biblioteca");
//		logInterface.exampleOperation("asd");
		return res;
	}

}
