package edu.fing.commons.front.dto;

import java.util.List;

import edu.fing.commons.dto.AdaptationTO;

public class SituationTO {

	private String name;
	private String description;
	private Long duration;
	private List<AdaptationTO> adaptations;
	private List<ContextSourceTO> contextSources;
	private List<String> outputContextData;
	private VersionTO versionTO;
	private int priority;

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

	public List<ContextSourceTO> getContextSources() {
		return contextSources;
	}

	public void setContextSources(List<ContextSourceTO> contextSources) {
		this.contextSources = contextSources;
	}

	public List<String> getOutputContextData() {
		return outputContextData;
	}

	public void setOutputContextData(List<String> outputContextData) {
		this.outputContextData = outputContextData;
	}

	public VersionTO getVersionTO() {
		return versionTO;
	}

	public void setVersionTO(VersionTO versionTO) {
		this.versionTO = versionTO;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

}
