package edu.fing.commons.dto;

import java.util.Map;

public class SituationDetectedTO {

	private String situationName;
	private Map<String,Object> contextualData;
	private String userId;
	
	public String getSituationName() {
		return situationName;
	}
	public void setSituationName(String situationName) {
		this.situationName = situationName;
	}
	public Map<String,Object> getContextualData() {
		return contextualData;
	}
	public void setContextualData(Map<String,Object> contextualData) {
		this.contextualData = contextualData;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
	
}
