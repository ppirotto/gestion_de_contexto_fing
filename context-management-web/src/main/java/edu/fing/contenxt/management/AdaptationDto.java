package edu.fing.contenxt.management;

import org.primefaces.model.UploadedFile;

import edu.fing.contenxt.management.ItineraryBean.AdaptationType;

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

	public String getExtraData() {
		return this.extraData;
	}

	public int getId() {
		return this.id;
	}

	public void setAdaptationType(AdaptationType adaptationType) {
		this.adaptationType = adaptationType;
	}

	public void setExtraData(String extraData) {
		this.extraData = extraData;
	}

	public void setId(int id) {
		this.id = id;
	}

	public UploadedFile getData() {
		return data;
	}

	public void setData(UploadedFile data) {
		this.data = data;
	}

}
