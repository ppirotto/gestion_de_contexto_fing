package edu.fing.commons.dto;

import java.util.Map;

public class ContextualDataTO {

	private String type;
	private Map<String,Object> info;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Map<String,Object> getInfo() {
		return info;
	}
	public void setInfo(Map<String,Object> info) {
		this.info = info;
	}
	
}
