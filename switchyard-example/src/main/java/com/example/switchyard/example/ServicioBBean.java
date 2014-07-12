package com.example.switchyard.example;

import org.switchyard.component.bean.Service;

@Service(ServicioB.class)
public class ServicioBBean implements ServicioB {
	
	@Override
	public String apendear(String name) {
		return name + "PASO POR B";
	}

}
