package edu.fing.commons;

import java.util.List;

public class AdaptedMessage {

	private String message;
	private String header;
	private List<Adaptation> adaptations;

	public List<Adaptation> getAdaptations() {
		return this.adaptations;
	}

	public String getHeader() {
		return this.header;
	}

	public String getMessage() {
		return this.message;
	}

	public void setAdaptations(List<Adaptation> adaptations) {
		this.adaptations = adaptations;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}