package com.example.switchyard.example;

import javax.xml.namespace.QName;

import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.switchyard.component.test.mixins.cdi.CDIMixIn;
import org.switchyard.test.Invoker;
import org.switchyard.test.ServiceOperation;
import org.switchyard.test.SwitchYardRunner;
import org.switchyard.test.SwitchYardTestCaseConfig;
import org.switchyard.test.SwitchYardTestKit;

@RunWith(SwitchYardRunner.class)
@SwitchYardTestCaseConfig(config = SwitchYardTestCaseConfig.SWITCHYARD_XML, mixins = { CDIMixIn.class })
public class ExampleServiceTest {

	private SwitchYardTestKit testKit;
	private CDIMixIn cdiMixIn;
	@ServiceOperation("ExampleService")
	private Invoker service;

	@Test
	public void testSayHello() throws Exception {
		// TODO Auto-generated method stub
		// initialize your test message
		String message = "Bob";
		String result = service.operation("sayHello").sendInOut(message)
				.getContent(String.class);

		// validate the results
		Assert.assertTrue("Hola, Bob".equals(result));
	}

	@Test
	public void testSayHelloTransform() throws Exception {
		final QName inputType = QName
				.valueOf("{urn:com.example.switchyard:switchyard-example:1.0}sayHello");
		final QName outputType = QName
				.valueOf("{urn:com.example.switchyard:switchyard-example:1.0}sayHelloResponse");
		// initialize your test message
		Object message = "<sayHello xmlns=\"urn:com.example.switchyard:switchyard-example:1.0\"><string>Bob</string></sayHello>";
		String result = service.operation("sayHello").inputType(inputType)
				.expectedOutputType(outputType).sendInOut(message)
				.getContent(String.class);

		// validate the results
		String control = "<sayHelloResponse xmlns=\"urn:com.example.switchyard:switchyard-example:1.0\"><string>Hello, Bob</string></sayHelloResponse>";
		Assert.assertTrue("Unexpected result: " + result,
				XMLUnit.compareXML(control, result).identical());
	}

}
