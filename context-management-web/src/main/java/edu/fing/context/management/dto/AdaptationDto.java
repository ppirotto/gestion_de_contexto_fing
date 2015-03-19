package edu.fing.context.management.dto;

import java.io.Serializable;

import edu.fing.commons.constant.AdaptationType;
import edu.fing.commons.front.dto.AdaptationTreeNodeTO;

public class AdaptationDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private AdaptationType adaptationType;
	private String description;
	private String data;
	private AdaptationTreeNodeTO tree;

	public AdaptationDto(int id, AdaptationType adaptationType, String desc) {
		super();
		this.id = id;
		this.description = desc;
		this.adaptationType = adaptationType;
	}

	public AdaptationType getAdaptationType() {
		return this.adaptationType;
	}

	public String getData() {
		return this.data;
	}

	public String getDescription() {
		return this.description;
	}

	public int getId() {
		return this.id;
	}

	public void setAdaptationType(AdaptationType adaptationType) {
		this.adaptationType = adaptationType;
	}

	public void setData(String data) {
		this.data = data;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(int id) {
		this.id = id;
	}

	public AdaptationTreeNodeTO getTree() {
		return tree;
	}

	public void setTree(AdaptationTreeNodeTO tree) {
		this.tree = tree;
	}

}
