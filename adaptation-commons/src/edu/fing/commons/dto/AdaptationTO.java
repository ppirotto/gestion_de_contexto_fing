package edu.fing.commons.dto;

import edu.fing.commons.constant.AdaptationType;

public class AdaptationTO {

	private String description;
	private int order;
	private AdaptationType adaptationType;
	private Object data;

	public AdaptationType getAdaptationType() {
		return this.adaptationType;
	}

	public Object getData() {
		return this.data;
	}

	public String getDescription() {
		return this.description;
	}

	public int getOrder() {
		return this.order;
	}

	public void setAdaptationType(AdaptationType adaptationType) {
		this.adaptationType = adaptationType;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setOrder(int order) {
		this.order = order;
	}

}
