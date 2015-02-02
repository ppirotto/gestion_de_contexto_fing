package com.example.switchyard.example;

import org.switchyard.component.bean.Service;

import com.thoughtworks.xstream.XStream;

@Service(ExampleService.class)
public class ExampleServiceBean implements ExampleService {

	@Override
	public String sayHello(String name) {

		XStream x = new XStream();
		return "Hola, " + name;
	}

}
