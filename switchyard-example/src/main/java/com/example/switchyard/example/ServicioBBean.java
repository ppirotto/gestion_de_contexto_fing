package com.example.switchyard.example;

import org.switchyard.component.bean.Service;

@Service(ServicioB.class)
public class ServicioBBean implements ServicioB {
	
	@Override
	public String apendear(String name) {
		System.out.println("PASO POR B");
		System.out.println(name);
		return name + "PASO POR B";
	}

}
