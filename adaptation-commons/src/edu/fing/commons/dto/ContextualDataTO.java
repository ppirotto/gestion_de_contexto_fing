package edu.fing.commons.dto;

import java.util.Map;

public class ContextualDataTO {

	private String eventName;
	private Map<String,Object> info;
	
	
	public Map<String,Object> getInfo() {
		return info;
	}
	public void setInfo(Map<String,Object> info) {
		this.info = info;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	
}
