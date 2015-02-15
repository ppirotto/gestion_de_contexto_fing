package edu.fing.commons.dto;

public class AdaptationTO {

	private String name;
	private String uri;
	private Object data;

	public String getName() {
		return name;
	}

	public Object getData() {
		return data;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

}
