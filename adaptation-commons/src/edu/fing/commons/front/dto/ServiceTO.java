package edu.fing.commons.front.dto;

import java.util.List;

public class ServiceTO {

	private Long id;
	private String serviceName;
	private String operationName;
	private List<String> operationNames;
	private String description;
	private String url;
	private List<SituationTO> situations;

	public String getDescription() {
		return this.description;
	}

	public Long getId() {
		return this.id;
	}

	public String getOperationName() {
		return this.operationName;
	}

	public List<String> getOperationNames() {
		return this.operationNames;
	}

	public String getServiceName() {
		return this.serviceName;
	}

	public List<SituationTO> getSituations() {
		return this.situations;
	}

	public String getUrl() {
		return this.url;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName.trim();
	}

	public void setOperationNames(List<String> operationNames) {
		this.operationNames = operationNames;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName.trim();
	}

	public void setSituations(List<SituationTO> situations) {
		this.situations = situations;
	}

	public void setUrl(String url) {
		this.url = url.trim();
	}

}
