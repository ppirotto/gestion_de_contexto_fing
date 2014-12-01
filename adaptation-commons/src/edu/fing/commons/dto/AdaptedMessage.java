package edu.fing.commons.dto;

import java.util.List;

public class AdaptedMessage {

	private String message;
	private String itinerary;
	private String service;
	private List<Adaptation> adaptations;

	public List<Adaptation> getAdaptations() {
		return this.adaptations;
	}

	public String getMessage() {
		return this.message;
	}

	public void setAdaptations(List<Adaptation> adaptations) {
		this.adaptations = adaptations;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getItinerary() {
		return itinerary;
	}

	public void setItinerary(String itinerary) {
		this.itinerary = itinerary;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}
}