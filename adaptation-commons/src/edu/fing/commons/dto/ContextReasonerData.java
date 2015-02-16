package edu.fing.commons.dto;

import java.util.Date;
import java.util.List;

public class ContextReasonerData {

	private String user;
	private String service;
	private String operation;
	private String serviceUrl;
	private String situationName;
	private int priority;
	private Date expirationDate;
	private List<AdaptationTO> adaptations;

	public String getUser() {
		return user;
	}

	public String getService() {
		return service;
	}

	public String getOperation() {
		return operation;
	}

	public List<AdaptationTO> getAdaptations() {
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

	public void setAdaptations(List<AdaptationTO> adaptations) {
		this.adaptations = adaptations;
	}

	public int getPriority() {
		return priority;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	public String getSituationName() {
		return situationName;
	}

	public void setSituationName(String situationName) {
		this.situationName = situationName;
	}

}
