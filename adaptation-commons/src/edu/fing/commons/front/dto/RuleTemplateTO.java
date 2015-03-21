package edu.fing.commons.front.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RuleTemplateTO {

	private String description; 
	private String name;
	private long duration;
	private Map<String, ContextSourceTO> mappedContextData = new HashMap<String, ContextSourceTO>();
	private List<String> selectedOutputDatum;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}
	public Map<String, ContextSourceTO> getMappedContextData() {
		return mappedContextData;
	}
	public void setMappedContextData(Map<String, ContextSourceTO> mappedContextData) {
		this.mappedContextData = mappedContextData;
	}
	public List<String> getSelectedOutputDatum() {
		return selectedOutputDatum;
	}
	public void setSelectedOutputDatum(List<String> selectedOutputDatum) {
		this.selectedOutputDatum = selectedOutputDatum;
	}

}
