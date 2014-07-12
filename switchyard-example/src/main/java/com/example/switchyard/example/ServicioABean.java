package com.example.switchyard.example;

import org.switchyard.component.bean.Service;

@Service(ServicioA.class)
public class ServicioABean implements ServicioA {
	
	@Override
	public String apendear(String name) {
		return  name+ " PASO POR A";
	}

}
