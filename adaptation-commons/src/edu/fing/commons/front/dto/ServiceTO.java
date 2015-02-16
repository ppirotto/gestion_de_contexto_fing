package edu.fing.commons.front.dto;

public class ServiceTO {

	private Long id;
	private String serviceName;
	private String operationName;
	private String description;
	private String url;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getServiceName() {
		return serviceName;
	}

	public String getOperationName() {
		return operationName;
	}

	public String getDescription() {
		return description;
	}

	public String getUrl() {
		return url;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
