package edu.fing.commons.front.dto;

import java.util.List;

import edu.fing.commons.dto.AdaptationTO;

public class ItineraryTO {

	private String situationName;
	private ServiceTO service;
	private int priority;
	private List<AdaptationTO> adaptations;

	public String getSituationName() {
		return situationName;
	}

	public ServiceTO getService() {
		return service;
	}

	public int getPriority() {
		return priority;
	}

	public List<AdaptationTO> getAdaptations() {
		return adaptations;
	}

	public void setSituationName(String situationName) {
		this.situationName = situationName;
	}

	public void setService(ServiceTO service) {
		this.service = service;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public void setAdaptations(List<AdaptationTO> adaptations) {
		this.adaptations = adaptations;
	}

}
