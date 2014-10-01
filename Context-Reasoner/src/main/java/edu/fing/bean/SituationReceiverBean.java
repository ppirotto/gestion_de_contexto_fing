package edu.fing.bean;

import java.util.HashMap;

import org.switchyard.component.bean.Service;

@Service(SituationReceiver.class)
public class SituationReceiverBean implements SituationReceiver {

	@Override
	public String receiveSituationFromCEP(HashMap<String, String> input) {
		
		System.out.println(input.toString());
		
		return "hola pirotto";
	}

}
