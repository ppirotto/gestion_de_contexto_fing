package edu.fing.commons.front.dto;

import java.util.List;

public class ServiceTO {

	private Long id;
	private String serviceName;
	private String operationName;
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

	public String getServiceName() {
		return this.serviceName;
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
		this.operationName = operationName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<SituationTO> getSituations() {
		return situations;
	}

	public void setSituations(List<SituationTO> situations) {
		this.situations = situations;
	}

}
