package edu.fing.switchyard.Adaptation_Manager;

import org.switchyard.component.bean.Service;

import edu.fing.commons.AdaptedMessage;

@Service(PruebaRouting.class)
public class PruebaRoutingBean implements PruebaRouting {

	@Override
	public AdaptedMessage hola(AdaptedMessage ad) {

		System.out.println("LLEGO AL PRUEBAAAAAAAAAAAAAAAAAAAAAAAAA");
		return ad;
	}
}
