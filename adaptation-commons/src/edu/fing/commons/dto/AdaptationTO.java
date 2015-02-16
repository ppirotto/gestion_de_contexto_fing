package edu.fing.commons.dto;

import edu.fing.commons.constant.AdaptationType;

public class AdaptationTO {

	private String name;
	private String uri;
	private String description;
	private int order;
	private AdaptationType adaptationType;
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

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public AdaptationType getAdaptationType() {
		return adaptationType;
	}

	public void setAdaptationType(AdaptationType adaptationType) {
		this.adaptationType = adaptationType;
	}

}
