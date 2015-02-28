package edu.fing.commons.front.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.fing.commons.dto.AdaptationTO;

public class ConfiguredItineraryTO {

	private String user;
	private String service;
	private String operation;
	private String situation;
	private int priority;
	private Date expirationDate;
	private List<AdaptationTO> adaptationDirective = new ArrayList<AdaptationTO>();
	private String adaptationNames;

	public List<AdaptationTO> getAdaptationDirective() {
		return this.adaptationDirective;
	}

	public Date getExpirationDate() {
		return this.expirationDate;
	}

	public String getOperation() {
		return this.operation;
	}

	public int getPriority() {
		return this.priority;
	}

	public String getService() {
		return this.service;
	}

	public String getSituation() {
		return this.situation;
	}

	public String getUser() {
		return this.user;
	}

	public void setAdaptationDirective(List<AdaptationTO> adaptationDirective) {
		this.adaptationDirective = adaptationDirective;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public void setService(String service) {
		this.service = service;
	}

	public void setSituation(String situation) {
		this.situation = situation;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getAdaptationNames() {
		return adaptationNames;
	}

	public void setAdaptationNames(String adaptationNames) {
		this.adaptationNames = adaptationNames;
	}

}
