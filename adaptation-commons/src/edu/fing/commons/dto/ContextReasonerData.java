package edu.fing.commons.dto;

import java.util.List;

public class ContextReasonerData {

	private String user;
	private String service;
	private String operation;
	private List<Adaptation> adaptations;
	public String getUser() {
		return user;
	}
	public String getService() {
		return service;
	}
	public String getOperation() {
		return operation;
	}
	public List<Adaptation> getAdaptations() {
		return adaptations;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public void setService(String service) {
		this.service = service;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public void setAdaptations(List<Adaptation> adaptations) {
		this.adaptations = adaptations;
	}
	
}
