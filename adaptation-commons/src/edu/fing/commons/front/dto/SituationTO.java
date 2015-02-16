package edu.fing.commons.front.dto;

public class SituationTO {

	private String name;
	private String description;
	private Long minuteDuration;

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Long getMinuteDuration() {
		return minuteDuration;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setMinuteDuration(Long minuteDuration) {
		this.minuteDuration = minuteDuration;
	}

}
