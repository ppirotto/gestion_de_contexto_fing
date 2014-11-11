package edu.fing.switchyard.Adaptation_Manager;

import org.switchyard.component.bean.Service;

import edu.fing.AdaptedMessage;

@Service(PruebaRouting.class)
public class PruebaRoutingBean implements PruebaRouting {

	@Override
	public AdaptedMessage hola(AdaptedMessage ad) {

		System.out.println("LLEGO AL PRUEBAAAAAAAAAAAAAAAAAAAAAAAAA");
		return ad;
	}
}
