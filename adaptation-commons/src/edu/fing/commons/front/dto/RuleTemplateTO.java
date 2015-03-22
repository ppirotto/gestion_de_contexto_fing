package edu.fing.commons.front.dto;

import java.util.ArrayList;
import java.util.List;

public class RuleTemplateTO {

	private String description;
	private String situationName;
	private long duration;
	private List<ContextSourceTO> mappedContextData = new ArrayList<ContextSourceTO>();
	private List<String> selectedOutputData;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public List<ContextSourceTO> getMappedContextData() {
		return mappedContextData;
	}

	public void setMappedContextData(List<ContextSourceTO> mappedContextData) {
		this.mappedContextData = mappedContextData;
	}

	public String getSituationName() {
		return situationName;
	}

	public void setSituationName(String situationName) {
		this.situationName = situationName;
	}

	public List<String> getSelectedOutputData() {
		return selectedOutputData;
	}

	public void setSelectedOutputData(List<String> selectedOutputData) {
		this.selectedOutputData = selectedOutputData;
	}

}
