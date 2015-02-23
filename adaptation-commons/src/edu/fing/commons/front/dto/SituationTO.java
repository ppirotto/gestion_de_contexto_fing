package edu.fing.commons.front.dto;

public class SituationTO {

	private String name;
	private String description;
	private Long minuteDuration;

	public String getDescription() {
		return this.description;
	}

	public Long getMinuteDuration() {
		return this.minuteDuration;
	}

	public String getName() {
		return this.name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setMinuteDuration(Long minuteDuration) {
		this.minuteDuration = minuteDuration;
	}

	public void setName(String name) {
		this.name = name;
	}

}
