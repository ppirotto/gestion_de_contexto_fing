package edu.fing.contenxt.management;

import org.primefaces.model.UploadedFile;

import edu.fing.commons.constant.AdaptationType;

public class AdaptationDto {
	private int id;
	private AdaptationType adaptationType;
	private UploadedFile data;
	private String extraData;

	public AdaptationDto(int id, AdaptationType adaptationType) {
		super();
		this.id = id;
		this.adaptationType = adaptationType;
	}

	public AdaptationType getAdaptationType() {
		return this.adaptationType;
	}

	public UploadedFile getData() {
		return this.data;
	}

	public String getExtraData() {
		return this.extraData;
	}

	public int getId() {
		return this.id;
	}

	public void setAdaptationType(AdaptationType adaptationType) {
		this.adaptationType = adaptationType;
	}

	public void setData(UploadedFile data) {
		this.data = data;
	}

	public void setExtraData(String extraData) {
		this.extraData = extraData;
	}

	public void setId(int id) {
		this.id = id;
	}

}
