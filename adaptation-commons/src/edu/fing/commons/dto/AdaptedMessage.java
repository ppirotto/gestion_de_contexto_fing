package edu.fing.commons.dto;

import java.util.List;

public class AdaptedMessage {

	private String message;
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
}