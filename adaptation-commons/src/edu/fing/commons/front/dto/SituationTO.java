package edu.fing.commons.front.dto;

import java.util.List;

import edu.fing.commons.dto.AdaptationTO;

public class SituationTO {

	private String name;
	private String description;
	private Long duration;
	private List<AdaptationTO> adaptations;

	public String getDescription() {
		return this.description;
	}

	public Long getDuration() {
		return this.duration;
	}

	public String getName() {
		return this.name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<AdaptationTO> getAdaptations() {
		return adaptations;
	}

	public void setAdaptations(List<AdaptationTO> adaptations) {
		this.adaptations = adaptations;
	}

}
